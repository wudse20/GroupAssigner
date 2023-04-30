package se.skorup.API.util;

import org.junit.jupiter.api.Test;

import javax.crypto.SealedObject;
import java.io.File;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestEncryptedSerializationUtil
{
    @Test
    public void testSerializationAndDeserialization()
    {
        var path = "./test.test";

        try
        {
            var obj = new TestDataStructure("Kalle", 123, List.of(1, 2, 3, 4, 5));
            EncryptedSerializationUtil.serializeObject(path, obj);
            var decrypted = (TestDataStructure) EncryptedSerializationUtil.deserializeObject(path);
            assertEquals(
                obj, decrypted, "The two objects must match! obj: %s, decrypted: %s".formatted(obj, decrypted)
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            var msg = e.getLocalizedMessage();
            fail(msg);
        }
        finally
        {
            new File(path).delete();
        }
    }

    @Test
    public void testDeserializeEncrypted()
    {
        var path = "./test.test";

        try
        {
            var obj = new TestDataStructure("Kalle", 123, List.of(1, 2, 3, 4, 5));
            EncryptedSerializationUtil.serializeObject(path, obj);
            var deserialized = SerializationUtil.deserializeObject(path);
            assertTrue(deserialized instanceof SealedObject, "The object is not an instance of SealedObject");
            assertThrows(
                InvalidKeyException.class,
                () -> ((SealedObject) deserialized).getObject(EncryptedSerializationUtil.generateKey()),
                "The object should not be able to be decrypted"
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            var msg = e.getLocalizedMessage();
            fail(msg);
        }
        finally
        {
            new File(path).delete();
        }
    }

    private record TestDataStructure(String s, int i, List<Integer> l) implements Serializable {}
}
