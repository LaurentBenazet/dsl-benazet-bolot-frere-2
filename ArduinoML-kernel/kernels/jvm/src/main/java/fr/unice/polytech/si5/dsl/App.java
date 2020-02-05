package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.behavioral.State;
import fr.unice.polytech.si5.dsl.behavioral.Transition;
import fr.unice.polytech.si5.dsl.generator.Visitable;
import fr.unice.polytech.si5.dsl.generator.Visitor;
import fr.unice.polytech.si5.dsl.structural.Brick;

import java.util.ArrayList;
import java.util.List;

public class App implements NamedElement, Visitable {

	private String name;
	private List<Brick> bricks = new ArrayList<>();
	private List<State> states = new ArrayList<>();
	private State initial;
	private List<Transition> errorTransitions = new ArrayList<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<Brick> getBricks() {
		return bricks;
	}

	public void setBricks(List<Brick> bricks) {
		this.bricks = bricks;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public State getInitial() {
		return initial;
	}

	public void setInitial(State initial) {
		this.initial = initial;
	}

	public void setErrorTransitions(List<Transition> transitions){
		this.errorTransitions = transitions;
	}

	public List<Transition> getErrorTransitions() {
		return errorTransitions;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
