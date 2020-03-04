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
    private int velocity = 50;
    private double duration;
    private double offset;
    private int noteOffset;

    public Note(double duration){
        this.duration = duration;
    }
    public Note(double duration,int noteOffset){
        this.duration = duration;
        this.noteOffset = noteOffset;
    }
}
