package se.skorup.util.io;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCSVReader
{
    public static Stream<Arguments> getCSVData()
    {
        var resourceDirectory = Paths.get("src", "test", "resources");
        var test1 = resourceDirectory.toFile().getAbsolutePath() + "/test.csv";
        var test2 = resourceDirectory.toFile().getAbsolutePath() + "/test2.csv";

        var expected = new String[][] {
            { "Anton", "Minna", "Victoria", "Felix" },
            { "Minna", "Victoria", "Felix" },
            { "Felix", "Minna", "Victoria", "Anton" },
            { "Stina", "Klas", "Nils", "Anton" },
            { "Klas", "Minna", "Nils" },
            { "Nils", "Klas", "Anton" }
        };

        return Stream.of(
            Arguments.of(test1, expected),
            Arguments.of(test2, expected)
        );
    }

    @ParameterizedTest
    @MethodSource("getCSVData")
    public void testCSV(String path, String[][] expected)
    {
        var read = new AtomicReferenceArray<String[]>(expected.length);
        assertDoesNotThrow(() -> {
            var res = CSVReader.readCSV(path);

            for (var i = 0; i < res.length; i++)
                read.set(i, res[i]);
        });

        var res = new String[expected.length][];
        for (var i = 0; i < res.length; i++)
            res[i] = read.get(i);

        assertTrue(Arrays.deepEquals(expected, res), "The resulting array should equal the expected.");
    }
}
