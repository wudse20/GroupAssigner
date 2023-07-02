package se.skorup.util.io;

import se.skorup.util.Log;
import se.skorup.util.Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class EncryptedSerializationUtil
{
    /** The storage location of the key, not the best to be honest but good enough. */
    public static String KEY_STORAGE = Utils.getFolderName() + "/saves/secret.data";

    /** The key that is used in encrypting the files. */
    public static final SecretKey key;

    static {
        try
        {
            key = getKey();
            SerializationUtil.serializeObject(KEY_STORAGE, key);
        }
        catch (NoSuchAlgorithmException | IOException e)
        {
            Log.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypts then serializes an object to the specified path.
     *
     * @param <T> the type of the object to be serialized.
     * @param object the object to be serialized.
     * @param path the path of the file that the object will be serialized to.
     * @throws Exception iff encryption or IO fails in some way.
     * */
    public static <T extends Serializable> void serializeObject(String path, T object) throws Exception
    {
        var cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        var sealed = new SealedObject(object, cipher);
        SerializationUtil.serializeObject(path, sealed);
    }

    /**
     * Decrypts and deserializes the object at the location path on the disc.
     *
     * @param <T> The type of the returned object.
     * @param path The path on disc of the encrypted file.
     * @throws Exception iff decryption or IO fails in some way.
     * */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserializeObject(String path) throws Exception
    {
        var cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        var obj = (SealedObject) SerializationUtil.deserializeObject(path);
        return (T) obj.getObject(cipher);
    }

    /**
     * Generates a new AES256 key.
     *
     * @return the generated AES256 key.
     * @throws NoSuchAlgorithmException if AES is not found on the computer.
     * */
    public static SecretKey generateKey() throws NoSuchAlgorithmException
    {
        var keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    /**
     * Gets the stored secret key for this computer. If it does not
     *  exist, then it will create a new one.
     *
     * @return the key used on this computer.
     * */
    private static SecretKey getKey() throws NoSuchAlgorithmException
    {
        var keyFile = new File(KEY_STORAGE);
        if (keyFile.exists())
        {
            try
            {
                return SerializationUtil.deserializeObject(KEY_STORAGE);
            }
            catch (IOException | ClassNotFoundException e)
            {
                Log.error(e);
                return generateKey();
            }
        }

        return generateKey();
    }
}
