package io.micronaut.picocli.docs.quickstart

// tag::imports[]
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.*

import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters

import jakarta.inject.Inject
// end::imports[]

// tag::class[]
@Command(name = 'git-star', header = [
    "@|green       _ _      _             |@", // <1>
    "@|green  __ _(_) |_ __| |_ __ _ _ _  |@",
    "@|green / _` | |  _(_-<  _/ _` | '_| |@",
    "@|green \\__, |_|\\__/__/\\__\\__,_|_|@",
    "@|green |___/                        |@"],
        description = 'Shows GitHub stars for a project',
        mixinStandardHelpOptions = true,
        version = 'git-star 0.1') // <2>
class GitStarCommand implements Runnable {

    @Client('https://api.github.com')
    @Inject
    HttpClient client // <3>

    @Option(names = [ '-v', '--verbose' ], description = 'Shows some project details')
    boolean verbose

    @Parameters(  // <4>
        description =  [
            'One or more GitHub slugs (comma separated) to show stargazers for. Default: ${DEFAULT-VALUE}'
        ],
        split = ',',
        paramLabel = '<owner/repo>'
    )
    List<String> githubSlugs = ['micronaut-projects/micronaut-core', 'remkop/picocli']

    void run() { // <5>
        BlockingHttpClient blockingClient = client.toBlocking()
        githubSlugs.each { slug ->
            HttpRequest<Object> httpRequest = HttpRequest.GET("/repos/$slug")
                    .header('User-Agent', 'remkop-picocli')
            Map<?,?> m = blockingClient.retrieve(httpRequest, Map.class)
            println("$slug has ${m.watchers} stars")

            if (verbose) {
                println "Description: ${m.description}\nLicense: ${m.license?.name}\nForks: ${m.forks}\nOpen issues: ${m.open_issues}\n"
            }
        }
    }

    static void main(String[] args) {
        int exitCode = PicocliRunner.execute(GitStarCommand, args + '-v')
        System.exit(exitCode)
    }
}
// end::class[]
