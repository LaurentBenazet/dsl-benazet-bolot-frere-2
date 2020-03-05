package fr.unice.polytech.si5.dsl.model;

import fr.unice.polytech.si5.dsl.converter.InstrumentToNumber;
import fr.unice.polytech.si5.dsl.converter.PercussionToNumber;

import java.util.ArrayList;
import java.util.List;


public class Instrument {
    private List<Bar> bars = new ArrayList<>();
    private int type;
    private String name;
    private boolean isPercussion = false;

    public Instrument(String name){
        this.name = name;
        if (PercussionToNumber.contains(name)) {
            isPercussion = true;
            type = PercussionToNumber.valueOf(name).getNumber();
        } else {
            type = InstrumentToNumber.valueOf(name).getNumber();
        }
    }

    public void addBar(Bar bar) {
        this.bars.add(bar);
    }

    public List<Bar> getBars() {
        return bars;
    }

    public int getType() {
        return type;
    }

    public boolean isPercussion(){
        return isPercussion;
    }
}
