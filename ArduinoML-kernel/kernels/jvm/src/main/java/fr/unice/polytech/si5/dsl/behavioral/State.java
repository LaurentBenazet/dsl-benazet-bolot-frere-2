package fr.unice.polytech.si5.dsl.behavioral;

import fr.unice.polytech.si5.dsl.NamedElement;
import fr.unice.polytech.si5.dsl.generator.Visitable;
import fr.unice.polytech.si5.dsl.generator.Visitor;

import java.util.ArrayList;
import java.util.List;

public class State implements NamedElement, Visitable {

    private double debounce = 200;
    private String name;
    private List<Action> actions = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public long getDebounce() {
        return Math.round(debounce);
    }

    public void setDebounce(double debounce) {
        this.debounce = debounce;
    }

    public void setFrequency(double frequency) {
        this.debounce = 1 / frequency * 1000;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public boolean hasTransition() {
        return !transitions.isEmpty();
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
