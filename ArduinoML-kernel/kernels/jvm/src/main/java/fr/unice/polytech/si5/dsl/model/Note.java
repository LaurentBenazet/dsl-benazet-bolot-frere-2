package fr.unice.polytech.si5.dsl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    private double velocity;
    private double duration;
    private double offset;
    private Instrument instrument;
}
