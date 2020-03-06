package fr.unice.polytech.si5.dsl.converter;

public enum NameToDuration {
    Whole_Note(4,"w",false),
    Half_Note(2,"h",false),
    Quarter_Note(1,"q",false),
    Eighth_Note(0.5,"e",false),
    Triplet(0.333,"t",false), //I know it's not accurate but it should be close enough for us
    Sixteenth_Note(0.25,"s",false),
    Quarter_Rest(0.25,".",true),
    Half_Rest(0.5,"-",true),
    Whole_Rest(1,"_",true);


    private double duration;
    private String regularName;
    private boolean isSilent;

    NameToDuration(double duration,String regularName,boolean isSilent) {
        this.duration = duration;
        this.regularName = regularName;
        this.isSilent = isSilent;
    }

    public static double getTiming(String test) {
        for (NameToDuration n : NameToDuration.values()) {
            if (n.regularName.equals(test)) {
                return n.duration;
            }
        }
        return -1;
    }

    public static boolean isSilent(String test) {
        for (NameToDuration n : NameToDuration.values()) {
            if (n.regularName.equals(test)) {
                return n.isSilent;
            }
        }
        return false;
    }

    public double getDuration() {
        return this.duration;
    }
}
