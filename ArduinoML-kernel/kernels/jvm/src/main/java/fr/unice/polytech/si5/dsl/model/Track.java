package fr.unice.polytech.si5.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private List<Section> sections = new ArrayList<>();
    private String name;
    private HashMap<Integer,Integer> instrumentsChannel = new HashMap<>();

    public Track(String name) {
        this.name = name;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void addInstrumentChannel(int instrumentType, int channel){
        instrumentsChannel.put(instrumentType,channel);
    }
}
