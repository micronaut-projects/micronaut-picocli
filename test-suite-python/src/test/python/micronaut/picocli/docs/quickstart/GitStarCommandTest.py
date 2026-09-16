from contextlib import redirect_stdout
from io import StringIO
from typing import Annotated

from jakarta.inject import Inject
from micronaut.configuration.picocli import PicocliRunner
from micronaut.context import ApplicationContext
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Disabled, Test

from .GitStarCommand import GitStarCommand


# TODO(python): picocli reads @Command/@Option/@Parameters reflectively from the Java class, but the Python compiler
# neither resolves the nested picocli.CommandLine annotations as decorators nor copies non-Micronaut annotations onto
# the generated Java class. See micronaut/picocli/docs/DISABLED_TESTS.md.
@Disabled("TODO(python): picocli annotations of Python classes are not emitted on the generated Java class")
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
