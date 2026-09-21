# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Picocli that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps). Use it as the
bug-fixing task list for the final migration wave.

## Reconciliation

- Last generated active `@Disabled` count: 0.
- Last generated command: `rg -n "@Disabled\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci`.
- Last full-suite result: build successful, 4 tests executed (4 test classes), 0 skipped.

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

None (core 5.2.3 / micronaut-build 8.1.2: the nested `picocli.CommandLine.Command` / `.Option` / `.Parameters` decorators
resolve, and the picocli annotations are kept on the generated classes with
`-Amicronaut.introspection.allowReflection=micronaut.picocli.docs.*`).

## Deviations from the Java samples

| Sample | Reason |
| --- | --- |
| `MyCliAppCommand`, `GitStarCommand` are `@Introspected` | picocli reads `@Option` / `@Parameters` from the fields (or annotated setters) of the Java class. The attribute annotations of a Python class are copied onto the generated class only for an introspected class (public fields); for a plain bean the attributes become `getVerbose()` / `setVerbose(boolean)` accessors without annotations, so picocli reports `Unknown option: '-v'`. `# TODO(python)` |
| `MyCliAppCommandTest` captures the log record with a `logging.StreamHandler` | Python's `logging` is the standard library module (no bridge to Logback), so `LOG.info("Hi!")` is not written to `System.out` and the Java test's `System.setOut` capture does not apply. |
| `GitStarCommand` uses `retrieve(request, Map)` (`java.util.Map`) | Python builtins (`dict`) are not usable as runtime type arguments (`Unsupported operation identifier 'getType'`). |

## Commented Unsupported Snippet Ports

None.

## Intentionally Unsupported Snippet Targets

None.
