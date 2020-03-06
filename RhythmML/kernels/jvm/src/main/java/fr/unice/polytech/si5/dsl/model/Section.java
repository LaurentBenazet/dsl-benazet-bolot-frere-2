package fr.unice.polytech.si5.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    private String name;
    private int beats;
    private Map<Instrument, List<Bar>> partition = new HashMap<>();

    public Section(String name) {
        this.name = name;
    }

    public void addBar(Instrument instrument, Bar bar) {
        if (partition.containsKey(instrument)) {
            partition.get(instrument).add(bar);
        } else {
            List<Bar> bars = new ArrayList<Bar>() {{
                add(bar);
            }};
            partition.put(instrument, bars);
        }
    }
}
