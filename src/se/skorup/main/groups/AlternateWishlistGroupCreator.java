package se.skorup.main.groups;

import se.skorup.main.manager.GroupManager;

/**
 * An alternate GroupCreator for wishlist generation.
 * */
public class AlternateWishlistGroupCreator extends WishlistGroupCreator
{
    /**
     * Creates a new GroupCreator.
     *
     * @param gm the group manager used to create
     *           the subgroups.
     */
    public AlternateWishlistGroupCreator(GroupManager gm)
    {
        super(gm);
    }

    @Override
    public String toString()
    {
        return "Alternativt sätt att skapa grupper efter önskningar (NYI)";
    }
}
