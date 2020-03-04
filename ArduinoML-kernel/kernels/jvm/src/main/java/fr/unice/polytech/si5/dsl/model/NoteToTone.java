package fr.unice.polytech.si5.dsl.model;

public enum NoteToTone {
    A(-3),
    A_Sharp(-2),
    B(-1),
    C(0),
    C_Sharp(1),
    D(2),
    D_Sharp(3),
    E(4),
    F(5),
    F_Sharp(6),
    G(7),
    G_Sharp(8);

    private int tone;

    NoteToTone(int tone) {
        this.tone = tone;
    }

    public int getTone() {
        return this.tone;
    }
}
