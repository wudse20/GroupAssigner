package se.skorup.API.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCSVParser
{
    public static Stream<Arguments> getTestData()
    {
        var wierdCSV = new String[][] {
            {"Kaka Kaka Kaka Kaka"},
            {"Kaka Kaka Kaka Kaka"},
            {"Kaka Kaka Kaka Kaka"}
        };

        var testData = new String[][] {
            { "Tidstämpel", "Namn" , "Önskning 1", "Önskning 2", "Önskning 3" },
            { "T1", "Agnes", "Emma", "Linnea", "Emma" },
            { "T2", "Alice", "Bella", "Isabelle", "Stella" },
            { "T3", "Allie", "Stella", "Isabelle", "Linnea" },
            { "T4", "Bella", "Alice", "Isabelle", "Stella" },
            { "T5", "Emma", "Linnea", "Emma", "Agnes" },
            { "T6", "Emma", "Agnes", "Emma", "Linnea" },
            { "T7", "Isabelle", "Allie", "Stella", "Alice"},
            { "T8", "Linnea", "Emma", "Agnes", "Emma" },
            { "T9", "Stella", "Isabelle", "Allie", "Linnea" }
        };

        var testData2 = new String[][] {
            { "Tidstämpel", "Namn" , "Önskning 1", "Önskning 2", "Önskning 3" },
            { "T1", "Agnes", "Emma", "Linnea", "Emma" },
            { "T2", "Alice", "Bella", "Isabelle", "" },
            { "T3", "Allie", "Stella", "Isabelle", "Linnea" },
            { "T4", "Bella", "Alice", "Isabelle", "Stella" },
            { "T5", "Emma", "Linnea", "Emma", "Agnes" },
            { "T6", "Emma", "Agnes", "Emma", "Linnea" },
            { "T7", "Isabelle", "Allie", "Stella", "Alice"},
            { "T8", "Linnea", "Emma", "Agnes", "Emma" },
            { "T9", "Stella", "Isabelle", "Allie", "" }
        };

        var testData3 = new String[][] {
            {"Person", "Telefonnummer"},
            {"A", "1"},
            {"B", "2"},
            {"C", "3"},
            {"D", "4"},
            {"E", "5"},
            {"F", "6"}
        };

        return Stream.of(
            Arguments.of(UUID.randomUUID().toString(), new String[0][0], "Non-existent path"),
            Arguments.of("./src/test/testData/empty.csv", new String[0][0], "Empty file"),
            Arguments.of("./src/test/testData/wierd_csv.csv", wierdCSV, "wierd_csv.csv"),
            Arguments.of("./src/test/testData/test_data.csv", testData, "test_data.csv"),
            Arguments.of("./src/test/testData/test_data2.csv", testData2, "test_data2.csv"),
            Arguments.of("./src/test/testData/test_data3.csv", testData2, "test_data3.csv (NameCase)"),
            Arguments.of("./src/test/testData/CSV_SEMI.csv", testData3, "CSV_SEMI.csv (Semicolon)"),
            Arguments.of("./src/test/testData/CSV_SEMI_MIXED.csv", testData3, "CSV_SEMI_MIXED.csv (Semicolon and comma)")
        );
    }

    @ParameterizedTest
    @MethodSource("getTestData")
    public void testCSVWithData(String path, String[][] expected, String message)
    {
        var res = CSVParser.parseCSV(path);
        assertNotNull(res, "The result is null and it's not allowed.");
        assertTrue(
            Arrays.deepEquals(expected, res),
            "Expected: %s, Got: %s, Message: %s".formatted(
                Arrays.deepToString(expected),
                Arrays.deepToString(res),
                message
        ));
    }
}
