package io.micronaut.picocli.docs.config

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class ConfigDemoSpec extends Specification {

    void "test configuration example"() {
        given:
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        when:
        String[] args = new String[] { }
        Object called = PicocliRunner.call(ConfigDemo, ctx, args)

        then:
        called == 'Hi!'

        cleanup:
        ctx.stop()
    }
}
