package dslgroovy.dsl

import fr.unice.polytech.si5.dsl.structural.SIGNAL
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.SecureASTCustomizer

class DslGroovyDSL {
    private GroovyShell shell
    private CompilerConfiguration configuration
    private DslGroovyBinding binding
    private DslGroovyBasescript basescript

    DslGroovyDSL() {
        binding = new DslGroovyBinding()
        binding.setModel(new DslGroovyModel(binding));
        configuration = getDSLConfiguration()
        configuration.setScriptBaseClass("dslgroovy.dsl.DslGroovyBasescript")
        shell = new GroovyShell(configuration)

        binding.setVariable("high", SIGNAL.HIGH)
        binding.setVariable("low", SIGNAL.LOW)
    }

    private static CompilerConfiguration getDSLConfiguration() {
        def secure = new SecureASTCustomizer()
        secure.with {
            //disallow closure creation
            closuresAllowed = false
            //disallow method definitions
            methodDefinitionAllowed = true
            //empty white list => forbid imports
            importsWhitelist = [
                    'java.lang.*', 'java.math.*'
            ]
            staticImportsWhitelist = []
            staticStarImportsWhitelist= []
            //language tokens disallowed
            // tokensBlacklist= []
            //language tokens allowed
            tokensWhitelist= []
            //types allowed to be used  (including primitive types)
            constantTypesClassesWhiteList= [
                    int, double, Integer, Number, Integer.TYPE, String, Object, BigDecimal
            ]
            //classes who are allowed to be receivers of method calls
            receiversClassesWhiteList= [
                    int, double, Number, Integer, String, Object, BigDecimal
            ]
        }

        def configuration = new CompilerConfiguration()
        configuration.addCompilationCustomizers(secure)

        return configuration
    }

    void eval(File scriptFile) {
        Script script = shell.parse(scriptFile)

        binding.setScript(script)
        script.setBinding(binding)

        script.run()
    }
}
