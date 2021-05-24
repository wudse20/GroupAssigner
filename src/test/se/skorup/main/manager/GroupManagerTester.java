package se.skorup.main.manager;

import org.testng.annotations.Test;
import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
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
        var gm = new GroupManager();

        try
        {
            gm.registerPerson("Anton", Person.Role.LEADER);
            gm.registerPerson("Sebbe", Person.Role.CANDIDATE);
        }
        catch (Exception e)
        {
            System.out.printf("Unexpected exception: %s%n", e.getLocalizedMessage());
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
        var gm = new GroupManager();
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson(null, Person.Role.LEADER));
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson("  ap  ", Person.Role.CANDIDATE));
        assertThrows(IllegalArgumentException.class, () -> gm.registerPerson("Anton", null));
    }

    /**
     * Tests that register person returns the correct
     * type for the persons, created.
     * */
    @Test
    public void testRegisterPersonType()
    {
        var gm = new GroupManager();

        assertTrue(gm.registerPerson("Anton", Person.Role.LEADER) instanceof Leader);
        assertTrue(gm.registerPerson("Sebbe", Person.Role.CANDIDATE) instanceof Candidate);
    }

    /**
     * Tests the remove person method
     * */
    @Test
    public void testRemove()
    {
        var gm = new GroupManager();
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
        var gm = new GroupManager();

        var ctr = new HashSet<>(
                Arrays.asList(
                    gm.registerPerson("Anton", Person.Role.LEADER),
                    gm.registerPerson("Sebbe", Person.Role.CANDIDATE)
                )
        );

        assertEquals(ctr.size(), gm.getAllPersons().size());
        assertEquals(ctr, gm.getAllPersons());
    }

    /**
     * Test the get all of roll method.
     * */
    @Test
    public void testGetAllOfRole()
    {
        var gm = new GroupManager();

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
        var gm = new GroupManager();
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
        var gm = new GroupManager();
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
        var gm = new GroupManager();

        for (int i = 0; i < (int) Math.pow(10, 3); i++)
        {
            gm.registerPerson("Anton", Person.Role.LEADER);
            assertEquals(i + 1, gm.getPersonFromName("Anton").size());
        }
    }
}
