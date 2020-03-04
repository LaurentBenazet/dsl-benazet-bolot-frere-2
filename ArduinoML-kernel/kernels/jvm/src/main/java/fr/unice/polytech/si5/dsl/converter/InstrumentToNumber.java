package fr.unice.polytech.si5.dsl.converter;

public enum InstrumentToNumber {
    Piano(0),
    Clavi(7),
    Celesta(8),
    Glockenspiel(9),
    Vibraphone(11),
    Marimba(12),
    Xylophone(13),
    Dulcimer(15),
    Organ(16),
    Accordion(21),
    Harmonica(22),
    Guitar(24),
    Bass(32),
    Violin(40),
    Viola(41),
    Cello(42),
    Contrabass(43),
    Harp(46),
    Timpani(47),
    Choir(52),
    Trumpet(56),
    Trombone(57),
    Tuba(58),
    Horn(60),
    Saxophone(64),
    Oboe(68),
    Bassoon(70),
    Clarinet(71),
    Piccolo(72),
    Flute(73),
    Whistle(78),
    Ocarina(79);

    private int number;

    InstrumentToNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }
}
