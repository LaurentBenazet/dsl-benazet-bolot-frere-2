package dslgroovy.dsl;

import groovy.lang.Binding;
import groovy.lang.Script;

import java.util.Map;

public class DslGroovyBinding extends Binding {
    private Script script;
    private DslGroovyModel model;

    public DslGroovyBinding() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public DslGroovyBinding(Map variables) {
        super(variables);
    }

    public DslGroovyBinding(Script script) {
        super();
        this.script = script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Object getVariable(String name) {
        return super.getVariable(name);
    }

    public void setVariable(String name, Object value) {
        super.setVariable(name, value);
    }

    public DslGroovyModel getModel() {
        return model;
    }

    public void setModel(DslGroovyModel model) {
        this.model = model;
    }
}
