from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .ConfigDemo import ConfigDemo


@MicronautTest(environments=["cli"])
class ConfigDemoTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_configuration_example(self):
        called = PicocliRunner.call(ConfigDemo, self.ctx, [])

        assert called == "Hi!"
