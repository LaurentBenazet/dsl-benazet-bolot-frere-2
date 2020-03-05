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
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DslGroovyModel {

    private Binding binding;

    private Track track;
    private Map<String, Section> sections = new HashMap<>();
    private Section currentSection;
    private Instrument currentInstrument;
    private Bar currentBar;

    public DslGroovyModel(Binding binding) {
        this.binding = binding;
    }

    public Track getTrack() {
        return track;
    }

    @SuppressWarnings("rawtypes")
    public Object generateCode(String appName) {
        App app = new App();
        app.setName(appName);
        app.setTrack(track);

        Visitor codeGenerator = new ToWiring();
        app.accept(codeGenerator);

        return codeGenerator.getResult();
    }

    public void createTrack(String trackName) {
        track = new Track(trackName);
    }

    public void createSection(String sectionName) {
        currentSection = new Section(sectionName);
        sections.put(sectionName, currentSection);
    }

    public void setTempo(int tempo) {
        currentSection.setTempo(tempo);
    }

    public void setBeats(int beats) {
        currentSection.setBeats(beats);
    }

    public void createInstrument(String name) {
        track.addInstrument(new Instrument(name));
    }

    public void repeatBar(int amount) {
        for (int i = 0; i < amount; i++) {
            createBarForInstrument(
                    currentInstrument.getName(),
                    String.join(" ", currentBar.getNotes())
            );
        }
    }

    public void setInstrumentType(String name, String strType) {
        String formattedType = formatInstrumentType(strType);
        track.findInstrument(name).setType(formattedType);
    }

    public void createBarForInstrument(String instrName, String content) {
        currentBar = new Bar();
        currentBar.addNotes(Arrays.asList(content.split(" ")));
        currentInstrument = track.findInstrument(instrName);
        currentSection.addBar(currentInstrument, currentBar);
    }

    public void playSections(String... names) {
        Arrays.stream(names).forEach(name-> track.addSection(sections.get(name)));
    }

    ///////////////////////////////////////////
    ////////////////// UTILS //////////////////
    ///////////////////////////////////////////

    private String formatInstrumentType(String raw) {
        return Arrays.stream(raw.trim().replaceAll("_", " ").split(" "))
                .map(this::toFirstUppercase)
                .collect(Collectors.joining("_"));
    }

    private String toFirstUppercase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
