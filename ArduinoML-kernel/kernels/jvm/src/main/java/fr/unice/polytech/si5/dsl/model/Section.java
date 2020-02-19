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
public class Section {
    private String name;
    private int tempo;
    private int beats;
    private List<Instrument> instruments = new ArrayList<>();

    public Section(String name) {
        this.name = name;
    }

    public void addInstrument(Instrument instrument) {
        this.instruments.add(instrument);
    }
}
