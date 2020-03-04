package fr.unice.polytech.si5.dsl.converter;

public enum PercussionToNumber {
    Acoustic_Bass_Drum(34),
    Bass_Drum(35),
    Side_Stick(36),
    Acoustic_Snare(37),
    Hand_Clap(38),
    Electric_Snare(39),
    Low_Floor_Tom(40),
    Closed_Hi_Hat(41),
    High_Floor_Tom(42),
    Pedal_Hi_Hat(43),
    Low_Tom(44),
    Open_Hi_Hat(45),
    Low_Mid_Tom(46),
    Hi_Mid_Tom(47),
    Crash_Cymbal(48),
    High_Tom(49),
    Ride_Cymbal(50),
    Tambourine(53),
    Cowbell(55);

    private int number;

    PercussionToNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }
}
