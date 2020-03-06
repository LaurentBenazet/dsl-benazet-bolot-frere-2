package fr.unice.polytech.si5.dsl.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si5.dsl.converter.NameToDuration.getTiming;
import static fr.unice.polytech.si5.dsl.converter.NameToDuration.isSilent;
import static fr.unice.polytech.si5.dsl.converter.NoteToTone.getTone;

@Getter
@Setter
public class Bar {
    private List<String> notes = new ArrayList<>();
    private List<Note> realnotes = new ArrayList<>();

    public void addNote(String note) {
        notes.add(note);

        String[] in = note.split("\\(");
        Note n = new Note(getTiming(in[0]));

        if (isSilent(in[0])) {
            n.setVelocity(0);
        }

        if (in.length > 1) {
            if(in[1].split("\\)")[0].split("\\+").length>1){
                String[] test = in[1].split("\\)")[0].split("\\+");
                int octave = Integer.parseInt(test[1]);
                n.setNoteOffset(getTone(test[0])+(12*octave));
            } else if(in[1].split("\\)")[0].split("-").length>1){
                String[] test = in[1].split("\\)")[0].split("-");
                int octave = Integer.parseInt(test[1]);
                n.setNoteOffset(getTone(test[0])-(12*octave));
            } else {
                n.setNoteOffset(getTone(in[1].split("\\)")[0]));
            }
        }

        realnotes.add(n);
    }

    public void addNotes(List<String> notes) {
        notes.forEach(this::addNote);
    }
}
