package io.micronaut.picocli.docs.subcommand

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TopCommandTest {
    @Test
    fun testSubCommandsExample() {
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            val args = arrayOf<String>()

            var called = PicocliRunner.call(TopCommand::class.java, ctx, *args)
            Assertions.assertEquals("Hi Top Command!", called)

            called = PicocliRunner.call(SubCmd1::class.java, ctx, *args)
            Assertions.assertEquals("Hi Sub Command 1!", called)

            called = PicocliRunner.call(SubCmd2::class.java, ctx, *args)
            Assertions.assertEquals("Hi Sub Command 2!", called)
        }
    }
}
