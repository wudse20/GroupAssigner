package se.skorup.util.localization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;

public class TestLocalization
{
    public static final Map<String, String> SV_SE = Map.of(
        "ui.button.save", "Spara",
        "ui.button.exit", "Avsluta",
        "ui.button.kaka", "Kaka: ",
        "ui.button.krokodil", "Krokodil:"
    );

    public static Stream<Arguments> getLocalizationData()
    {
        var resourceDirectory = Paths.get("src", "test", "resources");
        var SV_se_PATH = resourceDirectory.toFile().getAbsolutePath() + "/SV_se.lang";
        var SV_se_hard_PATH = resourceDirectory.toFile().getAbsolutePath() + "/SV_se_hard.lang";

        return Stream.of(
            Arguments.of(SV_SE, SV_se_PATH),
            Arguments.of(SV_SE, SV_se_hard_PATH)
        );
    }

    public static Stream<Arguments> getKeyValueData() throws IOException
    {
        var resourceDirectory = Paths.get("src", "test", "resources");
        var SV_se_PATH = resourceDirectory.toFile().getAbsolutePath() + "/SV_se.lang";
        Localization.parseLanguageFile(SV_se_PATH);

        return Stream.of(
            Arguments.of(new ArrayList<>(SV_SE.keySet()), new ArrayList<>(SV_SE.values()), new HashSet<>()),
            Arguments.of(
                List.of("kaka.kaka", "krokodil.kaka", "ui.button.save"),
                List.of("kaka.kaka", "krokodil.kaka", SV_SE.get("ui.button.save")),
                Set.of("kaka.kaka", "krokodil.kaka")
            ),
            Arguments.of(new ArrayList<>(SV_SE.keySet()), new ArrayList<>(SV_SE.values()), new HashSet<>()) // Twice just to check that the test code is working with clear.
        );
    }

    public static Stream<Arguments> testGetMapData()
    {
        var resourceDirectory = Paths.get("src", "test", "resources");
        var SV_se_PATH = resourceDirectory.toFile().getAbsolutePath() + "/SV_se.lang";
        var SV_se_hard_PATH = resourceDirectory.toFile().getAbsolutePath() + "/SV_se_hard.lang";

        return Stream.of(
            Arguments.of(SV_SE, SV_se_PATH),
            Arguments.of(SV_SE, SV_se_hard_PATH)
        );
    }

    @ParameterizedTest
    @MethodSource("getLocalizationData")
    public void testParseLocalization(Map<String, String> values, String path) throws IOException
    {
        Localization.missing.clear();
        var res = Localization.parseLanguageFile(path);
        assertEquals(values, res, "The parsed values don't match the expected.");
    }

    @ParameterizedTest
    @MethodSource("getKeyValueData")
    public void testGetValue(List<String> keys, List<String> expectedValues, Set<String> expectedMissing)
    {
        Localization.missing.clear();

        if (keys.size() != expectedValues.size())
            fail("Buggy test code!");

        for (var i = 0; i < keys.size(); i++)
            assertEquals(
                expectedValues.get(i),
                Localization.getValue(keys.get(i)),
                "Key and value pair wrong :("
            );

        assertEquals(expectedMissing, Localization.missing, "The expected missing elements did not match.");
    }

    @ParameterizedTest
    @MethodSource("testGetMapData")
    public void testGetMap(Map<String, String> expected, String path) throws IOException
    {
        Localization.missing.clear();
        Localization.parseLanguageFile(path);
        assertNotSame(expected, Localization.getLanguageMap(), "The maps should not be the same instance :(");
        assertEquals(expected, Localization.getLanguageMap(), "The maps should have the same content :(");
    }
}
