package se.skorup.main.gui.objects;

import java.awt.Color;

/**
 * The text box used to draw a person.
 * */
public class PersonBox extends TextBox
{
    /** The id of the person. */
    private final int id;

    /**
     * Creates a new TextBox.
     *
     * @param text the text of the text box.
     * @param x the x-position.
     * @param y the y-position.
     * @param c the default color of the TextBox.
     * @param id the id of the person.
     * */
    public PersonBox(String text, int x, int y, Color c, int id)
    {
        super(text, x, y, c);
        this.id = id;
    }

    /**
     * Getter for: id.
     *
     * @return the id of the person.
     * */
    public int getId()
    {
        return id;
    }
}
