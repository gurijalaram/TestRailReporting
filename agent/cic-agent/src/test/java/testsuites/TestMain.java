package testsuites;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TestMain {
    public static Class defaultTest = AgentInstallSuite.class;

    public static void main(String[] args) {

        List<Class> testsToRun = new ArrayList<>();

        CommandLine cmd = parseCommandLineArguments(args);
        String tests = cmd.getOptionValue("tests");

        if (tests == null) {
            testsToRun.add(defaultTest);
        } else {
            Arrays.stream(tests.split(",")).forEach(testName -> {
                try {
                    testsToRun.add(Class.forName(testName));
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(String.format(
                        "Could not find class with name '%s'",
                        testName
                    ));
                }
            });
        }

        for (Class testClass : testsToRun) {
            run(testClass);
        }
    }

    private static CommandLine parseCommandLineArguments(String[] args) {

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();

        Option testsOption = new Option(
            "t",
            "tests",
            true,
            "The tests to run. This is a comma-delimited string that cannot include spaces."
        );
        testsOption.setRequired(false);
        options.addOption(testsOption);

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
            return null;
        }
    }

    private static void run(Class testClass) {

        final LauncherDiscoveryRequest request =
            LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build();

        final Launcher launcher = LauncherFactory.create();
        final SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        log.info("-------------------------------------------------------\n");
        log.info("T E S T S\n");
        log.info("-------------------------------------------------------");

        log.info("Number of tests to execute :- {}", summary.getTestsFoundCount());

        log.info("Number of tests succeeded :- {}", summary.getTestsSucceededCount());

        log.info("Number of tests skipped :- {}", summary.getTestsSkippedCount());

        log.info("Number of tests aborted :- {}", summary.getTestsAbortedCount());

        log.info("Number of tests failed :- {}", summary.getTestsFailedCount());

        summary.getFailures().forEach(failure -> log.info(String.format("Failure :- %s *** Exception :- %s", failure.getTestIdentifier().getDisplayName(), failure.getException())));
    }
}
