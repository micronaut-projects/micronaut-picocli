# tag::imports[]
from java.lang import System
from java.util.concurrent import Callable
from micronaut.configuration.picocli import MicronautFactory
from micronaut.context import ApplicationContext
from micronaut.context.env import Environment
from picocli import CommandLine
from picocli.CommandLine import Command
# end::imports[]


# tag::class[]
@Command(name="configuration-example")
class ConfigDemo(Callable[object]):

    @staticmethod
    def execute(clazz: type, args: list[str]) -> int:
        context = ApplicationContext.builder(clazz, Environment.CLI).start()  # <1>
        try:
            return (CommandLine(clazz, MicronautFactory(context))  # <2>
                    .setCaseInsensitiveEnumValuesAllowed(True)  # <3>
                    .setUsageHelpAutoWidth(True)  # <4>
                    .execute(args))  # <5>
        finally:
            context.close()

    @staticmethod
    def main(args: list[str]) -> None:
        exit_code = ConfigDemo.execute(ConfigDemo, args)
        System.exit(exit_code)  # <6>

    def call(self) -> object:
        return "Hi!"
# end::class[]
