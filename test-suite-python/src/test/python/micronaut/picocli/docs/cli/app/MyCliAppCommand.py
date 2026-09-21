# tag::imports[]
import logging
from typing import Annotated

from java.lang import Runnable
from micronaut.core.annotation import Introspected
from micronaut.configuration.picocli import PicocliRunner
from picocli.CommandLine import Command, Option

LOG = logging.getLogger(__name__)
# end::imports[]


# tag::class[]
@Introspected  # TODO(python): the @Option/@Parameters attribute annotations are copied onto the generated class only for an introspected class
@Command(name="my-cli-app", description="...", mixinStandardHelpOptions=True)  # <1>
class MyCliAppCommand(Runnable):  # <2>

    verbose: Annotated[bool, Option(names=["-v", "--verbose"], description="...")] = False  # <3>

    @staticmethod
    def main(args: list[str]) -> None:
        PicocliRunner.run(MyCliAppCommand, args)  # <4>

    def run(self) -> None:  # <5>
        # business logic here
        if self.verbose:
            LOG.info("Hi!")
# end::class[]
