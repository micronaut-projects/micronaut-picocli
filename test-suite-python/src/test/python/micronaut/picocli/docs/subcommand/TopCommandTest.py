from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .TopCommand import SubCmd1, SubCmd2, TopCommand


# TODO(python): picocli reads @Command reflectively from the Java class, but the Python compiler neither resolves
# the nested picocli.CommandLine annotations as decorators nor copies non-Micronaut annotations onto the generated
# Java class. See micronaut/picocli/docs/DISABLED_TESTS.md.
@Disabled("TODO(python): picocli annotations of Python classes are not emitted on the generated Java class")
@MicronautTest(environments=["cli"])
class TopCommandTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_sub_commands_example(self):
        called = PicocliRunner.call(TopCommand, self.ctx, [])
        assert called == "Hi Top Command!"

        called = PicocliRunner.call(SubCmd1, self.ctx, [])
        assert called == "Hi Sub Command 1!"

        called = PicocliRunner.call(SubCmd2, self.ctx, [])
        assert called == "Hi Sub Command 2!"
