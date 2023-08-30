package io.micronaut.picocli.docs.subcommand;

// tag::imports[]
import picocli.CommandLine.Command;
import io.micronaut.configuration.picocli.PicocliRunner;

import java.util.concurrent.Callable;
// end::imports[]

// tag::class[]
@Command(name = "topcmd", subcommands = {SubCmd1.class, SubCmd2.class}) // <1>
public class TopCommand implements Callable<Object> { // <2>

    public static void main(String[] args) {
        PicocliRunner.execute(TopCommand.class, args); // <3>
    }

    @Override
    public Object call() throws Exception {
        return "Hi Top Command!";
    }
}

@Command(name = "subcmd1")
class SubCmd1 implements Callable<Object> { // <2>
    @Override
    public Object call() throws Exception {
        return "Hi Sub Command 1!";
    }
}
@Command(name = "subcmd2")
class SubCmd2 implements Callable<Object> { // <2>
    @Override
    public Object call() throws Exception {
        return "Hi Sub Command 2!";
    }
}
// tag::class[]
