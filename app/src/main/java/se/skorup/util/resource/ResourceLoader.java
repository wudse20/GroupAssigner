package se.skorup.util.resource;

import se.skorup.gui.dialog.Dialog;
import se.skorup.util.Log;
import se.skorup.util.io.MyFileReader;
import se.skorup.util.localization.Localization;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private static final String BASE_ICON_PATH = "icons/";

    private String languageFile;
    private String iconFile;

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
            var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
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
     * Reads an image and returns it.
     *
     * @param file the path to the file.
     * @throws IOException if file reading fails.
     * */
    private BufferedImage getImage(String file) throws IOException
    {
        var io = ResourceLoader.class.getClassLoader().getResourceAsStream(file);

        BufferedImage res;
        if (io != null)
        {
            res = ImageIO.read(io);
            io.close();
        }
        else
        {
            var bis = new BufferedInputStream(new FileInputStream(BASE_DEV_PATH + file));
            res = ImageIO.read(bis);
            bis.close();
        }

        return res;
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
     * Loads the program's icons.
     *
     * @throws IOException if file reading fails.
     * */
    private void loadIcons() throws IOException
    {
        var buffImg = getImage(BASE_ICON_PATH + iconFile).getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var img = new ImageIcon(buffImg);
        Dialog.loadIcon(img);
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
        Log.debugf("Starting to load icons");
        loadIcons();
        Log.debugf("Successfully loaded the icons.");
        Log.debug("Finished loading resources.");
    }

    /**
     * Gets the builder for the resource loader.
     * */
    public static LanguageStep getBuilder()
    {
        return new ResourceBuilder();
    }

    /** The step where you specify which lang-file to use. */
    public interface LanguageStep
    {
        /**
         * Initializes the resource loader with the language
         * file of choice.
         *
         * @param langFileName The name of the language file.
         * @return the next step in the chain.
         * */
        IconStep initLangFile(String langFileName);
    }

    public interface IconStep
    {
        /**
         * Loads the icons into the program.
         *
         * @return loads all the icons.
         * */
        FinalStep loadIcons();
    }

    /** The final step in building the resources. */
    public interface FinalStep
    {
        /**
         * Builds the resource loader.
         *
         * @return the build resource loader.
         * */
        ResourceLoader build();
    }

    /**
     * The builder class used to build the resource loader.
     * */
    public static class ResourceBuilder implements LanguageStep, FinalStep, IconStep
    {
        private final ResourceLoader loader;

        /** Should never be instantiated by another class. */
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
        public IconStep initLangFile(String langFileName)
        {
            loader.languageFile = langFileName;
            return this;
        }

        @Override
        public FinalStep loadIcons()
        {
            loader.iconFile = "information.png";
            return this;
        }
    }
}
