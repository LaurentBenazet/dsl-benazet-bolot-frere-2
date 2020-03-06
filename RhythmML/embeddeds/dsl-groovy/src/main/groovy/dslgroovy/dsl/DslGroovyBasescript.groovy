package dslgroovy.dsl

abstract class DslGroovyBasescript extends Script {

    def track(String name) {
        model().createTrack(name)
    }

    def instrument(String name) {
        model().createInstrument(name)
        [type: { String instrType ->
            ((DslGroovyBinding) this.getBinding()).getModel().setInstrumentType(name, instrType)
        }]
    }

    def section(String name) {
        model().createSection(name)
    }

    def tempo(int tempo) {
        model().setTempo(tempo)
    }

    def beats(int beats) {
        model().setBeats(beats)
    }

    def instr(String instrName) {

        def closure
        closure = { String content ->
            ((DslGroovyBinding) this.getBinding()).getModel().createBarForInstrument(instrName, content)
            [repeat: { int amount ->
                ((DslGroovyBinding) this.getBinding()).getModel().repeatBar(amount)
            }, bar : closure]
        }
        [bar: closure]
    }

    def repeat(int amount) {
        ((DslGroovyBinding) this.getBinding()).getModel().repeatBar(amount)
    }

    def play(String... names) {
        model().playSections(names)
        String name = model().getTrack().getName()
        println(model().generateCode(name).toString())
    }

    // export name
    def export(String name) {
        println(((DslGroovyBinding) this.getBinding()).getModel().generateCode(name).toString())
    }

    // disable run method while running
    int count = 0

    abstract void scriptBody()

    def run() {
        if (count == 0) {
            count++
            scriptBody()
        } else {
            println "Run method is disabled"
        }
    }

    private def model() {
        return ((DslGroovyBinding) this.getBinding()).getModel()
    }
}
