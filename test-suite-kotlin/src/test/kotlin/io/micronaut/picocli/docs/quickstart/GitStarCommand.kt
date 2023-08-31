package io.micronaut.picocli.docs.quickstart

// tag::imports[]
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import kotlin.system.exitProcess
// end::imports[]

// tag::class[]
@Command(
    name = "git-star",
    header = [
        "@|green       _ _      _             |@",  // <1>
        "@|green  __ _(_) |_ __| |_ __ _ _ _  |@",
        "@|green / _` | |  _(_-<  _/ _` | '_| |@",
        "@|green \\__, |_|\\__/__/\\__\\__,_|_|@",
        "@|green |___/                        |@"],
    description = ["Shows GitHub stars for a project"],
    mixinStandardHelpOptions = true,
    version = ["git-star 0.1"] // <2>
)
class GitStarCommand : Runnable {

    @Inject
    @field:Client("https://api.github.com/")
    lateinit var client: HttpClient // <3>

    @Option(names = ["-v", "--verbose"], description = ["Shows some project details"])
    var verbose = false

    @Parameters(  // <4>
        description = ["One or more GitHub slugs (comma separated) to show stargazers for. Default: \${DEFAULT-VALUE}"],
        split = ",",
        paramLabel = "<owner/repo>"
    )
    var githubSlugs: List<String> = mutableListOf("micronaut-projects/micronaut-core", "remkop/picocli")

    override fun run() { // <5>
        val blockingClient = client.toBlocking()
        githubSlugs.forEach { slug ->
            val httpRequest = HttpRequest.GET<Any>("repos/$slug")
                .header("User-Agent", "remkop-picocli")
            val m = blockingClient.retrieve(httpRequest, Map::class.java)
            println("$slug has ${m["watchers"]} stars")

            if (verbose) {
                println("""Description: ${m["description"]}
                          |License: ${(m["license"] as Map<*,*>)["name"]}
                          |Forks: ${m["forks"]}
                          |Open issues: ${m["open_issues"]}
                          |""".trimMargin())
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val exitCode = PicocliRunner.execute(GitStarCommand::class.java, *args)
            exitProcess(exitCode)
        }
    }
}
// end::class[]
