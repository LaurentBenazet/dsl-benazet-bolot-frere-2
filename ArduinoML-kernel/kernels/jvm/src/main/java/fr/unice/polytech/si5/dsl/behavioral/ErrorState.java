package fr.unice.polytech.si5.dsl.behavioral;

import fr.unice.polytech.si5.dsl.generator.Visitor;

public class ErrorState extends State {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
