package fr.unice.polytech.si5.dsl.behavioral;

import fr.unice.polytech.si5.dsl.structural.SIGNAL;
import fr.unice.polytech.si5.dsl.structural.Sensor;

public class Condition {

    private Sensor sensor;
    private SIGNAL value;

    public Condition(Sensor sensor, SIGNAL value) {
        this.sensor=sensor;
        this.value = value;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }
}
