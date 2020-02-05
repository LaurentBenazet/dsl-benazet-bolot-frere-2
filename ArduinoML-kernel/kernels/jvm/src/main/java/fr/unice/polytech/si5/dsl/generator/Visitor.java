package fr.unice.polytech.si5.dsl.generator;


import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.behavioral.Action;
import fr.unice.polytech.si5.dsl.behavioral.ErrorState;
import fr.unice.polytech.si5.dsl.behavioral.State;
import fr.unice.polytech.si5.dsl.behavioral.Transition;
import fr.unice.polytech.si5.dsl.structural.Actuator;
import fr.unice.polytech.si5.dsl.structural.Sensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(ErrorState errorState);
	public abstract void visit(Transition transition);
	public abstract void visit(Action action);

	public abstract void visit(Actuator actuator);
	public abstract void visit(Sensor sensor);


	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

