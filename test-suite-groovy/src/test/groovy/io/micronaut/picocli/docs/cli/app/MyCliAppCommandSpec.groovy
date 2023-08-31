package io.micronaut.picocli.docs.cli.app

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class MyCliAppCommandSpec extends Specification {

    void "test with commandLine option"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        when:
        String[] args = [ '-v' ]
        PicocliRunner.run(MyCliAppCommand, ctx, args)

        then:
        baos.toString().contains('Hi!')

        cleanup:
        ctx.stop()
    }
}
