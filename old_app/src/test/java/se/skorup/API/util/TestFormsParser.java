package se.skorup.API.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFormsParser
{
    private static GroupManager gm1()
    {
        var gm1 = new GroupManager("");
        var p1 = gm1.registerPerson("Agnes", Person.Role.CANDIDATE);
        var p2 = gm1.registerPerson("Emma", Person.Role.CANDIDATE);
        var p3 = gm1.registerPerson("Linnea", Person.Role.CANDIDATE);
        var p4 = gm1.registerPerson("Alice", Person.Role.CANDIDATE);
        var p5 = gm1.registerPerson("Stella", Person.Role.CANDIDATE);
        var p6 = gm1.registerPerson("Isabelle", Person.Role.CANDIDATE);
        var p7 = gm1.registerPerson("Allie", Person.Role.CANDIDATE);
        var p8 = gm1.registerPerson("Bella", Person.Role.CANDIDATE);

        p1.addWishlistId(p2.getId());
        p1.addWishlistId(p3.getId());
        p1.addWishlistId(p2.getId());

        p4.addWishlistId(p8.getId());
        p4.addWishlistId(p6.getId());
        p4.addWishlistId(p5.getId());
        p4.addWishlistId(p5.getId());
        p4.addWishlistId(p6.getId());
        p4.addWishlistId(p3.getId());

        p8.addWishlistId(p4.getId());
        p8.addWishlistId(p6.getId());
        p8.addWishlistId(p5.getId());

        p2.addWishlistId(p3.getId());
        p2.addWishlistId(p2.getId());
        p2.addWishlistId(p1.getId());
        p2.addWishlistId(p1.getId());
        p2.addWishlistId(p2.getId());
        p2.addWishlistId(p3.getId());

        p6.addWishlistId(p7.getId());
        p6.addWishlistId(p5.getId());
        p6.addWishlistId(p4.getId());

        p3.addWishlistId(p2.getId());
        p3.addWishlistId(p1.getId());
        p3.addWishlistId(p2.getId());

        p5.addWishlistId(p6.getId());
        p5.addWishlistId(p7.getId());
        p5.addWishlistId(p3.getId());

        return gm1;
    }

    public static Stream<Arguments> getData()
    {
        return Stream.of(
            Arguments.of("./src/test/testData/test_data.csv", gm1())
        );
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testData(File path, GroupManager expected) throws IOException
    {
        var gm = new GroupManager("");
        var data = MyFileReader.readFile(path);
        FormsParser.parseFormData(data, gm);
        assertEquals(expected, gm, "GroupManagers aren't equal!");
    }
}
