from typing import Annotated

from jakarta.inject import Inject
from java.io import ByteArrayOutputStream, PrintStream
from java.lang import System
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .MyCliAppCommand import MyCliAppCommand


# TODO(python): picocli reads @Command/@Option reflectively from the Java class, but the Python compiler neither
# resolves the nested picocli.CommandLine annotations as decorators nor copies non-Micronaut annotations onto the
# generated Java class. See micronaut/picocli/docs/DISABLED_TESTS.md.
@Disabled("TODO(python): picocli annotations of Python classes are not emitted on the generated Java class")
@MicronautTest(environments=["cli"])
class MyCliAppCommandTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_with_command_line_option(self):
        baos = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))

        PicocliRunner.run(MyCliAppCommand, self.ctx, ["-v"])

        assert "Hi!" in baos.toString()
