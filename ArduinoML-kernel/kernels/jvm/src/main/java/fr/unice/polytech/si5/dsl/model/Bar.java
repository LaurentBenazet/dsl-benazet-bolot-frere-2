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
public class Bar {
    private List<String> notes = new ArrayList<>();
    private List<Note> realnotes = new ArrayList<>();

    public Bar(List<String> notes){
        this.notes = notes;
        //temporary test
        for (String x : notes) {
            if(x.equals("e")){
                realnotes.add(new Note(0.5));
            } else {
                realnotes.add(new Note(1));
            }
        }
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public void addNote(Note note) {
        realnotes.add(note);
    }
}
