# tag::imports[]
from typing import Annotated

from jakarta.inject import Inject
from java.lang import Runnable, System
from micronaut.configuration.picocli import PicocliRunner
from micronaut.http import HttpRequest
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from picocli.CommandLine import Command, Option, Parameters
# end::imports[]


# tag::class[]
@Command(name="git-star", header=[
    "@|green       _ _      _             |@",  # <1>
    "@|green  __ _(_) |_ __| |_ __ _ _ _  |@",
    "@|green / _` | |  _(_-<  _/ _` | '_| |@",
    "@|green \\__, |_|\\__/__/\\__\\__,_|_|@",
    "@|green |___/                        |@"],
    description="Shows GitHub stars for a project",
    mixinStandardHelpOptions=True,
    version="git-star 0.1")  # <2>
class GitStarCommand(Runnable):

    client: Annotated[HttpClient, Client("https://api.github.com"), Inject]  # <3>

    verbose: Annotated[bool, Option(names=["-v", "--verbose"], description="Shows some project details")] = False

    github_slugs: Annotated[list[str], Parameters(  # <4>
        description=[
            "One or more GitHub slugs (comma separated) to show stargazers for. Default: ${DEFAULT-VALUE}"
        ],
        split=",",
        paramLabel="<owner/repo>"
    )] = ["micronaut-projects/micronaut-core", "remkop/picocli"]

    def run(self) -> None:  # <5>
        blocking_client = self.client.toBlocking()
        for slug in self.github_slugs:
            http_request = HttpRequest.GET("/repos/" + slug) \
                .header("User-Agent", "remkop-picocli")
            m = blocking_client.retrieve(http_request, dict)
            print(f"{slug} has {m['watchers']} stars")

            if self.verbose:
                print(f"Description: {m['description']}\n"
                      f"License: {m['license']['name']}\n"
                      f"Forks: {m['forks']}\n"
                      f"Open issues: {m['open_issues']}\n")

    @staticmethod
    def main(args: list[str]) -> None:
        exit_code = PicocliRunner.execute(GitStarCommand, args)
        System.exit(exit_code)
# end::class[]
