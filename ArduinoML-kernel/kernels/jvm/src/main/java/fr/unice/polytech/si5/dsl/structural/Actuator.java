package fr.unice.polytech.si5.dsl.structural;

import fr.unice.polytech.si5.dsl.generator.Visitor;

public class Actuator extends Brick {

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
