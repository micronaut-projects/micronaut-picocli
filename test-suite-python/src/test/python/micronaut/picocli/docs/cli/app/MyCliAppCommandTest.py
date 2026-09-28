import logging
from io import StringIO
from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .MyCliAppCommand import MyCliAppCommand


@MicronautTest(environments=["cli"])
class MyCliAppCommandTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_with_command_line_option(self):
        output = StringIO()
        handler = logging.StreamHandler(output)
        root = logging.getLogger()
        root.addHandler(handler)
        root.setLevel(logging.INFO)
        try:
            PicocliRunner.run(MyCliAppCommand, self.ctx, ["-v"])
        finally:
            root.removeHandler(handler)

        assert "Hi!" in output.getvalue()
