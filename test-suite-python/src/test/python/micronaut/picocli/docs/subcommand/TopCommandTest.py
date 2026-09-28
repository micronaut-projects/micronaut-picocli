from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .TopCommand import SubCmd1, SubCmd2, TopCommand


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
