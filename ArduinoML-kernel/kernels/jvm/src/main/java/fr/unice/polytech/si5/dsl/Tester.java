package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Track;
import fr.unice.polytech.si5.dsl.model.*;

import javax.sound.midi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tester {

    private int totalTime;
    private int resolution;
    private HashMap<Integer, Integer> channelsMapping;
    private Sequencer sequencer;
    private Sequence sequence;
    private javax.sound.midi.Track midiTrack;

    public Tester() {
        totalTime = 100;
        resolution = 200; // in slices per beat
        try {
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, resolution);
            midiTrack = sequence.createTrack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindChannel(int channel, int instrumentType) {
        try {
            ShortMessage instrumentChange = new ShortMessage();
            instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentType, 0);
            midiTrack.add(new MidiEvent(instrumentChange, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playTrack(Track track) throws Exception {
        //probably need to change that somehow
        channelsMapping = track.getInstrumentsChannel();

        for (Map.Entry<Integer, Integer> entry : channelsMapping.entrySet()) {
            bindChannel(entry.getValue(), entry.getKey());
        }

        for (Section s : track.getSections()) {
            playSection(s);
            //add time passed
        }

        int tempo = 118;

        NoteUtils.analyzeSequence(sequence);

        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(tempo);
        sequencer.start();
        int x = totalTime / 200;
        //int durationOfTheTrackMS = nbBar * nbBeatPerBar * 60000 / tempo;
        int durationOfTheTrackMS = (x + 1) * 60000 / tempo;
        System.out.println("sleeping " + (durationOfTheTrackMS) + "ms");
        Thread.sleep(durationOfTheTrackMS);
        System.out.println("stop sleeping");
        sequencer.stop();
        sequencer.close();
    }

    private void playSection(Section section) {
        int maxTime = 0;
        Map<Instrument, List<Bar>> partition = section.getPartition();

        for (Instrument instr : partition.keySet()) {
            int t = playInstrument(instr, partition.get(instr));

            if (t > maxTime) {
                maxTime = t;
            }
        }
        totalTime += maxTime;
    }

    private int playInstrument(Instrument instrument, List<Bar> bars) {
        int time = 0;
        for (Bar bar : bars) {
            int t = playBar(bar, instrument, time + totalTime);
            time = time + t;
        }
        return time;
    }

    private int playBar(Bar bar, Instrument instrument, int timePassed) {
        int time = 0;
        for (Note note : bar.getRealnotes()) {
            int t = playNote(note, time + timePassed, instrument);
            time = time + t;
        }
        return time;
    }

    private int playNote(Note note, int timeBeforeNote, Instrument instrument) {
        int noteValue = note.getNoteOffset();
        int channel = channelsMapping.get(instrument.getType());
        if (channel == 9) {
            noteValue = instrument.getType();
        }
        NoteUtils.addNote(midiTrack, channel, noteValue, timeBeforeNote, note.getVelocity(), note.getDuration());
        return (int) (note.getDuration() * resolution);
    }

    public static void main(String[] args) {
        Track track;
        Section section;
        Instrument instrument;
        Bar bar;
        int chan = 0;

        track = new Track("Billie Jean with piano");

        instrument = new Instrument("ch");
        instrument.setType(42);
        track.addInstrumentChannel(42, 9);
        track.addInstrument(instrument);

        instrument = new Instrument("ch", "Closed_Hi_Hat");
        if(!instrument.isPercussion()){
            track.addInstrumentChannel(instrument.getType(), chan);
            chan++;
        }
        track.addInstrument(instrument);
        instrument = new Instrument("sd");
        instrument.setType(38);
        track.addInstrumentChannel(38, 9);
        track.addInstrument(instrument);

        instrument = new Instrument("sd", "Acoustic_Snare");
        if(!instrument.isPercussion()){
            track.addInstrumentChannel(instrument.getType(), chan);
            chan++;
        }
        track.addInstrument(instrument);
        instrument = new Instrument("bd");
        instrument.setType(36);
        track.addInstrumentChannel(36, 9);
        track.addInstrument(instrument);

        instrument = new Instrument("bd", "Bass_Drum");
        if(!instrument.isPercussion()){
            track.addInstrumentChannel(instrument.getType(), chan);
            chan++;
        }
        track.addInstrument(instrument);
        instrument = new Instrument("piano");
        instrument.setType(0);
        track.addInstrumentChannel(0, 9);
        track.addInstrument(instrument);

        instrument = new Instrument("piano", "Piano");
        if(!instrument.isPercussion()){
            track.addInstrumentChannel(instrument.getType(), chan);
            chan++;
        }
        track.addInstrument(instrument);

        section = new Section("No piano");
        section.setBeats(4);
        section.setTempo(0);
        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        track.addSection(section);

        section = new Section("Main");
        section.setBeats(4);
        section.setTempo(0);
        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("q(D)");
        bar.addNote("s(C)");
        bar.addNote("s(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("-");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("q(D)");
        bar.addNote("q(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C#)");
        bar.addNote("e(E)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(E)");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("e(D)");
        bar.addNote("-");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        track.addSection(section);

        section = new Section("Transition");
        section.setBeats(4);
        section.setTempo(0);
        bar = new Bar();
        bar.addNote("-");
        bar.addNote("e(E)");
        bar.addNote("e(D)");
        bar.addNote("q(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C#)");
        bar.addNote("e(E)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(E)");
        bar.addNote("e(E)");
        bar.addNote("_");
        bar.addNote("-");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("e(D)");
        bar.addNote("-");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        track.addSection(section);

        section = new Section("Main");
        section.setBeats(4);
        section.setTempo(0);
        bar = new Bar();
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("q(D)");
        bar.addNote("s(C)");
        bar.addNote("s(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("-");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("-");
        bar.addNote("q(D)");
        bar.addNote("q(C)");
        bar.addNote("e(D)");
        bar.addNote("e(C#)");
        bar.addNote("e(E)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(E)");
        bar.addNote("_");
        bar.addNote("_");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        bar.addNote("q(D)");
        bar.addNote("s(E)");
        bar.addNote("s(F)");
        bar.addNote("e(D)");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q(D)");
        bar.addNote("e(D)");
        bar.addNote("-");
        bar.addNote("_");
        bar.addNote("_");
        instrument = track.findInstrument("piano");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        instrument = track.findInstrument("sd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("ch");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("_");
        bar.addNote("q");
        bar.addNote("_");
        instrument = track.findInstrument("bd");
        section.addBar(instrument, bar);

        track.addSection(section);

        Tester midiPlayer = new Tester();
        try {
            midiPlayer.playTrack(track);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
