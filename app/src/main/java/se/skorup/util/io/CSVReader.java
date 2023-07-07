package se.skorup.util.io;

import se.skorup.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * A class for reading CSV-files.
 * */
public class CSVReader
{
    /**
     * Reads a CSV-file and creates a matrix representing the csv-file.
     * It will also convert all strings to name case using
     * {@link Utils#toNameCase toNameCase}.
     *
     * @param path the path to the CSV-file.
     * @return a matrix of name case strings representing the csv-file.
     * @throws IOException iff file reading fails.
     * */
    public static String[][] readCSV(String path) throws IOException
    {
        var data = MyFileReader.readFile(new File(path));

        if (data.trim().isEmpty())
            return new String[0][0];

        var lines = Arrays.stream(data.trim().split("\n"))
                          .toArray(String[]::new);

        if (lines.length == 0)
            return new String[0][0];

        return Arrays.stream(lines)
                     .map(s -> Arrays.stream(s.split(s.indexOf(',') != -1 ? "," : ";"))
                     .map(Utils::toNameCase).toArray(String[]::new))
                     .toArray(String[][]::new);
    }
}
