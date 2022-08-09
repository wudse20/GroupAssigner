package se.skorup.API.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class for parsing CSV-files.
 * */
public class CSVParser
{
    /**
     * Parses a CSV-file to a matrix of Strings.<br><br>
     *
     * If it cannot parse the file or if the file doesn't
     * exist it will return new String[0][0].
     *
     * @param path the path of the file.
     * @return the parse file as a matrix.
     * */
    public static String[][] parseCSV(String path)
    {
        try
        {
            var data = MyFileReader.readFile(new File(path));

            if (data.trim().isEmpty())
                return new String[0][0];

            var splitByLine =
                Arrays.stream(data.split("\n"))
                      .toArray(String[]::new);

            if (splitByLine.length == 0) // No lines.
                return new String[0][0];

            // Want a mutable list, so the detour off creating arraylists.
            var splitByComma =
                Arrays.stream(splitByLine)
                      .map(s -> s.split(s.indexOf(',') != -1 ? "," : ";"))
                      .map(arr -> {
                          var al = new ArrayList<String>();
                          Collections.addAll(al, arr);
                          return al;
                      }).collect(Collectors.toCollection(ArrayList::new));

            var max =
                splitByComma.stream()
                            .max(Comparator.comparingInt(List::size))
                            .orElse(new ArrayList<>())
                            .size();

            for (var list : splitByComma)
            {
                for (var i = list.size(); i < max; i++)
                    list.add("");
            }

            return splitByComma.stream()
                               .map(l -> l.stream().map(Utils::toNameCase).toList())
                               .map(l -> l.toArray(new String[0]))
                               .toArray(String[][]::new);
        } // Catching IndexOutOfBounds, since if it isn't a CSV file it will most likely be thrown.
        catch (IOException | IndexOutOfBoundsException e)
        {
            DebugMethods.log(e.getLocalizedMessage(), DebugMethods.LogType.ERROR);
            return new String[0][0];
        }
    }
}
