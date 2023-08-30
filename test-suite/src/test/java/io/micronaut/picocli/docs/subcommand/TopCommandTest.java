package io.micronaut.picocli.docs.subcommand;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopCommandTest {

    @Test
    public void testSubCommandsExample() {

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { };

            Object called = PicocliRunner.call(TopCommand.class, ctx, args);
            assertEquals("Hi Top Command!", called);

            called = PicocliRunner.call(SubCmd1.class, ctx, args);
            assertEquals("Hi Sub Command 1!", called);

            called = PicocliRunner.call(SubCmd2.class, ctx, args);
            assertEquals("Hi Sub Command 2!", called);
        }
    }
}
