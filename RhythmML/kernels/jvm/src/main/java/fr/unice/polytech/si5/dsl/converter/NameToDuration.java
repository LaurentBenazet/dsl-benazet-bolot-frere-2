package fr.unice.polytech.si5.dsl.converter;

public enum NameToDuration {
    WHOLE_NOTE(4, "w", false),
    HALF_NOTE(2, "h", false),
    QUARTER_NOTE(1, "q", false),
    EIGHTH_NOTE(0.5, "e", false),
    TRIPLET(0.333, "t", false), //I know it's not accurate but it should be close enough for us
    SIXTEENTH_NOTE(0.25, "s", false),
    QUARTER_REST(0.25, ".", true),
    HALF_REST(0.5, "-", true),
    WHOLE_REST(1, "_", true);


    private double duration;
    private String regularName;
    private boolean isSilent;

    NameToDuration(double duration, String regularName, boolean isSilent) {
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
