package se.skorup.gui.dialog;

import se.skorup.gui.components.buttons.Button;
import se.skorup.gui.components.containers.ComponentContainer;
import se.skorup.gui.components.output.Label;
import se.skorup.gui.components.containers.Panel;
import se.skorup.util.localization.Localization;

import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public final class ConfirmDialog extends Dialog<Boolean>
{
    private String title;
    private Label lblQuestion;
    private Button btnApprove;
    private Button btnDisapprove;

    /** This should never be directly instantiated outside this class. */
    private ConfirmDialog() {}

    @Override
    protected void setupFrame(DialogFrame frame)
    {
        var bp = new Panel(new FlowLayout(FlowLayout.CENTER));
        bp.add(btnApprove);
        bp.add(btnDisapprove);

        var ip = new Panel(new FlowLayout(FlowLayout.LEFT));
        ip.add(new JLabel(getIcon()));
        ip.add(new ComponentContainer(lblQuestion));

        var cont = new Panel(new BorderLayout());
        cont.add(new Label(" "), BorderLayout.PAGE_START);
        cont.add(new Label("   "), BorderLayout.LINE_START);
        cont.add(new ComponentContainer(ip), BorderLayout.CENTER);
        cont.add(bp, BorderLayout.PAGE_END);

        var cp = frame.getContentPane();
        cp.add(cont, BorderLayout.CENTER);

        frame.setTitle(title);
        frame.pack();
    }

    /**
     * Starts the building process of this dialog.
     *
     * @return the next step in the building process.
     * */
    public static QuestionStep create()
    {
        return new Builder();
    }

    /** The step in the building process, where the question is set. */
    public interface QuestionStep
    {
        /**
         * Sets the question of the dialog.
         *
         * @param question the question to be set.
         * @return the next step in the building process.
         * */
        ApproveButtonStep setQuestion(String question);


        /**
         * Sets the question of the dialog using a localization key.
         *
         * @param localizationKey the localization key of the question to be set.
         * @return the next step in the building process.
         * */
        ApproveButtonStep setLocalizedQuestion(String localizationKey);

        /**
         * Sets the question of the dialog using a localization key with formatting.
         *
         * @param localizationKey the localization key of the question to be set.
         * @param args the args for formatting.
         * @return the next step in the building process.
         * */
        ApproveButtonStep setLocalizedQuestionf(String localizationKey, Object... args);
    }

    /** The step in the building process, where the approve-button text is set.*/
    public interface ApproveButtonStep
    {
        /**
         * Sets the approve-button text using a localization key.
         *
         * @param localizationKey the key used to determine the text of the button.
         * @return the next step in the building process.
         * */
        DisproveButtonStep setLocalizedApproveButtonText(String localizationKey);
    }

    /** The step in the building process, where the disapprove-button text is set.*/
    public interface DisproveButtonStep
    {
        /**
         * Sets the disapprove-button text using a localization key.
         *
         * @param localizationKey the key used to determine the text of the button.
         * @return the next step in the building process.
         * */
        TitleStep setLocalizedDisapproveButtonText(String localizationKey);
    }

    /** The step in the building process, where the title is set.*/
    public interface TitleStep
    {
        /**
         * Sets the title of the dialog.
         *
         * @param title the title that is supposed to be on the dialog.
         * @return the finished confirm-dialog.
         * */
        ConfirmDialog setTitle(String title);

        /**
         * Sets the title of the dialog using a localization key.
         *
         * @param localizationKey the key of the title that is supposed to be on the dialog.
         * @return the finished confirm-dialog.
         * */
        ConfirmDialog setLocalizedTitle(String localizationKey);

        /**
         * Sets the title of the dialog using a localization key,
         * with formatting.
         *
         * @param localizationKey the key of the title that is supposed to be on the dialog.
         *                        If the key doesn't contain any format specifiers then it will
         *                        just treat the key as a normal localization key.
         * @param args the args to the format string.
         * @return the finished confirm-dialog.
         * */
        ConfirmDialog setLocalizedTitlef(String localizationKey, Object... args);
    }

    /** The class responsible for building the dialog. */
    public static class Builder implements QuestionStep, ApproveButtonStep, DisproveButtonStep, TitleStep
    {
        private ConfirmDialog dialog;

        /** This shouldn't be directly instantiated. */
        private Builder()
        {
            this.dialog = new ConfirmDialog();
        }

        @Override
        public ApproveButtonStep setQuestion(String question)
        {
            dialog.lblQuestion = new Label(question);
            return this;
        }

        @Override
        public ApproveButtonStep setLocalizedQuestion(String localizationKey)
        {
            dialog.lblQuestion = new Label(localizationKey, true);
            return this;
        }

        @Override
        public ApproveButtonStep setLocalizedQuestionf(String localizationKey, Object... args)
        {
            dialog.lblQuestion = new Label(Localization.getValuef(localizationKey, args));
            return this;
        }

        @Override
        public DisproveButtonStep setLocalizedApproveButtonText(String localizationKey)
        {
            dialog.btnApprove = new Button(localizationKey);
            dialog.btnApprove.addActionListener(e -> {
                dialog.result = true;
                dialog.close();
            });
            return this;
        }

        @Override
        public TitleStep setLocalizedDisapproveButtonText(String localizationKey)
        {
            dialog.btnDisapprove = new Button(localizationKey);
            dialog.btnDisapprove.addActionListener(e -> {
                dialog.result = false;
                dialog.close();
            });
            return this;
        }

        @Override
        public ConfirmDialog setTitle(String title)
        {
            dialog.title = title;
            return dialog;
        }

        @Override
        public ConfirmDialog setLocalizedTitle(String localizationKey)
        {
            dialog.title = Localization.getValue(localizationKey);
            return dialog;
        }

        @Override
        public ConfirmDialog setLocalizedTitlef(String localizationKey, Object... args) {
            dialog.title = Localization.getValuef(localizationKey, args);
            return dialog;
        }
    }
}
