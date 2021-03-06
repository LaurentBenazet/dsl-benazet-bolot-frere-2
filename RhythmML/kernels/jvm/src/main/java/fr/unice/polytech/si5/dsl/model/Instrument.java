package fr.unice.polytech.si5.dsl.model;

import fr.unice.polytech.si5.dsl.converter.InstrumentToNumber;
import fr.unice.polytech.si5.dsl.converter.PercussionToNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Instrument {
    private int type;
    private String typeName;
    private String name;
    private boolean isPercussion = false;

    public Instrument(String name) {
        this.name = name;
    }

    public Instrument(String name, String typeName) {
        this.name = name;
        this.setType(typeName);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(String type) {
        this.typeName = type;

        if (PercussionToNumber.contains(type)) {
            isPercussion = true;
            this.type = PercussionToNumber.valueOf(type).getNumber();
        } else {
            this.type = InstrumentToNumber.valueOf(type).getNumber();
        }
    }

    @Override
    public String toString() {
        return "Instrument{" +
                ", type=" + type +
                ", name='" + name + "'" +
                ", isPercussion='" + isPercussion + "'" +
                '}';
    }
}
