package fr.unice.polytech.si5.dsl.converter;

public enum NoteToTone {
    A(-3, "A"),
    A_SHARP(-2, "A#"),
    B(-1, "B"),
    C(0, "C"),
    C_SHARP(1, "C#"),
    D(2, "D"),
    D_SHARP(3, "D#"),
    E(4, "E"),
    F(5, "F"),
    F_SHARP(6, "F#"),
    G(7, "G"),
    G_SHARP(8, "G#");

    private int tone;
    private String regularName;

    NoteToTone(int tone, String regularName) {
        this.tone = tone;
        this.regularName = regularName;
    }

    public static int getTone(String test) {
        for (NoteToTone n : NoteToTone.values()) {
            if (n.regularName.equals(test)) {
                return n.tone;
            }
        }
        return 0;
    }
}
