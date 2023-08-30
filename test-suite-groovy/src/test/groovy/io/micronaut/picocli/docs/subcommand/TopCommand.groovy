package io.micronaut.picocli.docs.subcommand

// tag::imports[]
import picocli.CommandLine.Command
import io.micronaut.configuration.picocli.PicocliRunner

import java.util.concurrent.Callable
// end::imports[]

// tag::class[]
@Command(name = 'topcmd', subcommands = [ SubCmd1, SubCmd2 ]) // <1>
class TopCommand implements Callable<Object> { // <2>

    static void main(String[] args) {
        PicocliRunner.execute(TopCommand.class, args) // <3>
    }

    @Override
    Object call() throws Exception {
        'Hi Top Command!'
    }
}

@Command(name = 'subcmd1')
class SubCmd1 implements Callable<Object> { // <2>
    @Override
    Object call() throws Exception {
        'Hi Sub Command 1!'
    }
}
@Command(name = 'subcmd2')
class SubCmd2 implements Callable<Object> { // <2>
    @Override
    Object call() throws Exception {
        'Hi Sub Command 2!'
    }
}
// end::class[]
