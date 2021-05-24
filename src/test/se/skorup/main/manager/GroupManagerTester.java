package se.skorup.main.manager;

import org.testng.annotations.Test;
import se.skorup.main.objects.Person;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

/**
 * The class responsible for testing
 * the group manager.
 * */
public class GroupManagerTester
{
    /**
     * Tests the getNextId method.
     * */
    @Test
    public void testGetNextId()
    {
        for (int i = 0; i < (int) Math.pow(10, 6); i++)
            assertEquals(i, GroupManager.getNextId());
    }

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
}
