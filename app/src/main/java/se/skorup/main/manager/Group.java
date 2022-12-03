package se.skorup.main.manager;

import se.skorup.main.objects.Person;
import se.skorup.main.objects.Tuple;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The common type for all GroupManagers
 * */
public interface Group extends Serializable
{
    long serialVersionUID = 6462178428517775725L;

    /**
     * Registers a person.
     *
     * @param name the name of the person.
     * @param r the role of the person.
     * @return the created person.
     * @throws IllegalArgumentException iff the name or the role is {@code null}
     *                                  and the name is shorter than 3 chars.
     * */
    Person registerPerson(String name, Person.Role r) throws IllegalArgumentException;

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
    Person registerPerson(String name, Person.Role r, int id) throws IllegalArgumentException;

    /**
     * Registers an new person.
     *
     * @param p the person to be registered.
     * @throws IllegalArgumentException iff p is null or the id of p is taken.
     * */
    void registerPerson(Person p);

    /**
     * Removes a person from the group.
     *
     * @param id the id of the person to be
     *           removed.
     * @return {@code true} iff the person was removed,
     *          else {@code false}.
     * */
    boolean removePerson(int id);

    /**
     * Returns a set that contains all the persons.
     *
     * @return A set that contains all persons.
     * */
    Set<Person> getAllPersons();

    /**
     * Returns a set that contains all the
     * persons except for p.
     *
     * @param p the person not contained in the set.
     * @return A set that contains all persons but p.
     * @throws IllegalArgumentException iff p == {@code null}
     * */
    Set<Person> getAllBut(Person p) throws IllegalArgumentException;

    /**
     * Get all persons of roll r.
     *
     * @param r the roll that's wanted.
     * @return a set of persons with only the role r.
     * */
    Set<Person> getAllOfRoll(final Person.Role r);

    /**
     * Gets all persons' ids of roll r.
     *
     * @param r the roll that's wanted.
     * @return a set of ids where the roll of the ids are r.
     * */
    Set<Integer> getAllIdsOfRoll(final Person.Role r);

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
    Set<Person> getAllOfRollBut(Person p) throws IllegalArgumentException;

    /**
     * Gets all members of a given role and main group.
     *
     * @param r the role sought after.
     * @param mg the main group sought after.
     * @return a set containing all the persons of the
     *         passed role and main group.
     * */
    Set<Person> getAllOfMainGroupAndRoll(Person.Role r, Person.MainGroup mg);

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
    Set<Person> getAllOfMainGroupAndRollBut(Person p, Person.Role r, Person.MainGroup mg);

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
    Person getPersonFromId(int id);

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
    List<Person> getPersonFromName(String name);

    /**
     * Returns a list of all names.
     *
     * @return a list with all names.
     * */
    List<String> getNames();

    /**
     * Gets the next id of the person.
     *
     * @return the next id.
     * */
    int getNextId();

    /**
     * Getter for: members
     *
     * @return the number of members.
     * */
    int getMemberCount();

    /**
     * Getter for: members of role.
     *
     * @param r the role sought after.
     * @return the number of members of role r.
     * */
    int getMemberCountOfRole(Person.Role r);

    /**
     * Getter for: group.
     *
     * @return the map containing the group.
     * */
    Map<Integer, Person> getGroup();

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
    Set<Tuple> getDenyGraph();

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
    Set<Tuple> getWishGraph();

    /**
     * Setter for: name
     *
     * @param name the new name of the group.
     * */
    void setName(String name);

    /**
     * Getter for: name
     *
     * @return the name of the GroupManager.
     * */
    String getName();

    /**
     * Gets the number of persons of a certain main group.
     *
     * @param mg the main group which is searched.
     * @return the number of persons in mg.
     * */
    int getMembersOfMainGroup(Person.MainGroup mg);
}
