package se.skorup.main.gui.panels;

import se.skorup.API.Utils;
import se.skorup.main.gui.frames.MainFrame;
import se.skorup.main.objects.Person;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * The side panel to the left of the GUI in
 * the MainFrame.
 * */
public class SidePanel extends JPanel implements ComponentListener
{
    /** The instance of the MainFrame. */
    private final MainFrame mf;

    /** The layout of the frame. */
    private final BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

    /** The PersonPanel for the leaders. */
    private final PersonPanel pLeaders;

    /** The PersonPanel for the candidates. */
    private final PersonPanel pCandidates;

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
        this.pLeaders =
            new PersonPanel("Ledare:", mf.getCurrentGroup().getAllOfRoll(Person.Role.LEADER));
        this.pCandidates =
            new PersonPanel("Deltagare:", mf.getCurrentGroup().getAllOfRoll(Person.Role.CANDIDATE));

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

        mf.addComponentListener(this);
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
}
