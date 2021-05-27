package se.skorup.main.gui.events;

import se.skorup.main.gui.frames.AddGroupFrame;
import se.skorup.main.manager.GroupManager;

/**
 * The event invoked when a
 * group has been added.
 *
 * @param frame the instance of the frame.
 * @param result the result of the added group.
 * */
public record AddEvent(AddGroupFrame frame, GroupManager result) {}
