package se.skorup.API.util;

import se.skorup.API.collections.immutable_collections.ImmutableArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * The file reader that reads files.
 * */
public class MyFileReader
{
    /**
     * Read many files and return their content.
     *
     * @param files the files to be read.
     * @return the contents of the files in an array.
     * @throws IOException if the IO operation fails.
     * */
    public static ImmutableArray<String> readFiles(File[] files) throws IOException
    {
        var res = new ArrayList<String>();

        for (var f : files)
        {
            if (!f.exists())
               continue;

            res.add(readFile(f));
        }

        return ImmutableArray.fromList(res);
    }

    /**
     * Reads a file and returns its content.
     *
     * @param f the file to be read.
     * @return the content of the file.
     * @throws IOException if the IO operation fails.
     * */
    public static String readFile(File f) throws IOException
    {
        var fr = new FileReader(f, StandardCharsets.UTF_8);
        var br = new BufferedReader(fr);
        var lines = new StringBuilder();

        String tmp;
        while ((tmp = br.readLine()) != null)
            lines.append(tmp).append("\n");

        fr.close();
        br.close();

        return lines.toString();
    }
}
