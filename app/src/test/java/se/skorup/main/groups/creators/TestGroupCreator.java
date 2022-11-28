package se.skorup.main.groups.creators;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.API.util.FormsParser;
import se.skorup.API.util.MyFileReader;
import se.skorup.main.manager.Group;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGroupCreator
{
    private List<Group> gms;

    @BeforeEach
    public void setUp() throws IOException
    {
        this.gms = List.of(
            new GroupManager("Kaka"),
            new GroupManager("Kaka2")
        );

        FormsParser.parseFormData(
            MyFileReader.readFile(new File("./src/test/testData/test_data.csv")), gms.get(0)
        );

        FormsParser.parseFormData(
            MyFileReader.readFile(new File("./src/test/testData/test_data_some_what_bigger.csv")),
            gms.get(1)
        );
    }

    @AfterEach
    public void tearDown()
    {
        this.gms = null;
    }

    public static Stream<Arguments> getTestData()
    {
        return Stream.of(
            Arguments.of(new RandomGroupCreator()),
            Arguments.of(new WishlistGroupCreator()),
            Arguments.of(new WishlistGroupCreator(0)),
            Arguments.of(new WishesGroupCreator())
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testGroupCreator(GroupCreator gc)
    {
        var gcString = gc instanceof WishlistGroupCreator wc ? wc.toString() + wc.startingPerson : gc.toString();

        for (var gm : gms)
        {
            var res = new AtomicReference<List<List<Set<Integer>>>>();

            assertDoesNotThrow(
                () -> res.set(gc.generate(gm, 3, false)),
                "Failed group creation with %s%ngm: %s%n".formatted(gcString, gm)
            );

            for (var r : res.get())
            {
                var all = new ImmutableHashSet<Integer>();

                for (var g : r)
                    all = all.union(g);

                assertEquals(
                   gm.getAllIdsOfRoll(Person.Role.CANDIDATE).size(), all.size(),
                   "The sizes doesn't match up correctly!%n All: %s,%n current: %s,%n all: %s%n creator: %s%n gm: %s%n"
                    .formatted(res.get(), r, all, gcString, gm)
                );
            }
        }
    }
}
