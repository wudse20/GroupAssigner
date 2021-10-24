package se.skorup.main.manager;

import org.testng.annotations.Test;
import se.skorup.API.util.DebugMethods;
import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * The class responsible for testing
 * the group manager.
 * */
public class GroupManagerTester
{
    /**
     * Tests the register person method,
     * were everything is fine.
     * */
    @Test
    public void testRegisterPerson()
    {
        var gm = new GroupManager("");

        try
        {
            gm.registerPerson("Anton", Person.Role.LEADER);
            gm.registerPerson("Sebbe", Person.Role.CANDIDATE);
        }
        catch (Exception e)
        {
            DebugMethods.log(
                "Unexpected exception: %s%n".formatted(e.getLocalizedMessage()),
                DebugMethods.LogType.ERROR
            );
            throw new RuntimeException(e.getLocalizedMessage()); // To indicate the test failed.
        }
    }

    /**
     * Tests the register person method,
     * were everything isn't fine.
     * */
    @Test
    public void testRegisterPersonThrows()
    {
        var gm = new GroupManager("");
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson(null, Person.Role.LEADER));
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson("  a  ", Person.Role.CANDIDATE));
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson("Anton", null));
    }

    /**
     * Tests that register person returns the correct
     * type for the persons, created.
     * */
    @Test
    public void testRegisterPersonType()
    {
        var gm = new GroupManager("");

        assertTrue(gm.registerPerson("Anton", Person.Role.LEADER) instanceof Leader);
        assertTrue(gm.registerPerson("Sebbe", Person.Role.CANDIDATE) instanceof Candidate);
    }

    /**
     * Tests the remove person method
     * */
    @Test
    public void testRemove()
    {
        var gm = new GroupManager("");
        var p = gm.registerPerson("Anton", Person.Role.LEADER);
        assertTrue(gm.removePerson(p.getId()));
        assertFalse(gm.removePerson(p.getId()));
    }

    /**
     * Test the get all persons method.
     * */
    @Test
    public void testGetAllPersons()
    {
        var gm = new GroupManager("");

        var ctr = new HashSet<>(
                Arrays.asList(
                    gm.registerPerson("Anton", Person.Role.LEADER),
                    gm.registerPerson("Sebbe", Person.Role.CANDIDATE)
                )
        );

        assertEquals(ctr.size(), gm.getAllPersons().size());
        assertEquals(ctr, gm.getAllPersons());
        assertNotSame(gm.getAllPersons(), gm.getAllPersons());
    }

    /**
     * Test the get all of roll method.
     * */
    @Test
    public void testGetAllOfRole()
    {
        var gm = new GroupManager("");

        var ctr = new HashSet<>(
                Collections.singletonList(
                        gm.registerPerson("Anton", Person.Role.LEADER)
                )
        );

        var ctr2 = new HashSet<>(
                Collections.singletonList(
                        gm.registerPerson("Sebbe", Person.Role.CANDIDATE)
                )
        );

        assertEquals(ctr.size(), gm.getAllOfRoll(Person.Role.LEADER).size());
        assertEquals(ctr, gm.getAllOfRoll(Person.Role.LEADER));
        assertEquals(ctr2.size(), gm.getAllOfRoll(Person.Role.CANDIDATE).size());
        assertEquals(ctr2, gm.getAllOfRoll(Person.Role.CANDIDATE));
    }

    /**
     * Tests the method get from id.
     * */
    @Test
    public void testGetFromId()
    {
        var gm = new GroupManager("");
        var p = gm.registerPerson("Anton", Person.Role.LEADER);
        assertNotNull(gm.getPersonFromId(p.getId()));

        gm.removePerson(p.getId());
        assertNull(gm.getPersonFromId(p.getId()));
    }

    /**
     * Test the method get from name.
     * */
    @Test
    public void testGetFromName()
    {
        var gm = new GroupManager("");
        gm.registerPerson("Anton", Person.Role.LEADER);
        assertNull(gm.getPersonFromName(null));
        assertEquals(0, gm.getPersonFromName("Sebbe").size());
        assertEquals(1, gm.getPersonFromName("Anton").size());
    }

    /**
     * Test the method get from name with
     * many entries.
     * */
    @Test
    public void manyGetFromName()
    {
        var gm = new GroupManager("");

        for (int i = 0; i < (int) Math.pow(10, 3); i++)
        {
            gm.registerPerson("Anton", Person.Role.LEADER);
            assertEquals(i + 1, gm.getPersonFromName("Anton").size());
        }
    }

    /**
     * Tests the member count.
     * */
    @Test
    public void testMemberCount()
    {
        var gm = new GroupManager("");
        assertEquals(0, gm.getMemberCount());

        Person p = null;
        int i = 0;
        for (; i < (int) Math.pow(10, 6); i++)
        {
            p = gm.registerPerson("Anton", Person.Role.LEADER);
            assertEquals(i + 1, gm.getMemberCount());
        }

        assert p != null;
        gm.removePerson(p.getId());
        assertEquals(i - 1, gm.getMemberCount());
        gm.removePerson(p.getId());
        assertEquals(i - 1, gm.getMemberCount());

        try { gm.registerPerson(null, null); }
        catch (IllegalArgumentException e) {}

        assertEquals(i - 1, gm.getMemberCount());
    }

    /**
     * Tests the get all but person method.
     * */
    @Test
    public void testGetAllBut()
    {
        var gm = new GroupManager("");

        var p = gm.registerPerson("Anton", Person.Role.LEADER);
        var p2 = gm.registerPerson("Sebbe", Person.Role.CANDIDATE);

        var ctr = new HashSet<>(Arrays.asList(p, p2));
        var ctr2 = new HashSet<>(Collections.singletonList(p2));

        assertEquals(ctr.size() - 1, gm.getAllBut(p).size());
        assertEquals(ctr2, gm.getAllBut(p));
        assertThrows(IllegalArgumentException.class, () -> gm.getAllBut(null));
    }

    /**
     * Tests the get all of role but method.
     * */
    @Test
    public void testGetAllOfRoleBut()
    {
        var gm = new GroupManager("");

        var p = gm.registerPerson("Some other leader", Person.Role.LEADER);
        var p2 = gm.registerPerson("Some other candidate", Person.Role.CANDIDATE);

        var ctr = new HashSet<>(
            Collections.singletonList(
                gm.registerPerson("Anton", Person.Role.LEADER)
            )
        );

        var ctr2 = new HashSet<>(
            Collections.singletonList(
                gm.registerPerson("Sebbe", Person.Role.CANDIDATE)
            )
        );

        assertEquals(ctr, gm.getAllOfRollBut(p));
        assertEquals(ctr2, gm.getAllOfRollBut(p2));
        assertThrows(IllegalArgumentException.class, () -> gm.getAllOfRollBut(null));
        assertThrows(IllegalArgumentException.class, () -> gm.getAllOfRollBut(new Person("Anton", 123) {}));
    }

    /**
     * Tests the equals method.
     * */
    @Test
    public void testEquals()
    {
        var gm = new GroupManager("");

        assertEquals(gm, new GroupManager(""));
        assertNotEquals(gm, new GroupManager("Hej"));

        var p1 = gm.registerPerson("Anton", Person.Role.LEADER);
        var p2 = gm.registerPerson("Sebbe", Person.Role.CANDIDATE);

        var gm2 = new GroupManager("");
        var p3 = gm2.registerPerson("Anton", Person.Role.LEADER);
        var p4 = gm2.registerPerson("Sebbe", Person.Role.CANDIDATE);

        gm2.getGroup().remove(p3.getId());
        gm2.getGroup().remove(p4.getId());

        p3.setId(p1.getId());
        p4.setId(p2.getId());

        gm2.getGroup().put(p3.getId(), p3);
        gm2.getGroup().put(p4.getId(), p4);

        assertEquals(gm2, gm);
        assertNotEquals(gm, null);

        gm2.registerPerson("Anton", Person.Role.LEADER);
        assertNotEquals(gm, gm2);

        var gm3 = new GroupManager("Kaka");
        gm3.registerPerson("Anton", Person.Role.LEADER);
        gm3.registerPerson("Sebbe", Person.Role.CANDIDATE);

        assertNotEquals(gm, gm3);
    }

    /**
     * Tests the getDenyGraph method.
     * */
    @Test
    public void testGetDenyGraph()
    {
        var gm = new GroupManager("");

        var p1 = gm.registerPerson("Anton", Person.Role.CANDIDATE);
        var p2 = gm.registerPerson("Sebbe", Person.Role.CANDIDATE);
        var p3 = gm.registerPerson("Sebbe 2", Person.Role.CANDIDATE);

        p1.addDenylistId(p2.getId());
        p2.addDenylistId(p3.getId());

        var ctr = new HashSet<Tuple>();

        var t1 = new Tuple(p1.getId(), p2.getId());
        var t2 = new Tuple(p2.getId(), p3.getId());

        ctr.add(t1);
        ctr.add(t2);
        ctr.add(t1.invert());
        ctr.add(t2.invert());

        assertEquals(ctr, gm.getDenyGraph());
    }

    /**
     * Tests the getAllOfRoleAndMainGroup method.
     * */
    @Test
    public void testGetMainGroup()
    {
        var gm = new GroupManager("");

        var p1 = gm.registerPerson("Anton", Person.Role.CANDIDATE);
        var p2 = gm.registerPerson("Sebbe", Person.Role.CANDIDATE);
        gm.registerPerson("Sebbe 2", Person.Role.CANDIDATE);

        p1.setMainGroup(Person.MainGroup.MAIN_GROUP_2);
        p2.setMainGroup(Person.MainGroup.MAIN_GROUP_2);

        assertEquals(2, gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_2).size());
        assertEquals(1, gm.getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, Person.MainGroup.MAIN_GROUP_1).size());
    }
}
