/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.configuration.picocli;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.env.CommandLinePropertySource;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.TypeHint;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * Utility class with convenience methods for running picocli-based commands with
 * a micronaut application context.
 *
 * @author Remko Popma
 * @since 1.0
 */
@TypeHint(
    value = {System.class, Executable.class, Parameter.class, ResourceBundle.class, Path.class, Paths.class},
    typeNames = {"picocli.CommandLine$AutoHelpMixin", "picocli.CommandLine$Model$CommandSpec"},
    accessType = {TypeHint.AccessType.ALL_DECLARED_FIELDS, TypeHint.AccessType.ALL_PUBLIC_METHODS, TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS}
)
public class PicocliRunner {
    /**
     * Instantiates a new {@link ApplicationContext} for the {@link Environment#CLI} environment,
     * obtains an instance of the specified {@code Callable} command class from the context,
     * injecting any beans as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally calls the command and returns the result.
     * <p>
     * The {@code ApplicationContext} is {@linkplain ApplicationContext#close() closed} before this method returns.
     * </p>
     * @param cls the Callable command class
     * @param args the command line arguments
     * @param <C> The callable type
     * @param <T> The callable return type
     * @return {@code null} if an error occurred while parsing the command line options,
     *      or if help was requested and printed. Otherwise returns the result of calling the Callable
     */
    public static <C extends Callable<T>, T> T call(Class<C> cls, String... args) {
        try (ApplicationContext ctx = ApplicationContext.build(cls, Environment.CLI).start()) {
            return call(cls, ctx, args);
        }
    }

    /**
     * Obtains an instance of the specified {@code Callable} command class from the specified context,
     * injecting any beans from the specified context as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally calls the command and returns the result.
     * <p>
     * The caller is responsible for {@linkplain ApplicationContext#close() closing} the context.
     * </p>
     * @param cls the Callable command class
     * @param ctx the ApplicationContext that injects dependencies into the command
     * @param args the command line arguments
     * @param <C> The callable type
     * @param <T> The callable return type

     * @return {@code null} if an error occurred while parsing the command line options,
     *      or if help was requested and printed. Otherwise returns the result of calling the Callable
     * @throws InitializationException if the specified command object does not have
     *          a {@link Command}, {@link Option} or {@link Parameters} annotation
     * @throws ExecutionException if the Callable throws an exception
     */
    public static <C extends Callable<T>, T> T call(Class<C> cls, ApplicationContext ctx, String... args) {
        CommandLine commandLine = new CommandLine(cls, new MicronautFactory(ctx));
        commandLine.execute(args);
        return commandLine.getExecutionResult();
    }

    /**
     * Instantiates a new {@link ApplicationContext} for the {@link Environment#CLI} environment,
     * obtains an instance of the specified {@code Runnable} command class from the context,
     * injecting any beans as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally runs the command.
     * <p>
     * The {@code ApplicationContext} is {@linkplain ApplicationContext#close() closed} before this method returns.
     * </p>
     * @param cls the Runnable command class
     * @param args the command line arguments
     * @param <R> The runnable type
     *
     */
    public static <R extends Runnable> void run(Class<R> cls, String... args) {
        ApplicationContextBuilder builder = buildApplicationContext(cls, args);
        try (ApplicationContext ctx = builder.start()) {
            run(cls, ctx, args);
        }
    }

    /**
     * Obtains an instance of the specified {@code Runnable} command class from the specified context,
     * injecting any beans from the specified context as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally runs the command.
     * <p>
     * The caller is responsible for {@linkplain ApplicationContext#close() closing} the context.
     * </p>
     * @param cls the Runnable command class
     * @param ctx the ApplicationContext that injects dependencies into the command
     * @param args the command line arguments
     * @param <R> The runnable type
     * @throws InitializationException if the specified command object does not have
     *          a {@link Command}, {@link Option} or {@link Parameters} annotation
     * @throws ExecutionException if the Runnable throws an exception
     */
    public static <R extends Runnable> void run(Class<R> cls, ApplicationContext ctx, String... args) {
        CommandLine commandLine = new CommandLine(cls, new MicronautFactory(ctx));
        commandLine.execute(args);
    }
    
    /**
     * Instantiates a new {@link ApplicationContext} for the {@link Environment#CLI} environment,
     * obtains an instance of the specified {@code Callable} or {@code Runnable} command
     * class from the context, injecting any beans as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally {@link CommandLine#execute(String...) executes} the command
     * and returns the resulting exit code.
     * <p>
     * The {@code ApplicationContext} is {@linkplain ApplicationContext#close() closed} before this method returns.
     * </p><p>
     * This is equivalent to:
     * </p>
     * <pre>{@code
     * try (ApplicationContext context = ApplicationContext.build(clazz, Environment.CLI).start()) {
     *     return new CommandLine(clazz, new MicronautFactory(context)).execute(args);
     * }
     * }</pre>
     * <p>
     * Applications that need to customize picocli behavior by calling one of the setter
     * methods on the {@code CommandLine} instance should use code like the above instead of this method.
     * For example:
     * </p>
     * <pre>{@code
     * // example of customizing picocli parser before invoking a command
     * try (ApplicationContext context = ApplicationContext.build(clazz, Environment.CLI).start()) {
     *     return new CommandLine(clazz, new MicronautFactory(context)).
     *          setUsageHelpAutoWidth(true).
     *          setCaseInsensitiveEnumValuesAllowed(true).
     *          execute(args);
     * }
     * }</pre>
     * @param clazz the Runnable or Callable command class
     * @param args the command line arguments
     * @return the exit code returned by {@link CommandLine#execute(String...)}
     */
    public static int execute(Class<?> clazz, String... args) {
        ApplicationContextBuilder builder = buildApplicationContext(clazz, args);
        try (ApplicationContext context = builder.start()) {
            return execute(clazz, context, args);
        }
    }

    /**
     * Obtains an instance of the specified {@code Callable} or {@code Runnable} command class
     * from the specified context, injecting any beans from the specified context as required,
     * then parses the specified command line arguments, populating fields and methods annotated
     * with picocli {@link Option @Option} and {@link Parameters @Parameters}
     * annotations, and finally {@link CommandLine#execute(String...) executes} the command
     * and returns the resulting exit code.
     * <p>
     * The caller is responsible for {@linkplain ApplicationContext#close() closing} the context.
     * </p><p>
     * This is equivalent to:
     * </p>
     * <pre>{@code return new CommandLine(clazz, new MicronautFactory(context)).execute(args);}</pre>
     * @param clazz the Runnable or Callable command class
     * @param context the ApplicationContext that injects dependencies into the command
     * @param args the command line arguments
     * @return the exit code returned by {@link CommandLine#execute(String...)}
     */
    private static int execute(Class<?> clazz, ApplicationContext context, String... args) {
        return new CommandLine(clazz, new MicronautFactory(context)).execute(args);
    }

    private static ApplicationContextBuilder buildApplicationContext(Class<?> cls, String[] args) {
        io.micronaut.core.cli.CommandLine commandLine = io.micronaut.core.cli.CommandLine.parse(args);
        CommandLinePropertySource commandLinePropertySource = new CommandLinePropertySource(commandLine);
        return ApplicationContext
                .build(cls, Environment.CLI)
                .propertySources(commandLinePropertySource);
    }

}
