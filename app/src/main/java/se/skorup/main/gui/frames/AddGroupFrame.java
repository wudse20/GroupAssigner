package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.main.manager.Group;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

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
    protected Group groupAction(Group gm)
    {
        var res = new GroupManager(pName.getText());

        nameModel.getItems().forEach(x -> res.registerPerson(x, Person.Role.CANDIDATE));
        DebugMethods.log("Created: %s".formatted(res), DebugMethods.LogType.DEBUG);

        return res;
    }
}
