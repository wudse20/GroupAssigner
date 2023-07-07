package se.skorup.util.io;

import org.junit.jupiter.api.Test;
import se.skorup.group.Group;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The class responsible for testing the serialization manager.
 * */
public class TestSerializationUtil
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
        var gm = new Group("KAKA");
        gm.registerPerson("Anton");
        gm.registerPerson("Sebbe");

        var path = "Hej.txt";

        SerializationUtil.serializeObject(path, gm);
        var res = (Group) SerializationUtil.deserializeObject(path);

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
            () -> SerializationUtil.serializeObject(null, "")
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> SerializationUtil.serializeObject("", null)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> SerializationUtil.deserializeObject(null)
        );
    }
}
