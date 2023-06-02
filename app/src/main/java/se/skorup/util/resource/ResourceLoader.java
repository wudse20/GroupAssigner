package se.skorup.util.resource;

import se.skorup.util.Log;
import se.skorup.util.io.MyFileReader;
import se.skorup.util.localization.Localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class which is responsible for loading all the resources from the
 * jar-file. If it is in dev mode - i.e. no jar, it will read files from
 * the resource folder.
 * */
public class ResourceLoader
{
    private static final String BASE_DEV_PATH = "/src/resources/";
    private static final String BASE_LANG_PATH = "lang/";

    private String languageFile;

    /** No one should ever directly instantiate this class. */
    private ResourceLoader() {}

    /**
     * Gets the lines of a file.
     *
     * @param file the path of the file.
     * @return the lines of a file at location 'file'
     * @throws IOException if file reading fails.
     * */
    private String[] getLines(String file) throws IOException
    {
        var is = ResourceLoader.class.getClassLoader().getResourceAsStream(file);
        Log.debugf("file: %s", file);
        Log.debug(is == null);

        if (is != null)
        {
            var br = new BufferedReader(new InputStreamReader(is));
            var lines = new ArrayList<String>();
            var tmp = "";
            while ((tmp = br.readLine()) != null)
                lines.add(tmp);

            is.close();
            br.close();

            return lines.toArray(new String[0]);
        }
        else
        {
            return MyFileReader.readFile(new File(BASE_DEV_PATH + file)).split("\n");
        }
    }

    /**
     * Loads the language
     *
     * @throws IOException if file reading fails.
     * */
    private void loadLanguage() throws IOException
    {
        var lines = getLines("%s%s".formatted(BASE_LANG_PATH, languageFile));
        Localization.parseLanguageFile(lines);
    }

    /**
     * Loads all the resources from the jar file
     * into the program.
     *
     * @throws IOException if file reading fails.
     * */
    public void loadResources() throws IOException
    {
        Log.debug("Starting to load resources");
        Log.debugf("Starting to load language: %s", languageFile);
        loadLanguage();
        Log.debugf("Successfully loaded the language: %s", languageFile);
        Log.debug("Finished loading resources.");
    }

    /**
     * Gets the builder for the resource
     * loader.
     * */
    public static LanguageStep getBuilder()
    {
        return new ResourceBuilder();
    }


    public interface LanguageStep
    {
        /**
         * Initializes the resource loader with the language
         * file of choice.
         *
         * @param langFileName The name of the language file.
         * @return the resource builder that builds the resource loader.
         * */
        FinalStep initLangFile(String langFileName);
    }

    public interface FinalStep
    {
        /**
         * Builds the resource loader.
         *
         * @return the build resource loader.
         * */
        ResourceLoader build();
    }

    public static class ResourceBuilder implements LanguageStep, FinalStep
    {
        private final ResourceLoader loader;

        private ResourceBuilder()
        {
            this.loader = new ResourceLoader();
        }

        @Override
        public ResourceLoader build()
        {
            return loader;
        }

        @Override
        public FinalStep initLangFile(String langFileName)
        {
            loader.languageFile = langFileName;
            return this;
        }
    }
}
