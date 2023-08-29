package io.micronaut.picocli.docs.config;

// tag::imports[]
import io.micronaut.configuration.picocli.MicronautFactory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import picocli.CommandLine;
import picocli.CommandLine.*;
import java.util.concurrent.Callable;
// end::imports[]

// tag::class[]
@Command(name = "configuration-example")
public class ConfigDemo implements Callable<Object> {
    private static int execute(Class<?> clazz, String[] args) {
        try (ApplicationContext context = ApplicationContext.builder(
            clazz, Environment.CLI).start()) { // <1>

            return new CommandLine(clazz, new MicronautFactory(context)). // <2>
                setCaseInsensitiveEnumValuesAllowed(true). // <3>
                setUsageHelpAutoWidth(true). // <4>
                execute(args); // <5>
        }
    }

    public static void main(String[] args) {
        int exitCode = execute(ConfigDemo.class, args);
        System.exit(exitCode); // <6>
    }

    @Override
    public Object call() {
        return "Hi!";
    }
}
// end::class[]
