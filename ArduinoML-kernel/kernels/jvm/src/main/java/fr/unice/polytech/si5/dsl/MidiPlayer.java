package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.model.*;
import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Track;
import fr.unice.polytech.si5.dsl.NoteUtils;

import javax.sound.midi.*;

import java.util.ArrayList;
import java.util.List;

public class MidiPlayer {

    private int totalTime;
    private int resolution;
    private Sequencer sequencer;
    private Sequence sequence;
    private javax.sound.midi.Track midiTrack;

    public MidiPlayer(){
        totalTime = 100;
        resolution = 200; // in slices per beat
        try {
            sequencer = MidiSystem.getSequencer();
            sequence = new Sequence(Sequence.PPQ, resolution);
            midiTrack = sequence.createTrack();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){


        Track track = new Track("Test track");
        Section section = new Section("intro");
        section.setBeats(4);
        section.setTempo(160);
        Instrument instrument = new Instrument("hihat");
        instrument.setType(42);
        List<String> x = new ArrayList<>();
        for (int i=0;i<8;i++){
            x.add("e");
        }
        Bar bar = new Bar(x);
        instrument.addBar(bar);
        x = new ArrayList<>();
        x.add("q");
        x.add("q");
        x.add("q");
        x.add("q");
        bar = new Bar(x);
        instrument.addBar(bar);
        section.addInstrument(instrument);
        track.addSection(section);

        MidiPlayer midiPlayer = new MidiPlayer();
        try {
            midiPlayer.playTrack(track);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void playTrack(Track track) throws Exception{

        for (Section s : track.getSections()) {
            playSection(s);
            //add time passed
        }

        int tempo = 30;

        NoteUtils.analyzeSequence(sequence);

        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(tempo);
        sequencer.start();
        //int durationOfTheTrackMS = nbBar * nbBeatPerBar * 60000 / tempo;
        int durationOfTheTrackMS = (8+1) * 60000 / tempo;
        System.out.println("sleeping " + (durationOfTheTrackMS) + "ms");
        Thread.sleep(durationOfTheTrackMS);
        System.out.println("stop sleeping");
        sequencer.stop();
        sequencer.close();
    }

    private void playSection(Section section){
        int maxTime = 0;
        for (Instrument i : section.getInstruments()) {
            int t = playInstrument(i);
            if(t>maxTime){
                maxTime = t;
            }
        }
        totalTime = totalTime + maxTime;
    }

    private int playInstrument(Instrument instrument){
        int time = 0;
        int instr = instrument.getType();
        for (Bar b : instrument.getBars()) {
            int t = playBar(b,instr,time+totalTime);
            time = time + t;
        }
        return time;
    }

    private int playBar(Bar bar,int instrument,int timePassed){
        int time = 0;
        for (Note n : bar.getRealnotes()) {
            int t = playNote(n,time+timePassed,instrument);
            time = time + t;
        }
        return time;
    }

    private int playNote(Note note,int timeBeforeNote,int instrument){
        NoteUtils.addNote(midiTrack,instrument,timeBeforeNote,note.getVelocity());
        return (int) (note.getDuration()*resolution);
    }
}
