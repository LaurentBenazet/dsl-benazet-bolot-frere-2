package fr.unice.polytech.si5.dsl.generator;


import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.model.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(Bar bar);
	public abstract void visit(Instrument instrument);
	public abstract void visit(Note note);
	public abstract void visit(Track track);
	public abstract void visit(Section section);

	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

