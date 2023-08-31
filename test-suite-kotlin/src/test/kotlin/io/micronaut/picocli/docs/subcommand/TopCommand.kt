package io.micronaut.picocli.docs.subcommand

// tag::imports[]
import io.micronaut.configuration.picocli.PicocliRunner
import picocli.CommandLine.Command
import java.util.concurrent.Callable
// end::imports[]

// tag::class[]
@Command(name = "topcmd", subcommands = [SubCmd1::class, SubCmd2::class]) // <1>
class TopCommand : Callable<Any> { // <2>

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            PicocliRunner.execute(TopCommand::class.java, *args) // <3>
        }
    }

    @Throws(Exception::class)
    override fun call(): Any {
        return "Hi Top Command!"
    }
}

@Command(name = "subcmd1")
internal class SubCmd1 : Callable<Any> { // <2>

    @Throws(Exception::class)
    override fun call(): Any {
        return "Hi Sub Command 1!"
    }
}

@Command(name = "subcmd2")
internal class SubCmd2 : Callable<Any> { // <2>

    @Throws(Exception::class)
    override fun call(): Any {
        return "Hi Sub Command 2!"
    }
}
// end::class[]
