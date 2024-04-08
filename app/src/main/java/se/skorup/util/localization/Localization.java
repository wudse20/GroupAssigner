package se.skorup.util.localization;

import se.skorup.util.Log;
import se.skorup.util.io.MyFileReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class used for localization of the program.
 * */
public class Localization
{
    /** The set of the missed lookups in the localization table. */
    protected static final Set<String> missing;

    /** Protected for testing purposes. */
    protected static Map<String, String> map;

    static {
        map = new HashMap<>();
        missing = ConcurrentHashMap.newKeySet();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!missing.isEmpty())
            {
                var sb = new StringBuilder("Unmatched localizations: ");

                for (var str : missing)
                    sb.append(str).append(", ");

                sb.delete(sb.length() - 2, sb.length());

                Log.debug(sb);
            }
        }));
    }

    /** You should never be able to instantiate this class. */
    private Localization() {}

    /**
     * Gets the value of a key.
     *
     * @param key the key that you want the value for.
     * @return the keys corresponding value iff it exists,
     *         else it will just return the key.
     * */
    public static String getValue(String key)
    {
        if (!map.containsKey(key))
            missing.add(key);

        return map.getOrDefault(key, key);
    }

    /**
     * Gets the value of a key and applies a {@link String#format format}
     * if there exists a <i>'%'</i> in the string else it will just be
     * the same as {@link Localization#getValue(String) getValue}.
     *
     * @param key the key that you want the value for.
     * @param args the args to the format string.
     * @return the keys corresponding value iff it exists,
     *         else it will just return the key.
     *         If possible, it will apply a format string, else it
     *         will just return the value.
     * @see String#format
     * @see Localization#getValue(String)
     * */
    public static String getValuef(String key, Object... args)
    {
        var str = getValue(key);
        return str.contains("%") ? str.formatted(args) : str;
    }

    /**
     * Gets a copy of the language map.
     *
     * @return a copy of the language map.
     * */
    public static Map<String, String> getLanguageMap()
    {
        return new HashMap<>(map);
    }

    /**
     * Parses and sets a language file to the current lang file that will be used.
     *
     * @param path the path of the lang file.
     * @return a Map with the key-value pairs from the language file.
     * @throws IOException iff IO-opeartion fail.
     * */
    public static Map<String, String> parseLanguageFile(String path) throws IOException
    {
        var file = MyFileReader.readFile(new File(path));
        var lines = file.split("\n");
        return parseLanguageFile(lines);
    }

    /**
     * Parses and sets a language file to the current lang file that will be used.
     *
     * @param lines the lines of the lang file.
     * @return a Map with the key-value pairs from the language file.
     * */
    public static Map<String, String> parseLanguageFile(String[] lines)
    {
        var map = new HashMap<String, String>();

        for (var l : lines)
        {
            if (l.trim().isEmpty())
                continue;

            if (l.trim().length() > 2 && l.charAt(0) == '/' && l.charAt(1) == '/')
                continue;

            var keySb = new StringBuilder();
            var valueSb = new StringBuilder();
            var i = 0;
            for (; i < l.length(); i++)
            {
                var c = l.toCharArray()[i];
                if (c == ':')
                    break;

                keySb.append(c);
            }

            var started = false;
            for (; i < l.length(); i++)
            {
                var c = l.toCharArray()[i];

                if (!started && c == '\"')
                {
                    started = true;
                    continue;
                }

                if (!started)
                    continue;

                if (c == '\"')
                    break;

                valueSb.append(c);
            }

            if (valueSb.toString().trim().isEmpty())
                continue;

            map.put(keySb.toString().trim(), valueSb.toString());
        }

        Localization.map = map;

        return map;
    }
}
