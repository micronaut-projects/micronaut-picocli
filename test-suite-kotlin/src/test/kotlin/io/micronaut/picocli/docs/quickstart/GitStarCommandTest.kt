package io.micronaut.picocli.docs.quickstart

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GitStarCommandTest {

    @Test
    fun testGitStarCommand() {

        val baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))

        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            val args = arrayOf<String>()
            PicocliRunner.run(GitStarCommand::class.java, ctx, *args)
            val output = baos.toString()

            Assertions.assertTrue(output.contains("micronaut-projects/micronaut-core has "))
            Assertions.assertTrue(output.contains("remkop/picocli has "))
        }
    }
}
