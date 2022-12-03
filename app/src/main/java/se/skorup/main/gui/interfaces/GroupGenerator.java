package se.skorup.main.gui.interfaces;

import se.skorup.main.groups.exceptions.GroupCreationFailedException;

import java.util.List;
import java.util.Set;

/**
 * The interface used as a group generator.
 * */
@FunctionalInterface
public interface GroupGenerator
{
    /**
     * The generator method.
     *
     * @return the generated method.
     * @throws GroupCreationFailedException iff the group cannot be created.
     */
    List<List<Set<Integer>>> generate() throws GroupCreationFailedException;
}
