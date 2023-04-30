package se.skorup.group;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGroup
{
    @Test
    public void testRegisterSingleThread()
    {
        var cnt = 10000;
        var al = new ArrayList<String>();
        var ids = new HashSet<Integer>();
        var gr = new Group("Test");

        for (var i = 0; i < cnt; i++)
        {
            var name = UUID.randomUUID().toString();
            al.add(name);
            ids.add(gr.registerPerson(name));
        }

        assertEquals(
            al.size(), gr.getNames().size(),
            "There should be exactly %d names, got: %d"
            .formatted(cnt, gr.getNames().size())
        );

        assertEquals(al, gr.getNames(), "All names should be there.");

        assertEquals(
            al.size(), gr.getIds().size(),
            "There should be exactly %d ids, got: %d"
            .formatted(cnt, gr.getIds().size())
        );

        assertEquals(ids, gr.getIds(), "All ids should be there.");
    }

    @Test
    public void testRegisterMultiThreaded() throws InterruptedException
    {
        var cnt = 10000;
        var al = Collections.synchronizedList(new ArrayList<String>());
        Set<Integer> ids = ConcurrentHashMap.newKeySet();
        var gr = new Group("Test");

        var t1 = new Thread(() -> {
            for (var i = 0; i < cnt / 2; i++)
            {
                var name = UUID.randomUUID().toString();
                al.add(name);
                ids.add(gr.registerPerson(name));
            }
        }, "testRegisterMultiThreaded-1");

        var t2 = new Thread(() -> {
            for (var i = cnt/2; i < cnt; i++)
            {
                var name = UUID.randomUUID().toString();
                al.add(name);
                ids.add(gr.registerPerson(name));
            }
        }, "testRegisterMultiThreaded-2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(
            al.size(), gr.getNames().size(),
            "There should be exactly %d names, got: %d"
            .formatted(cnt, gr.getNames().size())
        );

        al.sort(String::compareTo);
        var names = gr.getNames().stream().sorted(String::compareTo).toList();
        assertEquals(al, names, "All names should be there.");

        assertEquals(
            al.size(), gr.getIds().size(),
            "There should be exactly %d ids, got: %d"
            .formatted(cnt, gr.getIds().size())
        );

        assertEquals(ids, gr.getIds(), "All ids should be there.");
    }
}
