package fr.unice.polytech.si5.dsl;

import fr.unice.polytech.si5.dsl.generator.Visitable;
import fr.unice.polytech.si5.dsl.generator.Visitor;
import fr.unice.polytech.si5.dsl.model.Track;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class App implements NamedElement, Visitable {

    private String name;
    private Track track;

	@Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
