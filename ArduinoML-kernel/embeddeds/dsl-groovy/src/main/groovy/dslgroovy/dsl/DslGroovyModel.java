package dslgroovy.dsl;

import groovy.lang.Binding;
import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.behavioral.*;
import fr.unice.polytech.si5.dsl.generator.ToWiring;
import fr.unice.polytech.si5.dsl.generator.Visitor;
import fr.unice.polytech.si5.dsl.structural.Actuator;
import fr.unice.polytech.si5.dsl.structural.Brick;
import fr.unice.polytech.si5.dsl.structural.SIGNAL;
import fr.unice.polytech.si5.dsl.structural.Sensor;

import java.util.ArrayList;
import java.util.List;

public class DslGroovyModel {
    private List<Brick> bricks;
    private List<State> states;
    private State initialState;

    private Binding binding;

    public DslGroovyModel(Binding binding) {
        this.bricks = new ArrayList<>();
        this.states = new ArrayList<>();
        this.binding = binding;

        this.binding.setVariable("error_transitions", new ArrayList<Transition>());
    }

    public void setFrequency(double frequency) {
        this.binding.setVariable("frequency", frequency);
    }

    public void createSensor(String name, Integer pinNumber) {
        Sensor sensor = new Sensor();
        sensor.setName(name);
        sensor.setPin(pinNumber);
        this.bricks.add(sensor);
        this.binding.setVariable(name, sensor);
    }

    public void createActuator(String name, Integer pinNumber) {
        Actuator actuator = new Actuator();
        actuator.setName(name);
        actuator.setPin(pinNumber);
        this.bricks.add(actuator);
        this.binding.setVariable(name, actuator);
    }

    public void addErrorTransition(Transition transition){
        ((List<Transition>)this.binding.getVariable("error_transitions")).add(transition);
    }

    public List<Transition> getErrorTransitions(){
        return (List<Transition>) this.binding.getVariable("error_transitions");
    }

    public void createState(String name, List<Action> actions) {
        State state = new State();
        state.setName(name);
        state.setActions(actions);

        if (this.binding.hasVariable("frequency")) {
            double frequency = (double) this.binding.getVariable("frequency");

            if (frequency > 0)
                state.setDebounce(1 / frequency * 1000);
        }

        this.states.add(state);
        this.binding.setVariable(name, state);
    }

    public void createErrorState(String name, List<Action> actions){
        State state = new ErrorState();
        state.setName(name);
        state.setActions(actions);

        if (this.binding.hasVariable("frequency")) {
            double frequency = (double) this.binding.getVariable("frequency");

            if (frequency > 0)
                state.setDebounce(1 / frequency * 1000);
        }

        this.states.add(state);
        this.binding.setVariable(name, state);
    }

    public State getState(String name){
        return (State) this.binding.getVariable(name);
    }

    public void createTransition(State from, State to, Sensor sensor, SIGNAL value) {
        Transition transition = new Transition();
        transition.setNext(to);
        transition.addCondition(sensor, value);
        from.addTransition(transition);
    }

    public void setInitialState(State state) {
        this.initialState = state;
    }

    @SuppressWarnings("rawtypes")
    public Object generateCode(String appName) {
        App app = new App();
        app.setName(appName);
        app.setBricks(this.bricks);
        app.setStates(this.states);
        app.setInitial(this.initialState);
        app.setErrorTransitions((List<Transition>) this.binding.getVariable("error_transitions"));
        Visitor codeGenerator = new ToWiring();
        app.accept(codeGenerator);

        return codeGenerator.getResult();
    }
}
