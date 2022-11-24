package se.skorup.main.manager;

import se.skorup.API.collections.immutable_collections.ImmutableHashSet;
import se.skorup.main.objects.Candidate;
import se.skorup.main.objects.Leader;
import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The manager that registers and keeps track of
 * the different people in a group.
 * */
public class GroupManager implements Group
{
    /** The serial ID of this class. */

    private int nextId = 0;
    private int members = 0;

    private final Map<Integer, Person> group;

    private String name;

    /**
     * Creates a new empty GroupManager.<br><br>
     *
     * @param name the name of the group.
     * */
    public GroupManager(String name)
    {
        this.group = new HashMap<>();
        this.name = name;
    }

    public Person registerPerson(String name, Person.Role r) throws IllegalArgumentException
    {
        return registerPerson(name, r, getNextId());
    }

    public Person registerPerson(String name, Person.Role r, int id) throws IllegalArgumentException
    {
        if (name == null)
            throw new IllegalArgumentException("The provided name cannot be null.");
        else if (r == null)
            throw new IllegalArgumentException("The provided role cannot be null.");
        else if (name.trim().length() < 2)
            throw new IllegalArgumentException(
                "The provided name needs to be at least of length 2, provided length %d"
                .formatted(name.trim().length())
            );

        var p = (r.equals(Person.Role.LEADER)) ? new Leader(name, id) : new Candidate(name, id);
        group.put(id, p);
        members++;
        return p;
    }

    public void registerPerson(Person p)
    {
        if (p == null)
            throw new IllegalArgumentException("The provided person is null.");
        else if (group.containsKey(p.getId()))
            throw new IllegalArgumentException("The ID is already in use.");

        group.put(p.getId(), p);
    }

    public boolean removePerson(int id)
    {
        if (group.containsKey(id))
            members--;

        return group.remove(id) != null;
    }

    public Set<Person> getAllPersons()
    {
        return new HashSet<>(group.values());
    }

    public Set<Person> getAllBut(Person p) throws IllegalArgumentException
    {
        if (p == null)
            throw new IllegalArgumentException("Param p cannot be null");

        return group.values()
                    .stream()
                    .filter(x -> x.getId() != p.getId())
                    .collect(Collectors.toSet());
    }

    public Set<Person> getAllOfRoll(final Person.Role r)
    {
        return group.values()
                    .stream()
                    .filter(x ->
                        r.equals(Person.Role.LEADER) ?
                        x instanceof Leader :
                        x instanceof Candidate
                    ).collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Integer> getAllIdsOfRoll(final Person.Role r)
    {
        return group.values()
                    .stream()
                    .filter(x ->
                        r.equals(Person.Role.LEADER) ?
                        x instanceof Leader :
                        x instanceof Candidate
                    ).map(Person::getId)
                    .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Person> getAllOfRollBut(Person p) throws IllegalArgumentException
    {
        if (p == null)
            throw new IllegalArgumentException("Param p cannot be null");
        else if (!(p instanceof Candidate || p instanceof Leader)) // Shouldn't be possible; but just to be safe :)
            throw new IllegalArgumentException(
                "Param p cannot be an instance of class Person, " +
                "needs to be an instance of the subclasses."
            );

        var res = getAllOfRoll(p instanceof Candidate ? Person.Role.CANDIDATE : Person.Role.LEADER);
        res.remove(p);

        return res;
    }

    public Set<Person> getAllOfMainGroupAndRoll(Person.Role r, Person.MainGroup mg)
    {
        return getAllOfRoll(r).stream()
                              .filter(p -> p.getMainGroup().equals(mg))
                              .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Person> getAllOfMainGroupAndRollBut(Person p, Person.Role r, Person.MainGroup mg)
    {
        return new ImmutableHashSet<>(getAllOfMainGroupAndRoll(r, mg)).dropMatching(p).toSet();
    }

    public Person getPersonFromId(int id)
    {
        return group.get(id);
    }

    public List<Person> getPersonFromName(final String name)
    {
        if (name == null)
            return null;

        return group.values()
                    .stream()
                    .filter(x -> x.getName().equals(name))
                    .collect(Collectors.toList());
    }

    public List<String> getNames()
    {
        return group.values()
                    .stream()
                    .map(Person::getName)
                    .collect(Collectors.toList());
    }

    public int getNextId()
    {
        return nextId++;
    }

    public int getMemberCount()
    {
        return members;
    }

    public int getMemberCountOfRole(Person.Role r)
    {
        return getAllOfRoll(r).size();
    }

    public Map<Integer, Person> getGroup()
    {
        return group;
    }

    public Set<Tuple> getDenyGraph()
    {
        var result = new HashSet<Tuple>();

        for (var e : group.entrySet())
        {
            for (var i : e.getValue().getDenylist())
            {
                result.add(new Tuple(e.getKey(), i));
                result.add(new Tuple(i, e.getKey())); // Adds the inverse as well.
            }
        }

        return result;
    }

    public Set<Tuple> getWishGraph()
    {
        var result = new HashSet<Tuple>();

        for (var e : group.entrySet())
        {
            for (var i : e.getValue().getWishlist())
                result.add(new Tuple(e.getKey(), i));
        }

        return result;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public int getMembersOfMainGroup(Person.MainGroup mg)
    {
        return getAllOfMainGroupAndRoll(Person.Role.CANDIDATE, mg).size();
    }

    @Override
    public String toString()
    {
        return "%s (%d)".formatted(name, members);
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof GroupManager g &&
               this.group.equals(g.group)  &&
               this.name.equals(g.name)    &&
               this.members == g.members;
    }

    @Override
    public int hashCode()
    {
        return group.hashCode() + name.hashCode() + members;
    }
}
