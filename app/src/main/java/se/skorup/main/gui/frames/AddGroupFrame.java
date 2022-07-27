package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.FormsParser;
import se.skorup.API.util.MyFileReader;
import se.skorup.API.util.Utils;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.IOException;

/**
 * The frame used to add groups.
 * */
public final class AddGroupFrame extends AbstractGroupFrame
{
    /**
     * Creates and shows a new AddGroupFrame.
     * */
    public AddGroupFrame()
    {
        super("Skapa en grupp!");
    }

    @Override
    protected GroupManager groupAction(GroupManager gm)
    {
        var res = new GroupManager(pName.getText());

        nameModel.getItems().forEach(x -> res.registerPerson(x, Person.Role.CANDIDATE));
        DebugMethods.log("Created: %s".formatted(res), DebugMethods.LogType.DEBUG);

        return res;
    }
}
