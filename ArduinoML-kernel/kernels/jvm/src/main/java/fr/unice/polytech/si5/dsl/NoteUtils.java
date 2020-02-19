package fr.unice.polytech.si5.dsl;

import javax.sound.midi.*;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

public class NoteUtils {


    private static void createEvent(Track track, int type, int chan, int instr, long tick, int velocity) {
        ShortMessage message = new ShortMessage();

        try {
            message.setMessage(type, chan, instr, velocity);
            MidiEvent event = new MidiEvent(message, tick);
            track.add(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void addNote(Track track, int instr, long tick, int velocity) {
        final int NOTEON = 144;
        final int NOTEOFF = 128;

        createEvent(track, NOTEON, 9, instr, tick, velocity);
        createEvent(track, NOTEOFF, 9, instr, tick + 1, velocity);
    }


    public static void analyzeSequence(Sequence sequence) {
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        //String noteName = NOTE_NAMES[note];
                        String noteName = "coucou";
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12) - 1;
                        int note = key % 12;
                        //String noteName = NOTE_NAMES[note];
                        String noteName = "coucou";
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }            System.out.println();        }    }

}
