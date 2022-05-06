package se.skorup.main.gui.helper;

import java.awt.LayoutManager;

/**
 * The interface that is used to generate the different
 * layouts for the SubgroupDisplayPanel.
 * */
@FunctionalInterface
public interface LayoutGenerator
{
    /**
     * Generates a layout.
     *
     * @param nbrGroups The number of groups.
     * */
    LayoutManager generateLayout(int nbrGroups);
}
