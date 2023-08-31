package io.micronaut.picocli.docs.quickstart

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class GitStarCommandSpec extends Specification {

    void "test GitStarCommand app"() {

        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        when:
        String[] args = [ '-v' ]
        PicocliRunner.run(GitStarCommand, ctx, args)
        String output = baos.toString()

        then:
        output.contains('micronaut-projects/micronaut-core has ')
        output.contains('remkop/picocli has ')

        cleanup:
        ctx.stop()
    }
}
