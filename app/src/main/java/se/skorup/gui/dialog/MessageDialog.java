package se.skorup.gui.dialog;

import se.skorup.gui.components.Button;
import se.skorup.gui.components.Label;
import se.skorup.gui.components.Panel;
import se.skorup.util.localization.Localization;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * A message dialog.
 * */
public final class MessageDialog extends Dialog
{
    private String title;

    private Button btnClose;
    private Label lblMessage;

    /** This class should be instantiated with {@link MessageDialog#show()}. */
    private MessageDialog() {}

    @Override
    protected void setupFrame(DialogFrame frame)
    {
        var bp = new Panel(new FlowLayout(FlowLayout.CENTER));
        bp.add(btnClose);

        var ip = new Panel(new FlowLayout(FlowLayout.LEFT));
        ip.add(new JLabel(informationIcon));
        ip.add(new Label(" ".repeat(3)));
        ip.add(lblMessage);

        var cp = frame.getContentPane();
        frame.setTitle(title);
        cp.add(new Label(" "), BorderLayout.PAGE_START);
        cp.add(new Label("   "), BorderLayout.LINE_START);
        cp.add(ip, BorderLayout.CENTER);
        cp.add(bp, BorderLayout.PAGE_END);

        frame.setSize(
            new Dimension(
                (int) Math.max(title.length() * 15, 3 * lblMessage.getPreferredSize().getWidth()),
                (int) (3 * btnClose.getPreferredSize().getHeight() + 3 * lblMessage.getPreferredSize().getHeight())
        ));
    }

    /**
     * Creates a dialog.
     *
     * @return the next step in creating the dialog.
     * */
    public static TitleStep create()
    {
        return new DialogBuilder();
    }

    /** The title step in building the dialog. */
    public interface TitleStep
    {
        /**
         * Sets a localized title. The title will be the value of
         * the localization key.
         *
         * @param localizationKey the key used for localization.
         * @return the next step in the building chain.
         * */
        InformationStep setLocalizedTitle(String localizationKey);

        /**
         * Sets a title.
         *
         * @param title the title of the frame.
         * @return the next step in the building chain.
         * */
        InformationStep setTitle(String title);
    }

    /** The information step in building the dialog. */
    public interface InformationStep
    {
        /**
         * Sets localized information. The information will be the value of
         * the localization key.
         *
         * @param localizationKey the key used for localization.
         * @return the next step in the chain.
         * */
        ButtonStep setLocalizedInformation(String localizationKey);

        /**
         * Sets the information.
         *
         * @param information the information of the frame.
         * @return the next step in the chain.
         * */
        ButtonStep setInformation(String information);
    }

    /** The button step in building the dialog. */
    public interface ButtonStep
    {
        /**
         * Sets localized button text. The button text will be the value of
         * the localization key.
         *
         * @param localizationKey the key used for localization.
         * @return the MessageDialog.
         * */
        MessageDialog setLocalizedButtonText(String localizationKey);
    }

    /** The class responsible for building dialogs. */
    public static class DialogBuilder implements TitleStep, InformationStep, ButtonStep
    {
        private final MessageDialog dialog;

        /** Shouldn't be instantiated outside this class. */
        private DialogBuilder()
        {
            this.dialog = new MessageDialog();
        }

        @Override
        public InformationStep setLocalizedTitle(String localizationKey)
        {
            dialog.title = Localization.getValue(localizationKey);
            return this;
        }

        @Override
        public InformationStep setTitle(String title)
        {
            dialog.title = title;
            return this;
        }

        @Override
        public ButtonStep setLocalizedInformation(String localizationKey)
        {
            dialog.lblMessage = new Label(localizationKey, true);
            return this;
        }

        @Override
        public ButtonStep setInformation(String information)
        {
            dialog.lblMessage = new Label(information);
            return this;
        }

        @Override
        public MessageDialog setLocalizedButtonText(String localizationKey)
        {
            dialog.btnClose = new Button(localizationKey);
            dialog.btnClose.addActionListener(e -> dialog.close());
            return dialog;
        }
    }
}
