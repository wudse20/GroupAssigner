package se.skorup.version.gui;

import se.skorup.API.util.DebugMethods;
import se.skorup.API.util.Utils;
import se.skorup.main.gui.interfaces.ActionCallbackWithParam;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

/**
 * The frame responsible for asking the user if they
 * are okay with automatic version checking.
 * */
public class VersionCheckerTermsFrame extends JFrame
{
    private static final String VERSION_MESSAGE =
        """
        <html>Vill du att programmet ska kolla ifall det har släppts en ny version
        varje gång det startar?

        Det som krävs för att detta skall fungera är en internetuppkippling.
        Det enda som den kommer att göra är att hämta den senasete versionen
        från en webserver. Därefter kollar programmet - lokalt, ifall versionen
        stämmer. Din version skickas aldrig någonstans.

        Skulle de inte stämma överrens så kommer den att fråga ifall du vill
        ladda ner en nyare version.

        Allt den kommer att hämta är innehållet av denna fil:
        %s.
        Namnet är lite tråkig programmerarehumor<html>.
        """.formatted(Utils.VERSION_URL).replace("\n", "<br>");

    private boolean userAccepted = false;

    private final Container cp = this.getContentPane();
    private final JLabel lblText = new JLabel(VERSION_MESSAGE);

    private final JButton btnAccept = new JButton("Ja");
    private final JButton btnDeny = new JButton("NEEEEJ");
    private final JButton btnOpen = new JButton("Öppna filen");

    private final ActionCallbackWithParam<Boolean> callback;

    /**
     * Creates a new VersionCheckerTermsFrame. The passed callback
     * will be executed on {@code dispose();}.
     *
     * @param callback the callback that is called on {@code dispose();}.
     * @see JFrame#dispose() dispose
     * */
    public VersionCheckerTermsFrame(ActionCallbackWithParam<Boolean> callback)
    {
        super("Vill du ha versions koll?");

        this.callback = callback;

        this.setProperties();
        this.addComponents();
        this.pack();
    }

    private void addComponents()
    {
        var btnPanel = new JPanel();
        btnPanel.setBackground(Utils.BACKGROUND_COLOR);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnOpen);
        btnPanel.add(btnDeny);
        btnPanel.add(btnAccept);

        cp.add(new JLabel(" "), BorderLayout.PAGE_START);
        cp.add(new JLabel("    "), BorderLayout.LINE_START);
        cp.add(lblText, BorderLayout.CENTER);
        cp.add(new JLabel("    "), BorderLayout.LINE_END);
        cp.add(btnPanel, BorderLayout.PAGE_END);
    }

    /**
     * Sets the properties.
     * */
    private void setProperties()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        cp.setBackground(Utils.BACKGROUND_COLOR);
        cp.setLayout(new BorderLayout());

        lblText.setForeground(Utils.FOREGROUND_COLOR);
        lblText.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);

        btnAccept.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnAccept.setForeground(Utils.FOREGROUND_COLOR);
        btnAccept.addActionListener(e -> {
            userAccepted = true;
            dispose();
        });

        btnDeny.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnDeny.setForeground(Utils.FOREGROUND_COLOR);
        btnDeny.addActionListener(e -> {
            userAccepted = false;
            dispose();
        });

        btnOpen.setBackground(Utils.COMPONENT_BACKGROUND_COLOR);
        btnOpen.setForeground(Utils.FOREGROUND_COLOR);
        btnOpen.addActionListener(e -> Utils.openWebpage(Utils.VERSION_URL));
    }

    @Override
    public void dispose()
    {
        callback.action(userAccepted);
        super.dispose();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(
            () -> new VersionCheckerTermsFrame(
                b -> DebugMethods.log(b, DebugMethods.LogType.DEBUG)
            )
        );
    }
}
