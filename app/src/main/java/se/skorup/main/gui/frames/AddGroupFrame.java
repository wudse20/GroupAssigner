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

    /**
     * Imports data and creates a group manager from the docs.
     * */
    private void importFromDocs()
    {
        var fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(true);
        var selection = fc.showDialog(this, "Välj");

        if (selection == JFileChooser.APPROVE_OPTION)
        {
            var sb = new StringBuilder();
            for (var f : fc.getSelectedFiles())
                sb.append(f.getName()).append(" + ");

            var result = new GroupManager(sb.substring(0, sb.length() - 3));
            try
            {
                for (var str : MyFileReader.readFiles(fc.getSelectedFiles()))
                    FormsParser.parseFormData(str, result);

                this.setResult(result);
            }
            catch (IOException e)
            {
                DebugMethods.log(e, DebugMethods.LogType.ERROR);
                JOptionPane.showMessageDialog(
                    this,
                    "Kunde inte läsa fil!\nFelmeddelande%s".formatted(e.getLocalizedMessage()),
                    "Kunde inte läsa fil", JOptionPane.ERROR_MESSAGE
                );
            }

            invokeAddListeners();
        }
    }

    @Override
    protected GroupManager groupAction(GroupManager gm)
    {
        var res = new GroupManager(pName.getText());

        nameModel.getItems().forEach(x -> res.registerPerson(x, Person.Role.CANDIDATE));
        DebugMethods.log("Created: %s".formatted(res), DebugMethods.LogType.DEBUG);

        return res;
    }

    @Override
    protected void setProperties()
    {
        super.setProperties();

        btnImport.setForeground(Utils.FOREGROUND_COLOR);
        btnImport.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnImport.addActionListener((e) -> importFromDocs());
    }
}
