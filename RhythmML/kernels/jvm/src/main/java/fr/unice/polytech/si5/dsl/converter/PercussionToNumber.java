package fr.unice.polytech.si5.dsl.converter;

import java.util.Arrays;

public enum PercussionToNumber {
    ACOUSTIC_BASS_DRUM(35),
    BASS_DRUM(36),
    SIDE_STICK(37),
    ACOUSTIC_SNARE(38),
    HAND_CLAP(39),
    ELECTRIC_SNARE(40),
    LOW_FLOOR_TOM(41),
    CLOSED_HI_HAT(42),
    HIGH_FLOOR_TOM(43),
    PEDAL_HI_HAT(44),
    LOW_TOM(45),
    OPEN_HI_HAT(46),
    LOW_MID_TOM(47),
    HI_MID_TOM(48),
    CRASH_CYMBAL(49),
    HIGH_TOM(50),
    RIDE_CYMBAL(51),
    TAMBOURINE(54),
    COWBELL(56);

    private int number;

    PercussionToNumber(int number) {
        this.number = number;
    }

    public static boolean contains(String test) {
        return Arrays
                .stream(PercussionToNumber.values())
                .anyMatch(c -> c.name().equals(test));
    }

    public int getNumber() {
        return this.number;
    }
}
