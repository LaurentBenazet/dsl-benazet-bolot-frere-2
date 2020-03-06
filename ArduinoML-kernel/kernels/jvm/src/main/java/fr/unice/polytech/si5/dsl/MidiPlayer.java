package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Track;
import fr.unice.polytech.si5.dsl.model.*;

import javax.sound.midi.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidiPlayer {

    private int totalTime;
    private int resolution;
    private HashMap<Integer, Integer> channelsMapping;
    private Sequencer sequencer;
    private Sequence sequence;
    private javax.sound.midi.Track midiTrack;

    public MidiPlayer() {

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

    public static void main(String[] args) {

        Track track;
        Section section;
        Instrument instrument;
        Bar bar;

        track = new Track("Test track");

        instrument = new Instrument("bd","Acoustic_Bass_Drum");
        instrument.setType("Acoustic_Bass_Drum");
        track.addInstrumentChannel(instrument.getType(), 9);

        instrument = new Instrument("ch","Closed_Hi_Hat");
        instrument.setType("Closed_Hi_Hat");
        track.addInstrumentChannel(instrument.getType(), 9);

        instrument = new Instrument("bass","Bass");
        instrument.setType("Bass");
        track.addInstrumentChannel(instrument.getType(), 9);

        section = new Section("intro");
        section.setBeats(4);
        section.setTempo(160);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("Acoustic_Bass_Drum");
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
        instrument = track.findInstrument("Acoustic_Bass_Drum");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("q");
        bar.addNote("q");
        bar.addNote("q");
        instrument = track.findInstrument("Closed_Hi_Hat");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("q");
        bar.addNote("q");
        bar.addNote("q");
        bar.addNote("q");
        instrument = track.findInstrument("Closed_Hi_Hat");
        section.addBar(instrument, bar);

        track.addSection(section);

        section = new Section("intro2");
        section.setBeats(4);
        section.setTempo(160);

        bar = new Bar();
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        bar.addNote("e");
        instrument = track.findInstrument("Bass");
        section.addBar(instrument, bar);

        bar = new Bar();
        bar.addNote("e(A)");
        bar.addNote("e(B)");
        bar.addNote("e(C)");
        bar.addNote("e(D)");
        bar.addNote("e(E)");
        bar.addNote("e(F)");
        bar.addNote("e(G)");
        bar.addNote("e(G)#");
        instrument = track.findInstrument("Bass");
        section.addBar(instrument, bar);

        track.addSection(section);

        MidiPlayer midiPlayer = new MidiPlayer();
        try {
            midiPlayer.playTrack(track);
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
        }

        int tempo = 60;

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
}
