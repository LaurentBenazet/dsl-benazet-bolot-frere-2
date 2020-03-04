package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.model.*;
import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Track;
import fr.unice.polytech.si5.dsl.NoteUtils;

import javax.sound.midi.*;

import java.util.*;

public class MidiPlayer {

    private int totalTime;
    private int resolution;
    private HashMap<Integer,Integer> channelsMapping;
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
        
        Instrument instrument = new Instrument("ClosedHihat");
        track.addInstrumentChannel(35,9);//for percussion channel is 9 and type will be converted to note
        instrument.setType(35);
        List<String> x = new ArrayList<>();
        for (int i=0;i<8;i++){
            x.add("e");
        }
        Bar bar = new Bar(x);
        instrument.addBar(bar);
        x = new ArrayList<>();
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        bar = new Bar(x);
        instrument.addBar(bar);
        section.addInstrument(instrument);

        instrument = new Instrument("DrumAcoustic");
        track.addInstrumentChannel(42,9);//for percussion channel is 9 and type will be converted to note
        instrument.setType(42);
        x = new ArrayList<>();
        x.add("q");
        x.add("q");
        x.add("q");
        x.add("q");
        bar = new Bar(x);
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


        section = new Section("intro2");
        section.setBeats(4);
        section.setTempo(160);

        instrument = new Instrument("ElectricTruc");
        track.addInstrumentChannel(40,9);//for percussion channel is 9 and type will be converted to note
        instrument.setType(40);
        x = new ArrayList<>();
        for (int i=0;i<8;i++){
            x.add("e");
        }
        bar = new Bar(x);
        instrument.addBar(bar);
        x = new ArrayList<>();
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
        x.add("e");
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
    
    private void bindChannel(int channel,int instrumentType){
        try
        {
            ShortMessage instrumentChange = new ShortMessage();
            instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrumentType, 0);
            midiTrack.add(new MidiEvent(instrumentChange,0));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void playTrack(Track track) throws Exception{
        //probably need to change that somehow
        channelsMapping = track.getInstrumentsChannel();

        for (Map.Entry<Integer, Integer> entry : channelsMapping.entrySet()) {
            bindChannel(entry.getValue(),entry.getKey());
        }

        for (Section s : track.getSections()) {
            playSection(s);
            //add time passed
        }

        int tempo = 60;

        NoteUtils.analyzeSequence(sequence);

        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM(tempo);
        sequencer.start();
        //int durationOfTheTrackMS = nbBar * nbBeatPerBar * 60000 / tempo;
        int durationOfTheTrackMS = (12+1) * 60000 / tempo;
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
        for (Bar b : instrument.getBars()) {
            int t = playBar(b,instrument,time+totalTime);
            time = time + t;
        }
        return time;
    }

    private int playBar(Bar bar,Instrument instrument,int timePassed){
        int time = 0;
        for (Note n : bar.getRealnotes()) {
            int t = playNote(n,time+timePassed,instrument);
            time = time + t;
        }
        return time;
    }

    private int playNote(Note note,int timeBeforeNote,Instrument instrument){
        int noteValue = note.getNoteOffset();
        int channel = channelsMapping.get(instrument.getType());
        if(channel == 9){
            noteValue = instrument.getType();
        }
        NoteUtils.addNote(midiTrack,channel,noteValue,timeBeforeNote,note.getVelocity(),note.getDuration());
        return (int) (note.getDuration()*resolution);
    }
}
