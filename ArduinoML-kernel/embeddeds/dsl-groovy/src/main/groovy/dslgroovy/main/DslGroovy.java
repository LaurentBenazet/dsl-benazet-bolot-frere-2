package dslgroovy.main;

import java.io.File;

import dslgroovy.dsl.DslGroovyDSL;

/**
 * This main takes one argument: the path to the Groovy script file to execute.
 * <p>
 * "We've Got A Groovy Thing Goin'"!
 *
 * @author Thomas Moreau
 */
public class DslGroovy {
    public static void main(String[] args) {
        DslGroovyDSL dsl = new DslGroovyDSL();
        if (args.length > 0) {
            dsl.eval(new File(args[0]));
        } else {
            System.out.println("/!\\ Missing arg: Please specify the path to a Groovy script file to execute");
        }
    }
}