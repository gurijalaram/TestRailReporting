package testsuites;

import io.qameta.allure.junit4.AllureJunit4;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestMain {
    public static Class defaultTest = DdsApiSuite.class;

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

        JUnitCore runner = new JUnitCore();
        runner.addListener(new ExecutionListener());
        runner.addListener(new AllureJunit4());
        Result result = runner.run(testClass);

        // Report failures that occurred during testing.
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.getMessage());
            System.out.println(failure.getTestHeader());
            System.out.println(failure.getTrace());
        }
    }

    static class ExecutionListener extends RunListener {

        /**
         * Called before any tests have been run.
         */
        public void testRunStarted(Description description) {
            System.out.println("Number of tests to execute : " + description.testCount());
        }

        /**
         * Called when all tests have finished.
         */
        public void testRunFinished(Result result) {
            System.out.println("Number of tests executed : " + result.getRunCount());
        }

        /**
         * Called when an atomic test is about to be started.
         */
        public void testStarted(Description description) {
            System.out.println("Starting execution of test case : " + description.getMethodName());
        }

        /**
         * Called when an atomic test has finished, whether the test succeeds or fails.
         */
        public void testFinished(Description description) {
            System.out.println("Finished execution of test case : " + description.getMethodName());
        }

        /**
         * Called when an atomic test fails.
         */
        public void testFailure(Failure failure) {
            System.out.println("Execution of test case failed : " + failure.getMessage());
        }

        /**
         * Called when a test will not be run, generally because a test method is annotated with Ignore.
         */
        public void testIgnored(Description description) {
            System.out.println("Execution of test case ignored : " + description.getMethodName());
        }
    }
}
