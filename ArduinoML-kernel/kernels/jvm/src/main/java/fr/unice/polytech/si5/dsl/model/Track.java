package fr.unice.polytech.si5.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private Collection<Instrument> instruments = new ArrayList<>();
    private HashMap<String,Section> sections = new HashMap<>();
    private String name;
    private HashMap<Integer, Integer> instrumentsChannel = new HashMap<>();
    private List<String> sectionOrder = new ArrayList<>();

    public Track(String name) {
        this.name = name;
    }

    public void addSection(Section section) {
        sections.put(section.getName(),section);
    }

    public void addInstrumentChannel(int instrumentType, int channel) {
        instrumentsChannel.put(instrumentType, channel);
    }

    public Section getCorrespondingSection(String name){
        return sections.get(name);
    }

    public void addInstrument(Instrument instrument){
        instruments.add(instrument);
    }

    public Instrument findInstrument(String name) {
        return instruments
                .stream()
                .filter(instr -> instr.getName().equals(name))
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return "Track{" +
                "sections=" + sections +
                ", name='" + name + '\'' +
                ", instrumentsChannel=" + instrumentsChannel +
                '}';
    }
}
