package se.skorup.snake;

import java.io.Serial;
import java.io.Serializable;

/**
 * A wrapper object in to represent a score.
 * */
public record Score(int score) implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1492521761930852855L;

    /**
     * Checks if a score is beaten.
     *
     * @param s the score to be compared against.
     * @return {@code true} iff {@code s.score > this.score}
     *         {@code false} iff {@code this.score >= s.score}
     * */
    public boolean isScoreBeaten(Score s)
    {
        return s.score > score;
    }

    /**
     * Checks if a score is beaten.
     *
     * @param score the score to be compared against.
     * @return {@code true} iff {@code score > this.score}
     *         {@code false} iff {@code this.score >= score}
     * */
    public boolean isScoreBeaten(int score)
    {
        return isScoreBeaten(new Score(score));
    }
}
