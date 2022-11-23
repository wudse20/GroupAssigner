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
public class GroupManager implements Serializable
{
    /** The serial ID of this class. */
    public static final long serialVersionUID = 6462178428517775725L;

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

    /**
     * Registers a person.
     *
     * @param name the name of the person.
     * @param r the role of the person.
     * @return the created person.
     * @throws IllegalArgumentException iff the name or the role is {@code null}
     *                                  and the name is shorter than 3 chars.
     * */
    public Person registerPerson(String name, Person.Role r) throws IllegalArgumentException
    {
        return registerPerson(name, r, getNextId());
    }

    /**
     * Registers a person, with a decided id. If
     * the ID is in use it will override the previous
     * person.
     *
     * @param name the name of the person.
     * @param r the role of the person.
     * @param id the id of the person, if the id is in use
     *           then it will override the previous person.
     * @return the created person.
     * @throws IllegalArgumentException iff the name or the role is {@code null}
     *                                  and the name is shorter than 3 chars.
     * */
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

    /**
     * Registers an new person.
     *
     * @param p the person to be registered.
     * @throws IllegalArgumentException iff p is null or the id of p is taken.
     * */
    public void registerPerson(Person p)
    {
        if (p == null)
            throw new IllegalArgumentException("The provided person is null.");
        else if (group.containsKey(p.getId()))
            throw new IllegalArgumentException("The ID is already in use.");

        group.put(p.getId(), p);
    }

    /**
     * Removes a person from the group.
     *
     * @param id the id of the person to be
     *           removed.
     * @return {@code true} iff the person was removed,
     *          else {@code false}.
     * */
    public boolean removePerson(int id)
    {
        if (group.containsKey(id))
            members--;

        return group.remove(id) != null;
    }

    /**
     * Returns a set that contains all the persons.
     *
     * @return A set that contains all persons.
     * */
    public Set<Person> getAllPersons()
    {
        return new HashSet<>(group.values());
    }

    /**
     * Returns a set that contains all the
     * persons except for p.
     *
     * @param p the person not contained in the set.
     * @return A set that contains all persons but p.
     * @throws IllegalArgumentException iff p == {@code null}
     * */
    public Set<Person> getAllBut(Person p) throws IllegalArgumentException
    {
        if (p == null)
            throw new IllegalArgumentException("Param p cannot be null");

        return group.values()
                    .stream()
                    .filter(x -> x.getId() != p.getId())
                    .collect(Collectors.toSet());
    }

    /**
     * Get all persons of roll r.
     *
     * @param r the roll that's wanted.
     * @return a set of persons with only the role r.
     * */
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

    /**
     * Gets all persons' ids of roll r.
     *
     * @param r the roll that's wanted.
     * @return a set of ids where the roll of the ids are r.
     * */
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

    /**
     * Gets all of a role but the provided
     * person p. The role used is the role
     * of the provided person.
     *
     * @param p the person that is being excluded.
     * @return the set containing all of the role
     *         of the person.
     * @throws IllegalArgumentException if p is {@code null}. It will
     *                                  also throw this exception if
     *                                  p isn't instanceof Candidate
     *                                  or Leader, i.e. Person. This
     *                                  shouldn't be possible but
     *                                  it's here just to be safe.
     * */
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

    /**
     * Gets all members of a given role and main group.
     *
     * @param r the role sought after.
     * @param mg the main group sought after.
     * @return a set containing all the persons of the
     *         passed role and main group.
     * */
    public Set<Person> getAllOfMainGroupAndRoll(Person.Role r, Person.MainGroup mg)
    {
        return getAllOfRoll(r).stream()
                              .filter(p -> p.getMainGroup().equals(mg))
                              .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Gets all members of a given role and main group but p.
     *
     * @param p the person to be excluded.
     * @param r the role sought after.
     * @param mg the main group sought after.
     * @return a set containing all the persons of the
     *         passed role and main group, where p is
     *         removed.
     * */
    public Set<Person> getAllOfMainGroupAndRollBut(Person p, Person.Role r, Person.MainGroup mg)
    {
        return new ImmutableHashSet<>(getAllOfMainGroupAndRoll(r, mg)).dropMatching(p).toSet();
    }

    /**
     * Gets a person from a given id.<br><br>
     *
     * Time: O(1)
     *
     * @param id the id of the person that's being searched from.
     * @return the person matching the id. If no person matches
     *         then it will return {@code null}.
     * @see GroupManager#getPersonFromName(String) GroupManager.getPersonFromString
     * */
    public Person getPersonFromId(int id)
    {
        return group.get(id);
    }

    /**
     * Gets a person from a given name. <br><br>
     *
     * Time: O(n)
     *
     * @param name the name that's being searched for.
     * @return {@code null} iff param name is null. If there are
     *         no results then it will return an empty list. Otherwise
     *         it will return a list of all the persons that matches
     *         the name.
     * @see GroupManager#getPersonFromId(int) GroupManager.getPersonFromId
     * */
    public List<Person> getPersonFromName(final String name)
    {
        if (name == null)
            return null;

        return group.values()
                    .stream()
                    .filter(x -> x.getName().equals(name))
                    .collect(Collectors.toList());
    }

    /**
     * Returns a list of all names.
     *
     * @return a list with all names.
     * */
    public List<String> getNames()
    {
        return group.values()
                    .stream()
                    .map(Person::getName)
                    .collect(Collectors.toList());
    }

    /**
     * Gets the next id of the person.
     *
     * @return the next id.
     * */
    public int getNextId()
    {
        return nextId++;
    }

    /**
     * Getter for: members
     *
     * @return the number of members.
     * */
    public int getMemberCount()
    {
        return members;
    }

    /**
     * Getter for: members of role.
     *
     * @param r the role sought after.
     * @return the number of members of role r.
     * */
    public int getMemberCountOfRole(Person.Role r)
    {
        return getAllOfRoll(r).size();
    }

    /**
     * Getter for: group.
     *
     * @return the map containing the group.
     * */
    public Map<Integer, Person> getGroup()
    {
        return group;
    }

    /**
     * Generates the graph based on the denylists.
     * The graph (V, E), where V is the set of
     * vertices and edges E &#8838; V x V. This graph
     * is undirected, so if (v, w) &#8712; E &hArr;
     * (w, v) &#8712; E. This method returns E.
     *
     * @return a set containing the relations
     *         consisting of the edges, binary
     *         tuples.
     * */
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

    /**
     * Generates the graph based on the wishlists.
     * The graph (V, E), where V is the set of
     * vertices and E the set of edges.
     * E &#8838; V x V. This method returns E.
     *
     * @return a set containing the relations
     *         consisting of the edges, binary
     *         tuples.
     * */
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

    /**
     * Setter for: name
     *
     * @param name the new name of the group.
     * */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Getter for: name
     *
     * @return the name of the GroupManager.
     * */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the number of persons of a certain main group.
     *
     * @param mg the main group which is searched.
     * @return the number of persons in mg.
     * */
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
