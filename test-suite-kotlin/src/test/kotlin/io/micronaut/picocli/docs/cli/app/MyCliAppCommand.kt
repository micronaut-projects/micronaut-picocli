package io.micronaut.picocli.docs.cli.app

// tag::imports[]
import io.micronaut.configuration.picocli.PicocliRunner
import org.slf4j.LoggerFactory
import picocli.CommandLine.Command
import picocli.CommandLine.Option
// end::imports[]

// tag::class[]
@Command(name = "my-cli-app", description = ["..."], mixinStandardHelpOptions = true) // <1>
class MyCliAppCommand : Runnable { // <2>

    @Option(names = ["-v", "--verbose"], description = ["..."]) // <3>
    var verbose = false

    companion object {
        private val LOG = LoggerFactory.getLogger(MyCliAppCommand::class.java)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.run(MyCliAppCommand::class.java, *args) // <4>
        }
    }

    override fun run() { // <5>
        // business logic here
        if (verbose) {
            LOG.info("Hi!")
        }
    }
}
// end::class[]
