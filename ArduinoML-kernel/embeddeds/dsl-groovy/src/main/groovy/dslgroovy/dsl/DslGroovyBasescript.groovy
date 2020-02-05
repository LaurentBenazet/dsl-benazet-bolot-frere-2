package dslgroovy.dsl

import fr.unice.polytech.si5.dsl.behavioral.Action
import fr.unice.polytech.si5.dsl.behavioral.Condition
import fr.unice.polytech.si5.dsl.behavioral.State
import fr.unice.polytech.si5.dsl.behavioral.Transition
import fr.unice.polytech.si5.dsl.structural.Actuator
import fr.unice.polytech.si5.dsl.structural.SIGNAL
import fr.unice.polytech.si5.dsl.structural.Sensor

abstract class DslGroovyBasescript extends Script {
    // sensor "name" pin n
    def sensor(String name) {
        [pin  : { n -> ((DslGroovyBinding) this.getBinding()).getModel().createSensor(name, n) },
         onPin: { n -> ((DslGroovyBinding) this.getBinding()).getModel().createSensor(name, n) }]
    }

    // actuator "name" pin n
    def actuator(String name) {
        [pin  : { n -> ((DslGroovyBinding) this.getBinding()).getModel().createActuator(name, n) },
         onPin: { n -> ((DslGroovyBinding) this.getBinding()).getModel().createActuator(name, n) }]
    }

    // state "name" means actuator becomes signal [and actuator becomes signal]*n
    def state(String name) {
        List<Action> actions = new ArrayList<Action>()
        ((DslGroovyBinding) this.getBinding()).getModel().createState(name, actions)
        // recursive closure to allow multiple and statements
        def closure
        closure = { actuator ->
            [becomes: { signal ->
                Action action = new Action()
                action.setActuator(actuator instanceof String ? (Actuator) ((DslGroovyBinding) this.getBinding()).getVariable(actuator) : (Actuator) actuator)
                action.setValue(signal instanceof String ? (SIGNAL) ((DslGroovyBinding) this.getBinding()).getVariable(signal) : (SIGNAL) signal)
                actions.add(action)
                [and: closure]
            }]
        }
        [means: closure]
    }

    // initial state
    def initial(state) {
        ((DslGroovyBinding) this.getBinding()).getModel().setInitialState(state instanceof String ? (State) ((DslGroovyBinding) this.getBinding()).getVariable(state) : (State) state)
    }

    // from state1 to state2 when sensor becomes signal
    def from(state1) {
        [to: { state2 ->
            [when: { sensor ->
                [becomes: { signal ->
                    ((DslGroovyBinding) this.getBinding()).getModel().createTransition(
                            state1 instanceof String ? (State) ((DslGroovyBinding) this.getBinding()).getVariable(state1) : (State) state1,
                            state2 instanceof String ? (State) ((DslGroovyBinding) this.getBinding()).getVariable(state2) : (State) state2,
                            sensor instanceof String ? (Sensor) ((DslGroovyBinding) this.getBinding()).getVariable(sensor) : (Sensor) sensor,
                            signal instanceof String ? (SIGNAL) ((DslGroovyBinding) this.getBinding()).getVariable(signal) : (SIGNAL) signal)

                    [frequency: { freq ->
                        State originState = (State) ((DslGroovyBinding) this.getBinding()).getVariable((String) state1)
                        originState.setFrequency((double) freq)
                    }]
                }]
            }]
        }]
    }

    // thorw_error 3 on actuator when sensor is signal and ...
    def err(code) {
        def model = ((DslGroovyBinding) this.getBinding()).getModel()

        def actions = new ArrayList<Action>()

        Transition transition = new Transition()

        [on: { actuator ->
            Closure closure
            closure = { sensor ->
                [becomes: { signal ->
                    def actualSensor = sensor instanceof String ? (Sensor) ((DslGroovyBinding) this.getBinding()).getVariable(sensor) : (Sensor) sensor
                    def actualSignal = signal instanceof String ? (SIGNAL) ((DslGroovyBinding) this.getBinding()).getVariable(signal) : (SIGNAL) signal
                    transition.addCondition(new Condition(actualSensor, actualSignal))
                    [and: closure]
                }]
            }

            for (int i = 0; i < code; i++) {
                Action action = new Action()
                action.setActuator(actuator instanceof String ? (Actuator) ((DslGroovyBinding) this.getBinding()).getVariable(actuator) : (Actuator) actuator)
                action.setValue(SIGNAL.HIGH)
                actions.add(action)

                action = new Action()
                action.setActuator(actuator instanceof String ? (Actuator) ((DslGroovyBinding) this.getBinding()).getVariable(actuator) : (Actuator) actuator)
                action.setValue(SIGNAL.LOW)
                actions.add(action)
            }

            model.createErrorState((String) ("error${code}"), actions)

            transition.setNext(model.getState("error${code}"))

            model.addErrorTransition(transition)

            [when: closure]


        }]
    }

    def frequency(double delay) {
        ((DslGroovyBinding) this.getBinding()).getModel().setFrequency(delay)
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
}
