from contextlib import redirect_stdout
from io import StringIO
from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

from .GitStarCommand import GitStarCommand


@MicronautTest(environments=["cli"])
class GitStarCommandTest:

    ctx: Annotated[ApplicationContext, Inject]

    @Test
    def test_git_star_command(self):
        output = StringIO()
        with redirect_stdout(output):
            PicocliRunner.run(GitStarCommand, self.ctx, ["-v"])

        assert "micronaut-projects/micronaut-core has " in output.getvalue()
        assert "remkop/picocli has " in output.getvalue()
