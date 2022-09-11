package se.skorup.main.objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.skorup.main.manager.GroupManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSubgroup
{
    private static final long subgroupSerialVersionUUID = 3812458839615746121L;

    private static final Subgroups KAKA = new Subgroups(
        "kaka", null, false,
        false, null, null
    );

    private static Subgroups kaka2()
    {
        var set1 = new HashSet<Integer>();
        var set2 = new HashSet<Integer>();

        set1.add(1);
        set1.add(2);
        set2.add(3);
        set2.add(4);

        return new Subgroups(
            "kaka", List.of(set1, set2), false,
            false, new String[] {"hej", "då"},
            List.of(new Leader("Anton", 5), new Leader("Sebbe", 6))
        );
    }

    private static SGWithGM kaka3()
    {
        var gm = new GroupManager("Krokodilernas rikspolisförbund");
        var p1 = gm.registerPerson("AAA", Person.Role.CANDIDATE);
        var p2 = gm.registerPerson("BBB", Person.Role.CANDIDATE);
        var p3 = gm.registerPerson("CCC", Person.Role.CANDIDATE);
        var p4 = gm.registerPerson("DDD", Person.Role.CANDIDATE);

        p1.addWishlistId(p2.getId());
        p1.addWishlistId(p3.getId());
        p1.addWishlistId(p4.getId());
        p2.addWishlistId(p3.getId());
        p2.addWishlistId(p4.getId());
        p3.addWishlistId(p4.getId());

        var sg = new Subgroups(
            "Kaka3", List.of(Set.of(1, 2), Set.of(3, 4)),
            false, false, new String[2],
            List.of()
        );

        return new SGWithGM(sg, gm);
    }

    public static Stream<Arguments> getNameTests()
    {
        return Stream.of(
            Arguments.of(KAKA, "kaka2"),
            Arguments.of(KAKA, null),
            Arguments.of(KAKA, ""),
            Arguments.of(kaka2(), "kaka2"),
            Arguments.of(kaka2(), null),
            Arguments.of(kaka2(), "")
        );
    }

    public static Stream<Arguments> getLabelTests()
    {
        return Stream.of(
            Arguments.of(KAKA, 0, ""),
            Arguments.of(kaka2(), 0, "hej"),
            Arguments.of(kaka2(), 1, "då"),
            Arguments.of(kaka2(), 2, ""),
            Arguments.of(kaka2(), -123, "")
        );
    }

    public static Stream<Arguments> getGroupAddingData()
    {
        var big = new int[1000];
        var set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        for (var i = 7; i < big.length + 7; i++)
        {
            big[i - 7] = i;
            set.add(i);
        }

        return Stream.of(
            Arguments.of(KAKA, 3, new int[] {1, 2, 3}, Set.of()),
            Arguments.of(kaka2(), 0, new int[] {7, 8}, Set.of(1, 2, 7, 8)),
            Arguments.of(kaka2(), 1, new int[] {7, 8}, Set.of(3, 4, 7, 8)),
            Arguments.of(kaka2(), 3, new int[] {7, 8}, Set.of()),
            Arguments.of(kaka2(), 0, big, set)
        );
    }

    public static Stream<Arguments> getGroupRemovingData()
    {
        return Stream.of(
            Arguments.of(KAKA, 3, new int[] {1, 2, 3}, Set.of()),
            Arguments.of(kaka2(), 0, new int[] {1, 2}, Set.of()),
            Arguments.of(kaka2(), 0, new int[] {1}, Set.of(2)),
            Arguments.of(kaka2(), 0, new int[] {2}, Set.of(1)),
            Arguments.of(kaka2(), 1, new int[] {3}, Set.of(4)),
            Arguments.of(kaka2(), 1, new int[] {4}, Set.of(3)),
            Arguments.of(kaka2(), 1, new int[] {3, 4}, Set.of()),
            Arguments.of(kaka2(), 3, new int[] {1, 2, 3}, Set.of()),
            Arguments.of(kaka2(), -123, new int[] {1, 2, 3}, Set.of())
        );
    }

    public static Stream<Arguments> testGetWishesData()
    {
        return Stream.of(
            Arguments.of(kaka3(), 1, 1),
            Arguments.of(kaka3(), 2, 0),
            Arguments.of(kaka3(), 3, 0),
            Arguments.of(kaka3(), 4, 0)
        );
    }

    @Test
    public void testSerialVersionUUID()
    {
        assertEquals(subgroupSerialVersionUUID, KAKA.getSerialVersionUID(), "SerialVersionUUID miss match!");
    }

    @ParameterizedTest
    @MethodSource("getNameTests")
    public void testChangeName(Subgroups sg, String newName)
    {
        var sg2 = sg.changeName(newName);
        assertNotSame(sg, sg2, "The updated object is the same instance WRONG!");
        assertEquals(newName, sg2.name(), "The name is wrong!");
        assertNotNull(sg2);
        assertEquals(sg.groups(), sg2.groups(), "groups don't match!");
        assertEquals(sg.isLeaderMode(), sg2.isLeaderMode(), "isLeaderMode don't match!");
        assertEquals(sg.isWishListMode(), sg2.isWishListMode(), "isWishListMode don't match!");
        assertArrayEquals(sg.labels(), sg2.labels(), "labels don't match!");
        assertEquals(sg.leaders(), sg2.leaders(), "leaders don't match!");
    }

    @ParameterizedTest
    @MethodSource("getLabelTests")
    public void testGetLabel(Subgroups sg, int index, String expected)
    {
        if (sg.labels() == null)
        {
            assertThrows(NullPointerException.class, () -> sg.getLabel(index), "Does not throw when null!");
            return;
        }

        if (sg.labels().length <= index || index < 0)
        {
            assertThrows(
                IndexOutOfBoundsException.class, () -> sg.getLabel(index), "Does not throw when supposed to!"
            );
            return;
        }

        assertDoesNotThrow(() -> sg.getLabel(index), "Get label throws we supposed not to!");
        assertEquals(expected, sg.getLabel(index), "Wrong label");
    }

    @ParameterizedTest
    @MethodSource("getLabelTests")
    public void testSetLabel(Subgroups sg, int index, String newLabel)
    {
        if (sg.labels() == null)
        {
            assertThrows(NullPointerException.class, () -> sg.getLabel(index), "Does not throw when null!");
            return;
        }

        if (sg.labels().length <= index || index < 0)
        {
            assertThrows(
                IndexOutOfBoundsException.class, () -> sg.getLabel(index), "Does not throw when supposed to!"
            );
            return;
        }

        assertDoesNotThrow(() -> sg.setLabel(index, newLabel), "The new label set threw unexpected exception.");
        assertEquals(newLabel, sg.getLabel(index), "The new label isn't correctly updated.");
    }

    @ParameterizedTest
    @MethodSource("getGroupAddingData")
    public void testAddPersonToGroup(Subgroups sg, int groupIndex, int[] ids, Set<Integer> expected)
    {
        if (sg.groups() == null)
        {
            for (var id : ids)
            {
                assertThrows(
                    NullPointerException.class,
                    () -> sg.addPersonToGroup(id, groupIndex),
                    "Should throw NullPointerException."
                );
            }

            return;
        }

        if (sg.groups().size() <= groupIndex || groupIndex < 0)
        {
            for (var id : ids)
            {
                assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> sg.addPersonToGroup(id, groupIndex),
                    "Should throw IndexOutOfBoundsException."
                );
            }

            return;
        }

        for (var id : ids)
            assertDoesNotThrow(
                () -> sg.addPersonToGroup(id, groupIndex),
                "Adding should not throw an exception!"
            );

        assertEquals(expected, sg.groups().get(groupIndex), "Groups should match; but they don't!");
    }

    @ParameterizedTest
    @MethodSource("getGroupRemovingData")
    public void testRemovePersonFromGroup(Subgroups sg, int groupIndex, int[] ids, Set<Integer> expected)
    {
        if (sg.groups() == null)
        {
            for (var id : ids)
            {
                assertThrows(
                    NullPointerException.class,
                    () -> sg.addPersonToGroup(id, groupIndex),
                    "Should throw NullPointerException."
                );
            }

            return;
        }

        if (sg.groups().size() <= groupIndex || groupIndex < 0)
        {
            for (var id : ids)
            {
                assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> sg.addPersonToGroup(id, groupIndex),
                    "Should throw IndexOutOfBoundsException."
                );
            }

            return;
        }

        for (var id : ids)
            assertDoesNotThrow(
                () -> sg.removePersonFromGroup(id, groupIndex),
                "Adding should not throw an exception!"
            );

        assertEquals(expected, sg.groups().get(groupIndex), "Groups should match; but they don't!");
    }

    @ParameterizedTest
    @MethodSource("testGetWishesData")
    public void testGetWises(SGWithGM x, int id, int expected)
    {
        assertEquals(expected, x.sg.getNumberOfWishes(id, x.gm), "Wishes are not correct");
    }

    private record SGWithGM(Subgroups sg, GroupManager gm) {}
}
