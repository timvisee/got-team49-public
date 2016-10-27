package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Pad;

public class Path {

    /**
     * Starting coordinate.
     */
    private Coordinaat start;

    /**
     * Ending coordinate.
     */
    private Coordinaat end;

    /**
     * The actual path.
     */
    private Pad path;

    /**
     * Constructor.
     *
     * @param start Starting coordinate.
     * @param end   Ending coordinate.
     * @param path  The actual path.
     */
    public Path(Coordinaat start, Coordinaat end, Pad path) {
        this.start = start;
        this.end = end;
        this.path = path;
    }

    /**
     * Get the starting coordinate.
     *
     * @return Starting coordinate.
     */
    public Coordinaat getStart() {
        return this.start;
    }

    /**
     * Get the ending coordinate.
     *
     * @return Ending coordinate.
     */
    public Coordinaat getEnd() {
        return this.end;
    }

    /**
     * Check whether this path is for the given two coordinates.
     *
     * @param start Start coordinate.
     * @param end   End coordinate.
     * @return True if this path is for the given coordinates, false if not.
     */
    public boolean isFor(Coordinaat start, Coordinaat end) {
        return this.start.equals(start) && this.end.equals(end);
    }

    /**
     * Get the path.
     *
     * @return Path.
     */
    public Pad getPath() {
        return this.path;
    }

    /**
     * Get the length.
     *
     * @return Length.
     */
    public int getLength() {
        return this.path.getTotaleTijd();
    }

    /**
     * Create a reversed Path instance.
     *
     * @return Reversed path.
     */
    public Path reverse() {
        return new Path(this.end, this.start, this.path.omgekeerd());
    }
}
