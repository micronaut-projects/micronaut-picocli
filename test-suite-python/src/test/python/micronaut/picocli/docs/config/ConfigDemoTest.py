from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .ConfigDemo import ConfigDemo


# TODO(python): picocli reads @Command reflectively from the Java class, but the Python compiler neither resolves
# the nested picocli.CommandLine annotations as decorators nor copies non-Micronaut annotations onto the generated
# Java class. See micronaut/picocli/docs/DISABLED_TESTS.md.
@Disabled("TODO(python): picocli annotations of Python classes are not emitted on the generated Java class")
@MicronautTest(environments=["cli"])
class ConfigDemoTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_configuration_example(self):
        called = PicocliRunner.call(ConfigDemo, self.ctx, [])

        assert called == "Hi!"
