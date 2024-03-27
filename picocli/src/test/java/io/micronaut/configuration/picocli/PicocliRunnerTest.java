package io.micronaut.configuration.picocli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Callable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

import io.micronaut.context.annotation.Property;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

public class PicocliRunnerTest {

    private static final int EXIT_CODE_INVALID_INPUT = 222;
    private static final int EXIT_CODE_EXECUTION_ERROR = 123;

    @Before
    public void before() {
        MyRunnableCmd.serviceResult = null;
        MyRunnableCmd.verbose = false;

        MyCallableCmd.x = 0;
        MyCallableCmd.y = 0;
    }

    @Singleton
    static class MyService {
        public String service() {
            return "good service";
        }
    }

    @Singleton
    static class AnotherService {
        public String service() {
            return "so-so service";
        }
    }

    @Command(name = "myRunnable", mixinStandardHelpOptions = true)
    static class MyRunnableCmd implements Runnable {

        @Inject
        AnotherService fieldInjectedService;

        //@Inject: use constructor injection instead of field injection
        MyService constructorInjectedService;

        @Property(name = "v", defaultValue = "false")
        boolean injectVerbose;

        MyRunnableCmd(MyService service) {
            constructorInjectedService = service;
            injectVerbose = false;
        }

        /** Command line option value for -v captured in static field for testing */
        @Option(names = {"-v", "--verbose"}, description = "...")
        static boolean verbose;

        /** Execution result captured in static field for testing */
        static String serviceResult;

        static boolean wasInjectedVerbose = false;

        public void run() {
            if (verbose) {
                System.out.println("myService: " + constructorInjectedService);
            }
            if (injectVerbose) {
                wasInjectedVerbose = injectVerbose;
            }
            serviceResult = constructorInjectedService.service();
        }
    }

    @Command(name = "myCallable", exitCodeOnInvalidInput = EXIT_CODE_INVALID_INPUT)
    static class MyCallableCmd implements Callable<Integer> {

        /** Command line option value for -x captured in static field for testing */
        @Option(names = "-x")
        static int x;

        /** Command line option value for -y captured in static field for testing */
        @Option(names = "-y")
        static int y;

        /** Returns the result of multiplying x * y. */
        public Integer call() {
            return x * y;
        }
    }

    @Command(name = "badCallable", exitCodeOnExecutionException = EXIT_CODE_EXECUTION_ERROR)
    static class BadCallableCmd implements Callable<Integer> {

        /** Always throws an exception. */
        public Integer call() {
            throw new IllegalStateException("internal error");
        }
    }

    @Test
    public void testRun() throws Exception {
        // preconditions
        assertFalse(MyRunnableCmd.verbose);
        assertNull(MyRunnableCmd.serviceResult);

        // exercise SUT
        PicocliRunner.run(MyRunnableCmd.class, "-v");

        // verify
        assertTrue(MyRunnableCmd.verbose);
        assertTrue(MyRunnableCmd.wasInjectedVerbose);
        assertEquals(MyRunnableCmd.serviceResult, new MyService().service());
    }

    @Test
    public void testCall() throws Exception {
        // preconditions
        assertEquals(0, MyCallableCmd.x);
        assertEquals(0, MyCallableCmd.y);

        // exercise SUT
        int result = PicocliRunner.call(MyCallableCmd.class, "-x=11", "-y", "3");

        // verify
        assertEquals(33, result);
        assertEquals(11, MyCallableCmd.x);
        assertEquals(3, MyCallableCmd.y);
    }

    @Test
    public void testExecuteNormal() {
        int exitCode = PicocliRunner.execute(MyRunnableCmd.class, "-v");
        assertEquals(0, exitCode);
    }

    @Test
    public void testExecuteNormal_withProvidedEnvironments() {
        int exitCode = PicocliRunner.execute(MyRunnableCmd.class, List.of("jdbc"), "-v");
        assertEquals(0, exitCode);
    }

    @Test
    public void testExecuteNonZeroExitCode() {
        int exitCode = PicocliRunner.execute(MyCallableCmd.class, "-x=11", "-y", "3");
        assertEquals(33, exitCode);
    }

    @Test
    public void testExecuteInvalidUserInput() {
        PrintStream oldErr = System.err;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setErr(new PrintStream(baos));

            int exitCode = PicocliRunner.execute(MyCallableCmd.class, "-x=NotANumber");
            assertEquals(EXIT_CODE_INVALID_INPUT, exitCode);

            assertThat(baos.toString(), containsString("Invalid value for option '-x': 'NotANumber' is not an int"));
        } finally {
            System.setErr(oldErr);
        }
    }

    @Test
    public void testExecuteErrorDuringExecution() {
        PrintStream oldErr = System.err;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setErr(new PrintStream(baos));

            int exitCode = PicocliRunner.execute(BadCallableCmd.class);
            assertEquals(EXIT_CODE_EXECUTION_ERROR, exitCode);

            assertThat(baos.toString(), containsString("java.lang.IllegalStateException: internal error"));
        } finally {
            System.setErr(oldErr);
        }
    }
}

