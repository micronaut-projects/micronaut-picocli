# tag::imports[]
from java.util.concurrent import Callable
from micronaut.configuration.picocli import PicocliRunner
from picocli.CommandLine import Command
# end::imports[]


# tag::class[]
@Command(name="subcmd1")
class SubCmd1(Callable[object]):  # <2>

    def call(self) -> object:
        return "Hi Sub Command 1!"


@Command(name="subcmd2")
class SubCmd2(Callable[object]):  # <2>

    def call(self) -> object:
        return "Hi Sub Command 2!"


@Command(name="topcmd", subcommands=[SubCmd1, SubCmd2])  # <1>
class TopCommand(Callable[object]):  # <2>

    @staticmethod
    def main(args: list[str]) -> None:
        PicocliRunner.execute(TopCommand, args)  # <3>

    def call(self) -> object:
        return "Hi Top Command!"
# end::class[]
