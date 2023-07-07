package se.skorup.group;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPerson
{
    public static Stream<Arguments> testData()
    {
        return Stream.of(
            Arguments.of(new Person("Kalle", 0), new Person("Kalle", 0), true),
            Arguments.of(new Person("Kalle", 0), new Person("Liza", 1), false),
            Arguments.of(new Person("Kalle", 0), new Person("Kalle", 1), false),
            Arguments.of(new Person("Kalle", 0), new Person("Liza", 0), true)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testEquals(Person p1, Person p2, boolean expected)
    {
        var error = "%s.equals(%s) == %b, but where supposed to be: %b".formatted(p1, p2, !expected, expected);
        assertEquals(expected, p1.equals(p2), error);
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testHashCode(Person p1, Person p2, boolean expected)
    {
        var error = "Got: %b, Expected: %b, p1.hashCode() should %sbe equal to p2.hashCode()%np1: %s%np2: %s"
            .formatted(!expected, expected, expected ? "" : "not ", p1, p2);
        assertEquals(expected, p1.hashCode() == p2.hashCode(), error);
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testHashAndEquals(Person p1, Person p2, boolean expected)
    {
        var e1 = "%s.equals(%s) == %b, but where supposed to be: %b".formatted(p1, p2, !expected, expected);
        var e2 = "Got: %b, Expected: %b, p1.hashCode() should %sbe equal to p2.hashCode()%np1: %s%np2: %s"
                .formatted(!expected, expected, expected ? "" : "not ", p1, p2);
        assertEquals(expected, p1.equals(p2), e1);
        assertEquals(expected, p1.hashCode() == p2.hashCode(), e2);
        assertEquals(
            expected, p1.hashCode() == p2.hashCode() && p1.equals(p2),
            "HashCode should be equal if objects are equal."
        );
    }
}
