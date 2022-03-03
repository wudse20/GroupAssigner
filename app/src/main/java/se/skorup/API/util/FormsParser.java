package se.skorup.API.util;

import se.skorup.API.collections.immutable_collections.ImmutableArray;
import se.skorup.main.manager.GroupManager;
import se.skorup.main.objects.Person;

/**
 * The class that parses the GoogleForms data.
 * */
public class FormsParser
{
    /**
     * Parses a String of the form and
     * registers the person to a
     * GroupManager.
     *
     * @param data the data to be parsed.
     * @param gm the group manager.
     * */
    public static void parseFormData(String data, GroupManager gm)
    {
        data = data.trim();
        data = ImmutableArray.fromArray(data.split("")).dropMatching("\"").mkString("");

        if (data.equals(""))
            return;

        var lines = data.split("\n");
        var processedData =
                ImmutableArray.fromArray(lines)
                              .drop(1) // Drops the header.
                              .map(x -> x.split(",")) // Splits at ','.
                              .map(ImmutableArray::fromArray) // Converts to immutable arrays.
                              .map(x -> x.drop(1)) // drops the time info.
                              .map(x -> x.map(String::trim))
                              .map(x -> x.map(Utils::toNameCase)); // Trims the strings to shape.

        DebugMethods.log(processedData.toString(), DebugMethods.LogType.DEBUG);

        for (int i = 0; i < processedData.size(); i++)
        {
            var curArr = processedData.get(i);
            Person p = null;
            for (int ii = 0; ii < curArr.size(); ii++)
            {
                var s = curArr.get(ii);
                var names = gm.getNames();

                var person = !names.contains(s) ?
                    gm.registerPerson(s, Person.Role.CANDIDATE) :
                    gm.getPersonFromName(s).get(0);

                if (ii == 0)
                {
                    p = person;
                    continue;
                }

                assert p != null;
                p.addWishlistId(person.getId());
            }
        }
    }
}
