# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Picocli that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps). Use it as the
bug-fixing task list for the final migration wave.

## Reconciliation

- Last generated active `@Disabled` count: 4 (all four test classes).
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 4 tests executed (4 test classes), 4 skipped.

## Migration Rules

- Methods that implement a Java interface keep the Java name (`run`, `call`); other methods, attributes and local
  variables are snake_case (`github_slugs`, `blocking_client`, `exit_code`). picocli option and parameter
  declarations are `Annotated[<type>, Option(...)]` / `Annotated[<type>, Parameters(...)]` class attributes with
  their default as the attribute value.
- The Java `static void main(String[] args)` entry point is a `@staticmethod main(args: list[str])`; subcommand
  classes are declared before the top-level command that references them (`subcommands=[SubCmd1, SubCmd2]`).
- The Java tests create their own `ApplicationContext.run(Environment.CLI, Environment.TEST)`; the Python tests are
  `@MicronautTest(environments=["cli"])` classes with an injected `ApplicationContext` (a nested manual context
  would close the shared GraalPy context).
- Output written with `System.out` (logback console appender) is captured with `System.setOut(PrintStream(baos))`
  like in Java; output written with Python `print` is captured with `contextlib.redirect_stdout`.

## Active `@Disabled` Tests

| Test | Reason |
| --- | --- |
| `micronaut.picocli.docs.cli.app.MyCliAppCommandTest` | picocli discovers `@Command`, `@Option` and `@Parameters` reflectively on the Java class of the command. The Python compiler (a) does not resolve the nested annotation types `picocli.CommandLine.Command` / `.Option` / `.Parameters` imported with `from picocli.CommandLine import Command` as decorators (the transformer skips nested annotation types, so the import is left as a plain Python import and fails at runtime with `ModuleNotFoundError: No module named 'picocli'`), and (b) copies only JUnit / `@MicronautTest` annotations onto the generated Java class, so even a resolved `@Command` would not be visible to picocli's reflection and the `@Option` attributes are plain `getVerbose()`/`setVerbose(boolean)` accessors without annotations. `# TODO(python)` |
| `micronaut.picocli.docs.config.ConfigDemoTest` | same as above (`@Command` on the class) |
| `micronaut.picocli.docs.quickstart.GitStarCommandTest` | same as above (`@Command`, `@Option`, `@Parameters`) |
| `micronaut.picocli.docs.subcommand.TopCommandTest` | same as above (`@Command(subcommands=[...])` on three classes) |

The four snippet classes document the intended programming model and are compiled by the Python compiler (the
generated Java classes implement `Runnable` / `Callable`); the guide carries a `[.lang-python]` WARNING on each of the
four pages until the compiler emits the picocli annotations on the generated classes.

## Commented Unsupported Snippet Ports

None.

## Intentionally Unsupported Snippet Targets

None.
