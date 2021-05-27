package se.skorup.main.manager.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The class with some static methods to
 * handle deserialization and serialization
 * of different objects that implements the
 * serialization interface.
 * */
public class SerializationManager
{
    /**
     * Serializes an object to a file with path: path.
     *
     * @param path the path of the file.
     * @param object the object to be serialized.
     * @param <T> the type of the object being serialized.
     * @throws IOException  if the file exists but is a directory
     *                      rather than a regular file,
     *                      does not exist but cannot be created,
     *                      or cannot be opened for any other reason.
     *                      if an I/O error occurs while writing stream header.
     * @throws ClassCastException iff object doesn't implement serializable.
     * @throws IllegalArgumentException iff one of the params is null.
     * */
    public static <T> void serializeObject(String path, T object) throws IOException, ClassCastException, IllegalArgumentException
    {
        if (object == null || path == null)
            throw new IllegalArgumentException("Passed argument cannot be null");

        if (object instanceof Serializable s)
        {
            var fos = new FileOutputStream(path);
            var oos = new ObjectOutputStream(fos);

            oos.writeObject(s);
            oos.flush();
            oos.close();

            fos.close();
        }
        else
        {
            throw new ClassCastException("%s is not an instanceof Serializable".formatted(object.getClass()));
        }
    }

    /**
     * Deserializes a object.
     *
     * @param path the path to the object file.
     * @return the object deserialized, as type object!!!
     * @throws IOException if the file does not exist,
     *                     is a directory rather than a regular file,
     *                     or for some other reason cannot be opened for reading.
     *                     if an I/O error occurs while reading stream header.
     * @throws ClassNotFoundException Class of a serialized object cannot be found.
     * @throws IllegalArgumentException iff path == null is true.
     * */
    public static Object deserializeObject(String path) throws IOException, ClassNotFoundException, IllegalArgumentException
    {
        if (path == null)
            throw new IllegalArgumentException("Passed argument cannot be null");

        var fis = new FileInputStream(path);
        var ois = new ObjectInputStream(fis);

        var obj = ois.readObject();

        fis.close();
        ois.close();

        return obj;
    }
}
