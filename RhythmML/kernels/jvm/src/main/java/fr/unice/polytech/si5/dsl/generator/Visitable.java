package fr.unice.polytech.si5.dsl.generator;

public interface Visitable {

	void accept(Visitor visitor);

}
