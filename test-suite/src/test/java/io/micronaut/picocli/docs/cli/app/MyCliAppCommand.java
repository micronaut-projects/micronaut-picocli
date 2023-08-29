package io.micronaut.picocli.docs.cli.app;

// tag::imports[]
import io.micronaut.configuration.picocli.PicocliRunner;
import org.slf4j.Logger;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static org.slf4j.LoggerFactory.getLogger;
// end::imports[]

// tag::class[]
@Command(name = "my-cli-app", description = "...", mixinStandardHelpOptions = true) // <1>
public class MyCliAppCommand implements Runnable { // <2>
    private static final Logger LOG = getLogger(MyCliAppCommand.class);

    @Option(names = {"-v", "--verbose"}, description = "...") // <3>
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(MyCliAppCommand.class, args); // <4>
    }

    public void run() { // <5>
        // business logic here
        if (verbose) {
            LOG.info("Hi!");
        }
    }
}
// end::class[]
