package io.gameoftrades.student49;

import io.gameoftrades.model.kaart.Coordinaat;

public class Path  {

    private Coordinaat start;
    private Coordinaat end;
    private int length;

    public Path(Coordinaat start, Coordinaat end, int length){
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public Coordinaat getStart(){
        return start;
    }

    public Coordinaat getEnd(){
        return end;
    }

    public int getLength(){
        return length;
    }


}
