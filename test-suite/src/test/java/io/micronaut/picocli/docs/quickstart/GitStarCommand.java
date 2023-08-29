package io.micronaut.picocli.docs.quickstart;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.*;

import static io.micronaut.http.HttpRequest.*;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.*;
import jakarta.inject.Inject;

@Command(name = "git-star", header = {
    "@|green       _ _      _             |@", // <1>
    "@|green  __ _(_) |_ __| |_ __ _ _ _  |@",
    "@|green / _` | |  _(_-<  _/ _` | '_| |@",
    "@|green \\__, |_|\\__/__/\\__\\__,_|_|@",
    "@|green |___/                        |@"},
    description = "Shows GitHub stars for a project",
    mixinStandardHelpOptions = true, version = "git-star 0.1") // <2>
public class GitStarCommand implements Runnable {

    @Client("https://api.github.com")
    @Inject
    HttpClient client; // <3>

    @Option(names = { "-v", "--verbose" }, description = "Shows some project details")
    boolean verbose;

    @Parameters(  // <4>
        description = { "One or more GitHub slugs (comma separated) to show stargazers for." +
            "  Default: ${DEFAULT-VALUE}"},
        split = ",",
        paramLabel = "<owner/repo>"
    )
    List<String> githubSlugs = Arrays.asList("micronaut-projects/micronaut-core", "remkop/picocli");

    public static void main(String[] args) {
        int exitCode = PicocliRunner.execute(GitStarCommand.class, args);
        System.exit(exitCode);
    }

    public void run() { // <5>
        BlockingHttpClient blockingClient = client.toBlocking();
        for (String slug : githubSlugs) {
            Map m = blockingClient.retrieve(GET("/repos/" + slug)
                .header("User-Agent", "remkop-picocli"), Map.class);
            System.out.printf("%s has %s stars%n", slug, m.get("watchers"));

            if (verbose) {
                String msg = "Description: %s%nLicense: %s%nForks: %s%nOpen issues: %s%n%n";
                System.out.printf(msg, m.get("description"),
                    ((Map) m.get("license")).get("name"),
                    m.get("forks"), m.get("open_issues"));
            }
        }
    }
}
