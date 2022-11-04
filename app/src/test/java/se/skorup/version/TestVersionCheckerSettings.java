package se.skorup.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestVersionCheckerSettings
{
    private static final long EXPECTED = 2797690461233526703L;

    @Test
    public void testSerialUUID()
    {
        assertEquals(EXPECTED, VersionCheckerSettings.serialVersionUID);
    }
}
