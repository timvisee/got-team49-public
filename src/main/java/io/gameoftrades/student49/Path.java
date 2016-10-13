package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Stad;

public class Path  {

    private Stad start;
    private Stad end;
    private int length;

    public Path(Stad start, Stad end, int length){
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public Coordinaat getStart(){
        return start.getCoordinaat();
    }

    public Coordinaat getEnd(){
        return end.getCoordinaat();
    }

    public Stad getStartCity(){
        return start;
    }

    public Stad getEndCity(){
        return end;
    }


    public int getLength(){
        return length;
    }


}
