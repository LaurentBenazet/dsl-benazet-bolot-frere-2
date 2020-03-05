package fr.unice.polytech.si5.dsl.model;

import fr.unice.polytech.si5.dsl.converter.NameToDuration;
import fr.unice.polytech.si5.dsl.converter.NoteToTone;
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
            String[] in = x.split("\\(");
            Note n = new Note(NameToDuration.getTiming(in[0]));
            if(NameToDuration.isSilent(in[0])){
                n.setVelocity(0);
            }
            if(in.length>1){
                n.setNoteOffset(NoteToTone.getTone(in[1].split("\\)")[0]));
            }
            realnotes.add(n);
        }
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public void addNote(Note note) {
        realnotes.add(note);
    }
}
