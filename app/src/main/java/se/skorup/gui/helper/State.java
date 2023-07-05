package se.skorup.gui.helper;

import java.awt.Color;

import static se.skorup.gui.components.CSVLabel.PERSON_COLOR;
import static se.skorup.gui.components.CSVLabel.SKIP_COLOR;
import static se.skorup.gui.components.CSVLabel.UNSELECTED_COLOR;
import static se.skorup.gui.components.CSVLabel.WISH_COLOR;

/**
 * The enum for the state of the
 * CSVLabels.
 * */
public enum State
{
    PERSON(PERSON_COLOR),
    WISH(WISH_COLOR),
    SKIP(SKIP_COLOR),
    UNSELECTED(UNSELECTED_COLOR);

    public final Color color;

    State(Color color)
    {
        this.color = color;
    }
}
