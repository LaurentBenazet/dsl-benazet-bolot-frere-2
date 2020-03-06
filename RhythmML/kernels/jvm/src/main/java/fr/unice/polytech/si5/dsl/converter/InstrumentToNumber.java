package fr.unice.polytech.si5.dsl.converter;

public enum InstrumentToNumber {
    PIANO(0),
    CLAVI(7),
    CELESTA(8),
    GLOCKENSPIEL(9),
    VIBRAPHONE(11),
    MARIMBA(12),
    XYLOPHONE(13),
    DULCIMER(15),
    ORGAN(16),
    ACCORDION(21),
    HARMONICA(22),
    GUITAR(24),
    BASS(32),
    VIOLIN(40),
    VIOLA(41),
    CELLO(42),
    CONTRABASS(43),
    HARP(46),
    TIMPANI(47),
    CHOIR(52),
    TRUMPET(56),
    TROMBONE(57),
    TUBA(58),
    HORN(60),
    SAXOPHONE(64),
    OBOE(68),
    BASSOON(70),
    CLARINET(71),
    PICCOLO(72),
    FLUTE(73),
    WHISTLE(78),
    OCARINA(79);

    private int number;

    InstrumentToNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }
}
