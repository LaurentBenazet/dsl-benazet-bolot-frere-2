package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.generator.Visitable;
import fr.unice.polytech.si5.dsl.generator.Visitor;

public class App implements NamedElement, Visitable {

	private String name;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void setName(String name) {

	}

	@Override
	public String getName() {
		return null;
	}
}
