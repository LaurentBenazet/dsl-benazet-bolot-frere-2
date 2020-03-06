package fr.unice.polytech.si5.dsl.generator;

import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.model.Bar;
import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Section;
import fr.unice.polytech.si5.dsl.model.Track;

import java.util.List;
import java.util.Map;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

    public ToWiring() {
        this.result = new StringBuffer();
    }

    private void w(String s) {
        result.append(String.format("%s\n", s));
    }

    @Override
    public void visit(App app) {
        Track track = app.getTrack();

        addUtilsMethods(track.getTempo());

        w("");
        w("    public static void main(String[] args) {");
        w("        Track track;");
        w("        Section section;");
        w("        Instrument instrument;");
        w("        Bar bar;");
        w("        int chan = 0;");
        w("");

        visit(track);

        w("        Tester midiPlayer = new Tester();"); //TODO CHANGE THAT
        w("        try {");
        w("            midiPlayer.playTrack(track);");
        w("        } catch (Exception e) {");
        w("            e.printStackTrace();");
        w("        }");
        w("    }");
        w("}");
    }

    @Override
    public void visit(Track track) {
        w("        track = new Track(\"" + track.getName() + "\");");
        w("");

        track.getInstruments().forEach(this::visit);

        w("");

        track.getSections().forEach(this::visit);
    }

    @Override
    public void visit(Instrument instrument) {
        w("        instrument = new Instrument(\"" + instrument.getName() + "\");");
        w("        instrument.setType(" + instrument.getType() + ");");
        w("        track.addInstrumentChannel(" + instrument.getType() + ", 9);");
        w("        track.addInstrument(instrument);");
        w("");

        w("        instrument = new Instrument(\"" + instrument.getName() + "\", \"" + instrument.getTypeName() + "\");");
        w("        if(!instrument.isPercussion()){");
        w("            track.addInstrumentChannel(instrument.getType(), chan);");
        w("            chan++;");
        w("        }");
        w("        track.addInstrument(instrument);");
    }

    @Override
    public void visit(Section section) {
        w("        section = new Section(\"" + section.getName() + "\");");
        w("        section.setBeats(" + section.getBeats() + ");");
        w("        section.setTempo(" + section.getTempo() + ");");

        Map<Instrument, List<Bar>> partition = section.getPartition();

        for (Instrument instr : partition.keySet()) {
            for (Bar bar : partition.get(instr)) {
                visit(instr, bar);
            }
        }

        w("        track.addSection(section);");
        w("");
    }

    @Override
    public void visit(Instrument instrument, Bar bar) {
        w("        bar = new Bar();");
        for (String note : bar.getNotes()) {
            w("        bar.addNote(\"" + note + "\");");
        }
        w("        instrument = track.findInstrument(\"" + instrument.getName() + "\");");
        w("        section.addBar(instrument, bar);");
        w("");
    }

    public void addUtilsMethods(int tempo) {

        w("package fr.unice.polytech.si5.dsl;");
        w("");
        w("import fr.unice.polytech.si5.dsl.model.Instrument;");
        w("import fr.unice.polytech.si5.dsl.model.Track;");
        w("import fr.unice.polytech.si5.dsl.model.*;");
        w("");
        w("import javax.sound.midi.*;");
        w("");
        w("import java.util.ArrayList;");
        w("import java.util.HashMap;");
        w("import java.util.List;");
        w("import java.util.Map;");
        w("");
        w("public class Tester {"); //TODO CHANGE THIS
        w("");
        w("    private int totalTime;");
        w("    private int resolution;");
        w("    private HashMap<Integer, Integer> channelsMapping;");
        w("    private Sequencer sequencer;");
        w("    private Sequence sequence;");
        w("    private javax.sound.midi.Track midiTrack;");
        w("");
        w("    public Tester() {"); //TODO CHANGE THAT
        w("        totalTime = 100;");
        w("        resolution = 200; // in slices per beat");
        w("        try {");
        w("            sequencer = MidiSystem.getSequencer();");
        w("            sequence = new Sequence(Sequence.PPQ, resolution);");
        w("            midiTrack = sequence.createTrack();");
        w("        } catch (Exception e) {");
        w("            e.printStackTrace();");
        w("        }");
        w("    }");
        w("");
        w("    private void bindChannel(int channel, int instrumentType) {");
        w("        try {");
        w("            ShortMessage instrumentChange = new ShortMessage();");
        w("            instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentType, 0);");
        w("            midiTrack.add(new MidiEvent(instrumentChange, 0));");
        w("        } catch (Exception e) {");
        w("            e.printStackTrace();");
        w("        }");
        w("    }");
        w("");
        w("    public void playTrack(Track track) throws Exception {");
        w("        //probably need to change that somehow");
        w("        channelsMapping = track.getInstrumentsChannel();");
        w("");
        w("        for (Map.Entry<Integer, Integer> entry : channelsMapping.entrySet()) {");
        w("            bindChannel(entry.getValue(), entry.getKey());");
        w("        }");
        w("");
        w("        for (Section s : track.getSections()) {");
        w("            playSection(s);");
        w("            //add time passed");
        w("        }");
        w("");
        w("        int tempo = " + tempo + ";");
        w("");
        w("        NoteUtils.analyzeSequence(sequence);");
        w("");
        w("        sequencer.open();");
        w("        sequencer.setSequence(sequence);");
        w("        sequencer.setTempoInBPM(tempo);");
        w("        sequencer.start();");
        w("        int x = totalTime / 200;");
        w("        //int durationOfTheTrackMS = nbBar * nbBeatPerBar * 60000 / tempo;");
        w("        int durationOfTheTrackMS = (x + 1) * 60000 / tempo;");
        w("        System.out.println(\"sleeping \" + (durationOfTheTrackMS) + \"ms\");");
        w("        Thread.sleep(durationOfTheTrackMS);");
        w("        System.out.println(\"stop sleeping\");");
        w("        sequencer.stop();");
        w("        sequencer.close();");
        w("    }");
        w("");
        w("    private void playSection(Section section) {");
        w("        int maxTime = 0;");
        w("        Map<Instrument, List<Bar>> partition = section.getPartition();");
        w("        ");
        w("        for (Instrument instr : partition.keySet()) {");
        w("            int t = playInstrument(instr, partition.get(instr));");
        w("            ");
        w("            if (t > maxTime) {");
        w("                maxTime = t;");
        w("            }");
        w("        }");
        w("        totalTime += maxTime;");
        w("    }");
        w("");
        w("    private int playInstrument(Instrument instrument, List<Bar> bars) {");
        w("        int time = 0;");
        w("        for (Bar bar : bars) {");
        w("            int t = playBar(bar, instrument, time + totalTime);");
        w("            time = time + t;");
        w("        }");
        w("        return time;");
        w("    }");
        w("");
        w("    private int playBar(Bar bar, Instrument instrument, int timePassed) {");
        w("        int time = 0;");
        w("        for (Note note : bar.getRealnotes()) {");
        w("            int t = playNote(note, time + timePassed, instrument);");
        w("            time = time + t;");
        w("        }");
        w("        return time;");
        w("    }");
        w("");
        w("    private int playNote(Note note, int timeBeforeNote, Instrument instrument) {");
        w("        int noteValue = note.getNoteOffset();");
        w("        int channel = channelsMapping.get(instrument.getType());");
        w("        if (channel == 9) {");
        w("            noteValue = instrument.getType();");
        w("        }");
        w("        NoteUtils.addNote(midiTrack, channel, noteValue, timeBeforeNote, note.getVelocity(), note.getDuration());");
        w("        return (int) (note.getDuration() * resolution);");
        w("    }");
    }
}
