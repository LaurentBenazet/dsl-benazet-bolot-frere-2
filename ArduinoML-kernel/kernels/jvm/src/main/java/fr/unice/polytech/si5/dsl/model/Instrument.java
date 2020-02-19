package fr.unice.polytech.si5.dsl.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instrument {
    private List<Bar> bars = new ArrayList<>();
    private String type;
    private String name;

    public Instrument(String name) {
        this.name = name;
    }

    public void addBar(Bar bar) {
        this.bars.add(bar);
    }
}
