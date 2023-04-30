package se.skorup.version;

import java.io.Serializable;

/**
 * The Settings for the version checker.
 *
 * @param shouldCheck if {@code true} it will check,
 *                    else if {@code false} it won't check.
 * */
public record VersionCheckerSettings(boolean shouldCheck) implements Serializable
{
    static final long serialVersionUID = 2797690461233526703L;
}
