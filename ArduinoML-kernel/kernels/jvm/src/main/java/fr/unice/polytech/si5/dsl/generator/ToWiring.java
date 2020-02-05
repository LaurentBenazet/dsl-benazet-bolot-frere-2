package fr.unice.polytech.si5.dsl.generator;

import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.model.*;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

    private final static String CURRENT_STATE = "current_state";
    private final static String FINAL_STATE = "final_state";

    public ToWiring() {
        this.result = new StringBuffer();
    }

    private void w(String s) {
        result.append(String.format("%s\n", s));
    }


    @Override
    public void visit(App app) {

    }

    @Override
    public void visit(Bar bar) {

    }

    @Override
    public void visit(Beat beat) {

    }

    @Override
    public void visit(Instrument instrument) {

    }

    @Override
    public void visit(Note note) {

    }

    @Override
    public void visit(Track track) {

    }

    @Override
    public void visit(Section section) {

    }
}
