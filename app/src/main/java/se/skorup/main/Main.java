package se.skorup.main;

import se.skorup.util.Log;
import se.skorup.util.localization.Localization;
import se.skorup.util.resource.ResourceLoader;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        loadResources();
        Log.debugf("Localization: %s", Localization.getLanguageMap());
        System.out.println("Hello, world!");
    }

    private static void loadResources()
    {
        var rl = ResourceLoader.getBuilder()
                               .initLangFile("SV_se.lang")
                               .build();

        try
        {
            rl.loadResources();
        }
        catch (IOException e)
        {
            Log.error(e);
        }
    }
}
