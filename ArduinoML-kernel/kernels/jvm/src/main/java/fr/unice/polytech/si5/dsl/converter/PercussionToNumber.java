package fr.unice.polytech.si5.dsl.converter;

import java.util.Arrays;

public enum PercussionToNumber {
    Acoustic_Bass_Drum(35),
    Bass_Drum(36),
    Side_Stick(37),
    Acoustic_Snare(38),
    Hand_Clap(39),
    Electric_Snare(40),
    Low_Floor_Tom(41),
    Closed_Hi_Hat(42),
    High_Floor_Tom(43),
    Pedal_Hi_Hat(44),
    Low_Tom(45),
    Open_Hi_Hat(46),
    Low_Mid_Tom(47),
    Hi_Mid_Tom(48),
    Crash_Cymbal(49),
    High_Tom(50),
    Ride_Cymbal(51),
    Tambourine(54),
    Cowbell(56);

    private int number;

    PercussionToNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public static boolean contains(String test) {
        return Arrays
                .stream(PercussionToNumber.values())
                .anyMatch(c -> c.name().equals(test));
    }
}
