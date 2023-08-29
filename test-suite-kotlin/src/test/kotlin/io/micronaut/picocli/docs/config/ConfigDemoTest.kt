package io.micronaut.picocli.docs.config

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigDemoTest {

    @Test
    fun testConfigurationExample() {
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            val args = arrayOf<String>()
            val called = PicocliRunner.call(ConfigDemo::class.java, ctx, *args)

            assertEquals("Hi!", called)
        }
    }
}
