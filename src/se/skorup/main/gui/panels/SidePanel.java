package se.skorup.main.gui.panels;

import se.skorup.API.DebugMethods;
import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashSet;

/**
 * The side panel to the left of the GUI in
 * the MainFrame.
 * */
public class SidePanel extends JPanel implements ComponentListener, WindowStateListener
{
    /** The instance of the MainFrame. */
    private final MainFrame mf;

    /** The layout of the frame. */
    private final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

    /** The PersonPanel for the leaders. */
    private final PersonListPanel pLeaders;

    /** The PersonPanel for the candidates. */
    private final PersonListPanel pCandidates;

    /** The spacer between the panels. */
    private final JLabel lblSpacer1 = new JLabel(" ");

    /** The spacer between the panels. */
    private final JLabel lblSpacer2 = new JLabel(" ");

    /**
     * Creates a new SidePane.
     *
     * @param mf the instance of the MainFrame in use.
     * */
    public SidePanel(MainFrame mf)
    {
        this.mf = mf;

        if (mf.getCurrentGroup() != null)
        {
            this.pLeaders =
                new PersonListPanel("Ledare:", mf.getCurrentGroup().getAllOfRoll(Person.Role.LEADER));
            this.pCandidates =
                new PersonListPanel("Deltagare:", mf.getCurrentGroup().getAllOfRoll(Person.Role.CANDIDATE));
        }
        else
        {
            this.pLeaders = new PersonListPanel("Ledare:", new HashSet<>());
            this.pCandidates = new PersonListPanel("Deltagare:", new HashSet<>());
        }

        this.setProperties();
        this.addComponents();
    }

    /**
     * Adds the components to the panel.
     * */
    private void addComponents()
    {
        this.add(lblSpacer1);
        this.add(pLeaders);
        this.add(lblSpacer2);
        this.add(pCandidates);
    }

    /**
     * Sets the properties of the panel.
     * */
    private void setProperties()
    {
        this.setBackground(Utils.BACKGROUND_COLOR);
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(mf.getWidth() / 5, this.getHeight()));

        pLeaders.addActionCallback(pCandidates::deselectAll);
        pLeaders.addActionCallback(this::callback);

        pCandidates.addActionCallback(pLeaders::deselectAll);
        pCandidates.addActionCallback(this::callback);

        mf.addComponentListener(this);
    }

    /**
     * The callback called when a Candidate is
     * selected.
     * */
    private void callback()
    {
        var p = getCurrentPerson();
        DebugMethods.log("Showing %s.".formatted(p), DebugMethods.LogType.DEBUG);
        mf.updatePerson(p);
    }

    /**
     * Gets the currently selected person.
     *
     * @return the currently selected person;
     *         {@code null} iff there is no
     *         person selected.
     * */
    public Person getCurrentPerson()
    {
        return (pCandidates.getCurrentPerson() == null) ?
                pLeaders.getCurrentPerson() :
                pCandidates.getCurrentPerson();
    }

    /**
     * Refreshes the lists.
     * */
    public void refreshLists()
    {
        var gm = mf.getCurrentGroup();

        pLeaders.updateList(gm.getAllOfRoll(Person.Role.LEADER));
        pCandidates.updateList(gm.getAllOfRoll(Person.Role.CANDIDATE));
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        this.setPreferredSize(new Dimension(mf.getWidth() / 5, this.getHeight()));
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void windowStateChanged(WindowEvent e)
    {
        this.setPreferredSize(new Dimension(mf.getWidth() / 5, this.getHeight()));
    }
}
