package se.skorup.main.gui.frames;

import se.skorup.API.util.DebugMethods;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

import java.util.HashSet;

/**
 * Used for edting group managers.
 * */
public final class EditGroupFrame extends AbstractGroupFrame
{
    /**
     * Creates a new EditGroupFrame
     *
     * @param gm the current GroupManager.
     * */
    public EditGroupFrame(GroupManager gm)
    {
        super("Uppdatera en grupp", gm, false);

        gm.getAllPersons()
                .stream()
                .map(Person::getName)
                .forEach(nameModel::addItem);

        pName.setText(gm.getName());
        btnApply.setText("Uppdatera gruppen");
    }


    @Override
    protected GroupManager groupAction(GroupManager gm)
    {
        gm.setName(pName.getText());
        var names = new HashSet<>(gm.getNames());

        for (var n : nameModel.getItems())
        {
            if (!names.contains(n))
                gm.registerPerson(n, Person.Role.CANDIDATE);
        }

        for (var n : removed)
            for (var p : gm.getPersonFromName(n))
                gm.removePerson(p.getId());

        return gm;
    }

    @Override
    protected void removeAction()
    {
        var index = names.getSelectedIndex();
        var elem = nameModel.getElementAt(index);

        DebugMethods.log(
            "%s was removed at index %d.".formatted(elem, index),
            DebugMethods.LogType.DEBUG
        );

        removed.add(elem);
        super.removeAction();
    }
}
