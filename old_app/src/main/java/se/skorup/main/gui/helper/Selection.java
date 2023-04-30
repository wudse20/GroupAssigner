package se.skorup.main.gui.helper;

/**
 * A selection in the SubgroupDisplayPanel.
 * */
public record Selection(int id, int group)
{
    /**
     * Checks if the selection is empty.
     *
     * @return {@code true} iff this.equals(empty) is {@code true},
     *         else it will return {@code false}.
     * */
    public boolean isEmpty()
    {
        return this.equals(empty());
    }

    /**
     * Creates a new instance of an empty
     * selection.
     *
     * @retrun the empty selection.
     * */
    public static Selection empty()
    {
        return new Selection(-1, -1);
    }
}
