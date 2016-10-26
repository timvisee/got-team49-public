package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;

public class Path  {

    /**
     * Starting city.
     */
    private Stad start;

    /**
     * Ending city.
     */
    private Stad end;

    /**
     * Length.
     */
    private int length;

    /**
     * Constructor.
     *
     * @param start Starting city.
     * @param end Ending city.
     * @param length Length.
     */
    public Path(Stad start, Stad end, int length){
        this.start = start;
        this.end = end;
        this.length = length;
    }

    /**
     * Get the starting coordinate.
     *
     * @return Starting coordinate.
     */
    public Coordinaat getStart(){
        return this.start.getCoordinaat();
    }

    /**
     * Get the ending coordinate.
     *
     * @return Ending coordinate.
     */
    public Coordinaat getEnd(){
        return this.end.getCoordinaat();
    }

    /**
     * Get the starting city.
     *
     * @return Starting city.
     */
    public Stad getStartCity(){
        return this.start;
    }

    /**
     * Get the ending city.
     *
     * @return Ending city.
     */
    public Stad getEndCity(){
        return this.end;
    }

    /**
     * Get the length.
     *
     * @return Length.
     */
    public int getLength(){
        return length;
    }
}
