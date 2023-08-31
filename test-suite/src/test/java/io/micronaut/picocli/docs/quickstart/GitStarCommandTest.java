package io.micronaut.picocli.docs.quickstart;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GitStarCommandTest {

    @Test
    void testGitStarCommand() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "-v" };
            PicocliRunner.run(GitStarCommand.class, ctx, args);
            String output = baos.toString();

            assertTrue(output.contains("micronaut-projects/micronaut-core has "));
            assertTrue(output.contains("remkop/picocli has "));
        }
    }
}
