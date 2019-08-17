package io.micronaut.configuration.picocli;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import org.junit.Test;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParseResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class MicronautFactoryCommandTest {

    @Command(name = "top", subcommands = SubCommand.class)
    static class TopLevelCommand {

        @Option(names = "-x")
        List<Integer> integerList;

        @Option(names = "-s")
        SortedSet<String> sortedSet;

        @Parameters
        Set<File> fileSet;

        @Inject
        BeanB bService;
    }

    @Command(name = "sub")
    static class SubCommand {

        @Option(names = "-y")
        Map<String, Long> longMap;

        @Parameters
        Queue<Path> pathQueue;

        @Inject
        BeanA aService;
    }

    @Singleton
    static class BeanA {
        @Value("${a.name:hello}")
        String injectedValue;
    }

    @Factory
    static class BBeanFactory {
        @Bean
        public BeanB createB() {
            return new BeanB();
        }
    }

    static class BeanB {
        static AtomicInteger count = new AtomicInteger();
        final int seq = count.incrementAndGet();
    }

    private MicronautFactory createFactory() {
        ApplicationContext applicationContext = ApplicationContext.run(PropertySource.of(
                "test",
                CollectionUtils.mapOf("a.name", "testValue")
        ));
        MicronautFactory factory = new MicronautFactory(applicationContext);
        return factory;
    }

    @Test
    public void testTopCommandCreate() {
        MicronautFactory factory = createFactory();
        CommandLine commandLine = new CommandLine(TopLevelCommand.class, factory);
        TopLevelCommand userObject = commandLine.getCommand();
        assertNull(userObject.integerList);
        assertNull(userObject.sortedSet);
        assertNull(userObject.fileSet);
        assertNotNull(userObject.bService);
        assertTrue(userObject.bService.seq > 0);
    }

    @Test
    public void testSubCommandCreate() {
        CommandLine commandLine = new CommandLine(SubCommand.class, createFactory());
        SubCommand userObject = commandLine.getCommand();
        assertNull(userObject.longMap);
        assertNull(userObject.pathQueue);
        assertNotNull(userObject.aService);
        assertEquals("testValue", userObject.aService.injectedValue);
    }

    @Test
    public void testCommandInvocation() {
        CommandLine cmd = new CommandLine(TopLevelCommand.class, createFactory());
        ParseResult parseResult = cmd.parseArgs(
                "-x=1", "-x", "2", "-sB", "-s=C", "-s", "A", "file1", "file2", "sub", "-y=b=3", "-y", "a=4", "path1", "path2");

        TopLevelCommand top = (TopLevelCommand) parseResult.commandSpec().userObject();
        assertNotNull(top.integerList);
        assertEquals(2, top.integerList.size());
        assertEquals(Arrays.asList(1, 2), top.integerList);

        assertNotNull(top.fileSet);
        assertEquals(2, top.fileSet.size());
        assertEquals(new LinkedHashSet<>(Arrays.asList(new File("file1"), new File("file2"))), top.fileSet);

        assertNotNull(top.sortedSet);
        assertEquals(3, top.sortedSet.size());
        assertEquals(new TreeSet<>(Arrays.asList("A", "B", "C")), top.sortedSet);

        assertNotNull(top.bService);
        assertTrue(top.bService.seq > 0);

        assertTrue(parseResult.hasSubcommand());
        SubCommand sub = (SubCommand) parseResult.subcommand().commandSpec().userObject();
        assertNotNull(sub.longMap);
        assertEquals(2, sub.longMap.size());
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("b", 3L);
        map.put("a", 4L);
        assertEquals(map, sub.longMap);

        assertNotNull(sub.pathQueue);
        assertEquals(2, sub.pathQueue.size());
        assertEquals(new LinkedList(Arrays.asList(Paths.get("path1"), Paths.get("path2"))), sub.pathQueue);

        assertNotNull(sub.aService);
        assertEquals("testValue", sub.aService.injectedValue);
    }
}
