package io.micronaut.picocli.docs.config

// tag::imports[]
import io.micronaut.configuration.picocli.MicronautFactory
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import picocli.CommandLine
import picocli.CommandLine.Command
import java.util.concurrent.Callable
import kotlin.system.exitProcess
// end::imports[]

// tag::class[]
@Command(name = "configuration-example")
class ConfigDemo : Callable<Any> {

    companion object {
        private fun execute(clazz: Class<*>, args: Array<String>): Int {
            ApplicationContext.builder(clazz, Environment.CLI).start().use { context ->  // <1>

                return CommandLine(clazz, MicronautFactory(context)). // <2>
                setCaseInsensitiveEnumValuesAllowed(true). // <3>
                setUsageHelpAutoWidth(true). // <4>
                execute(*args) // <5>
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val exitCode = execute(ConfigDemo::class.java, args)
            exitProcess(exitCode) // <6>
        }
    }

    override fun call(): Any {
        return "Hi!"
    }
}
// end::class[]
