package io.micronaut.picocli.docs.cli.app

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MyCliAppCommandTest {

    @Test
    fun testWithCommandLineOption() {
        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            val args = arrayOf("-v")
            PicocliRunner.run(MyCliAppCommand::class.java, ctx, *args)

            Assertions.assertTrue(baos.toString().contains("Hi!"))
        }
    }
}
