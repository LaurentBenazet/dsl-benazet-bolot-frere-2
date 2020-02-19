package dslgroovy.dsl;

import fr.unice.polytech.si5.dsl.App;
import fr.unice.polytech.si5.dsl.generator.ToWiring;
import fr.unice.polytech.si5.dsl.generator.Visitor;
import fr.unice.polytech.si5.dsl.model.Bar;
import fr.unice.polytech.si5.dsl.model.Instrument;
import fr.unice.polytech.si5.dsl.model.Section;
import fr.unice.polytech.si5.dsl.model.Track;
import groovy.lang.Binding;

import java.util.Arrays;

public class DslGroovyModel {
    //private List<Brick> bricks;
    //private List<State> states;
    //private State initialState;

    private Binding binding;

    private Track currentTrack;
    private Section currentSection;

    public DslGroovyModel(Binding binding) {
        this.binding = binding;
    }

    @SuppressWarnings("rawtypes")
    public Object generateCode(String appName) {
        App app = new App();
        app.setName(appName);
        Visitor codeGenerator = new ToWiring();
        app.accept(codeGenerator);

        return codeGenerator.getResult();
    }

    public void createTrack(String trackName) {
        currentTrack = new Track(trackName);
        binding.setVariable(trackName, currentTrack);
    }

    public void createSection(String sectionName) {
        currentSection = new Section(sectionName);
        currentTrack.addSection(currentSection);
        binding.setVariable(sectionName, currentSection);
    }

    public void setTempo(int tempo) {
        currentSection.setTempo(tempo);
    }

    public void setBeats(int beats) {
        currentSection.setBeats(beats);
    }

    public void createInstrument(String name) {
        binding.setVariable(name, new Instrument(name));
    }

    public void setInstrumentType(String name, String type) {
        ((Instrument) binding.getVariable(name)).setType(type);
    }

    public void createBarForInstrument(String instrName, String content) {
        Bar bar = new Bar();
        ((Instrument) binding.getVariable(instrName)).addBar(bar);

        String[] strNotes = content.split(" ");

        Arrays.asList(strNotes).forEach(bar::addNote);
    }

    public void setStartAt(String name) {
        binding.setVariable("start", name);
    }
}
