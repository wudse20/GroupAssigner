package se.skorup.games.base;

/**
 * A position in the program.
 * */
public record Pos(int x, int y)
{
    /**
     * Carries out an element by element multiplication of the points.
     * <br><br>
     * {@code var p1 = new Pos(a, b);}<br>
     * {@code var p2 = new Pos(c, d);}<br>
     * {@code p1.multiply(p2) = new Pos(ac, bd)}
     *
     * @param p the position to multiply with.
     * @return the result of the multiplication as a new instance.
     * */
    public Pos multiply(Pos p)
    {
        return new Pos(x * p.x, y * p.y);
    }

    /**
     * Carries out an  multiplication of the point with a scalar.
     * <br><br>
     * {@code var p1 = new Pos(x, y);}<br>
     * {@code var a = b;}<br>
     * {@code p1.multiply(a) = new Pos(a * x, b * y)}
     *
     * @param i the scalar to multiply with.
     * @return the result of the multiplication as a new instance.
     * */
    public Pos multiply(int i)
    {
        return new Pos(x * i, y * i);
    }

    /**
     * Carries out an element by element addition of the points.
     * <br><br>
     * {@code var p1 = new Pos(a, b);}<br>
     * {@code var p2 = new Pos(c, d);}<br>
     * {@code p1.add(p2) = new Pos(a + c, b + d)}
     *
     * @param p the position to add with.
     * @return the result of the addition as a new instance.
     * */
    public Pos add(Pos p)
    {
        return new Pos(x + p.x, y + p.y);
    }
}
