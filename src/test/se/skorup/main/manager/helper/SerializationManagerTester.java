package se.skorup.main.manager.helper;

import org.testng.annotations.Test;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

/**
 * The class responsible for testing the serialization manager.
 * */
public class SerializationManagerTester
{
    /**
     * Tests serialization and deserialization of an object.
     *
     * @throws IOException iff test fails.
     * @throws ClassNotFoundException iff test fails.
     * */
    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException
    {
        var gm = new GroupManager("KAKA");
        gm.registerPerson("Anton", Person.Role.LEADER);
        gm.registerPerson("Sebbe", Person.Role.CANDIDATE);

        var path = "Hej.txt";

        SerializationManager.serializeObject(path, gm);
        var res = (GroupManager) SerializationManager.deserializeObject(path);

        assertEquals(gm, res);

        new File(path).deleteOnExit();
    }

    /**
     * Tests serialization of null.
     * */
    @Test
    public void testNull()
    {
        assertThrows(
                IllegalArgumentException.class,
                () -> SerializationManager.serializeObject(null, "")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> SerializationManager.serializeObject("", null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> SerializationManager.deserializeObject(null)
        );
    }
}
