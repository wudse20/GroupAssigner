package se.skorup.util.resource;

import se.skorup.gui.components.containers.Frame;
import se.skorup.gui.dialog.Dialog;
import se.skorup.main.gui.about.panels.AttributionPanel;
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

import static se.skorup.main.gui.about.panels.AttributionPanel.IconInfo;

/**
 * A class which is responsible for loading all the resources from the
 * jar-file. If it is in dev mode - i.e., no jar, it will read files from
 * the resource folder.
 * */
public class ResourceLoader
{
    private static final String BASE_DEV_PATH = "/src/resources/";
    private static final String BASE_LANG_PATH = "lang/";
    private static final String BASE_ICON_PATH = "icons/";

    private String languageFile;
    private String informationIconFile;
    private String warningIconFile;
    private String errorIconFile;
    private String folderIconFile;
    private String fileIconFile;
    private String returnIconFile;
    private String homeIconFile;
    private String defaultIconFile;

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

        BufferedImage res = null;
        if (io != null)
        {
            res = ImageIO.read(io);
            io.close();
        }

        if (io == null || res == null)
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
        var buffImgInfo = getImage(BASE_ICON_PATH + informationIconFile)
            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var info = new ImageIcon(buffImgInfo);

        var buffImgWarn = getImage(BASE_ICON_PATH + warningIconFile)
            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var warn = new ImageIcon(buffImgWarn);

        var buffImgErr = getImage(BASE_ICON_PATH + errorIconFile)
            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var err = new ImageIcon(buffImgErr);

        Dialog.loadTypeIcons(info, warn, err);

        var buffImgFolder = getImage(BASE_ICON_PATH + folderIconFile)
            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var folder = new ImageIcon(buffImgFolder);

        var buffImgFile = getImage(BASE_ICON_PATH + fileIconFile)
            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var file = new ImageIcon(buffImgFile);

        var buffImgRet = getImage(BASE_ICON_PATH + returnIconFile);
        var ret = new ImageIcon(buffImgRet.getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        var retAttr = new ImageIcon(buffImgRet.getScaledInstance(32, 32,  Image.SCALE_SMOOTH));

        var buffImgHome = getImage(BASE_ICON_PATH + homeIconFile);
        var home = new ImageIcon(buffImgHome.getScaledInstance(14, 14, Image.SCALE_SMOOTH));
        var homeAttr = new ImageIcon(buffImgHome.getScaledInstance(32, 32, Image.SCALE_SMOOTH));

        Dialog.loadFileIcons(folder, file, ret, home);

        var buffImgDefault = getImage(BASE_ICON_PATH + defaultIconFile);
        var defaultIcon = new ImageIcon(buffImgDefault);

        Frame.setDefaultIcon(defaultIcon);

        AttributionPanel.loadAttributionIcons(
            new IconInfo(info, "ui.info.attribution.info"),
            new IconInfo(warn, "ui.info.attribution.warn"),
            new IconInfo(err, "ui.info.attribution.error"),
            new IconInfo(folder, "ui.info.attribution.folder"),
            new IconInfo(file, "ui.info.attribution.file"),
            new IconInfo(retAttr, "ui.info.attribution.back"),
            new IconInfo(homeAttr, "ui.info.attribution.home")
        );
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
     *
     * @return the builder that builds the resource loader.
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
            loader.informationIconFile = "information.png";
            loader.warningIconFile = "warning.png";
            loader.errorIconFile = "error.png";
            loader.folderIconFile = "folder.png";
            loader.fileIconFile = "files.png";
            loader.returnIconFile = "return.png";
            loader.homeIconFile = "home.png";
            loader.defaultIconFile = "GA.png";
            return this;
        }
    }
}
