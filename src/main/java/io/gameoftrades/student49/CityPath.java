package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;

public class CityPath extends Path {

    /**
     * Starting city.
     */
    private Stad start;

    /**
     * Ending city.
     */
    private Stad end;

    /**
     * Constructor.
     *
     * @param start Starting city.
     * @param end   Ending city.
     * @param path  Path.
     */
    public CityPath(Stad start, Stad end, Pad path) {
        // Construct the super
        super(start.getCoordinaat(), end.getCoordinaat(), path);

        // Set the start and end cities
        this.start = start;
        this.end = end;
    }

    /**
     * Constructor.
     *
     * @param start Starting city.
     * @param end   Ending city.
     * @param path  Path.
     */
    public CityPath(Stad start, Stad end, Path path) {
        this(start, end, path.getPath());
    }

    /**
     * Get the starting city.
     *
     * @return Starting city.
     */
    public Stad getStartCity() {
        return this.start;
    }

    /**
     * Get the ending city.
     *
     * @return Ending city.
     */
    public Stad getEndCity() {
        return this.end;
    }

    /**
     * Check whether this path is for the given two cities.
     *
     * @param start Start city.
     * @param end   End city.
     * @return True if this path is for the given cities, false if not.
     */
    public boolean isFor(Stad start, Stad end) {
        return super.isFor(start.getCoordinaat(), end.getCoordinaat());
    }

    @Override
    public CityPath reverse() {
        return new CityPath(this.end, this.start, getPath().omgekeerd());
    }
}
