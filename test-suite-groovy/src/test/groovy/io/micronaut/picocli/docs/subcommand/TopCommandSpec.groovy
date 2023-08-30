package io.micronaut.picocli.docs.subcommand

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import io.micronaut.picocli.docs.config.ConfigDemo
import spock.lang.Specification

class TopCommandSpec extends Specification {

    void "test sub commands example"() {
        given:
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        when:
        String[] args = new String[] { }
        Object called = PicocliRunner.call(TopCommand, ctx, args)

        then:
        called == 'Hi Top Command!'

        when:
        called = PicocliRunner.call(SubCmd1, ctx, args)

        then:
        called == 'Hi Sub Command 1!'

        when:
        called = PicocliRunner.call(SubCmd2, ctx, args)

        then:
        called == 'Hi Sub Command 2!'

        cleanup:
        ctx.stop()
    }
}
