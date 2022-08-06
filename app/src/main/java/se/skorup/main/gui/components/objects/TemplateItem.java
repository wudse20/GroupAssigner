package se.skorup.main.gui.components.objects;

import se.skorup.main.gui.components.enums.State;

/**
 * A template item.
 *
 * @param state the state used with the template item.
 * @param x the x-coord of the template item.
 * */
public record TemplateItem(State state, int x) {}
