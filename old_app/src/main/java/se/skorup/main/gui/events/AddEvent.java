package se.skorup.main.gui.events;

import se.skorup.main.gui.frames.AbstractGroupFrame;
import se.skorup.main.manager.Group;

/**
 * The event invoked when a
 * group has been added.
 *
 * @param frame the instance of the frame.
 * @param result the result of the added group.
 * */
public record AddEvent(AbstractGroupFrame frame, Group result) {}
