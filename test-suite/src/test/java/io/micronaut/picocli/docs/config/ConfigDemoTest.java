package io.micronaut.picocli.docs.config;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigDemoTest {

    @Test
    void testConfigurationExample() {

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { };
            Object called = PicocliRunner.call(ConfigDemo.class, ctx, args);

            assertEquals("Hi!", called);
        }
    }
}
