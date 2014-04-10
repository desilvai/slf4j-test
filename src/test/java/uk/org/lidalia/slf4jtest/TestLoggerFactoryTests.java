package uk.org.lidalia.slf4jtest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.org.lidalia.slf4jext.Level;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static uk.org.lidalia.slf4jext.Level.WARN;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;
import static uk.org.lidalia.slf4jtest.LoggingEvent.trace;
import static uk.org.lidalia.slf4jtest.TestLoggerFactory.getInstance;
import static uk.org.lidalia.test.ShouldThrow.shouldThrow;

@RunWith(PowerMockRunner.class)
public class TestLoggerFactoryTests {

    @Test
    public void getLoggerDifferentNames() throws Exception {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name2");

        assertNotSame(logger1, logger2);
    }

    @Test
    public void getLoggerSameNames() throws Exception {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name1");

        assertSame(logger1, logger2);
    }

    @Test
    public void staticGetTestLoggerStringReturnsSame() throws Exception {
        TestLogger logger1 = TestLoggerFactory.getTestLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name1");

        assertSame(logger1, logger2);
    }

    @Test
    public void staticGetTestLoggerClassReturnsSame() throws Exception {
        TestLogger logger1 = TestLoggerFactory.getTestLogger(String.class);
        TestLogger logger2 = getInstance().getLogger("java.lang.String");

        assertSame(logger1, logger2);
    }

    @Test
    public void clear() throws Exception {
        TestLogger logger1 = getInstance().getLogger("name1");
        logger1.trace("hello");
        Assert.assertTrue(logger1.getLoggingEvents().size() == 1);
        TestLogger logger2 = getInstance().getLogger("name2");
        logger2.trace("world");
        Assert.assertTrue(logger2.getLoggingEvents().size() == 1);

        TestLoggerFactory.clear();

        Assert.assertTrue(logger1.getLoggingEvents().size() == 0);
        Assert.assertTrue(logger2.getLoggingEvents().size() == 0);
        Assert.assertTrue(TestLoggerFactory.getLoggingEvents().size() == 0);
    }

    @Test
    public void getAllLoggingEvents() throws Exception {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name2");
        logger1.trace("hello");
        logger2.trace("world");
        logger1.trace("here");
        logger2.trace("I am");

        Assert.assertEquals(asList(trace("hello"),
                                   trace("world"),
                                   trace("here"),
                                   trace("I am")), 
                            TestLoggerFactory.getLoggingEvents());
    }

    @Test
    public void getAllLoggingEventsDoesNotAddToMultipleLoggers() throws Exception {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name2");
        logger1.trace("hello");
        logger2.trace("world");

        Assert.assertEquals(asList(trace("hello")), logger1.getLoggingEvents());
        Assert.assertEquals(asList(trace("world")), logger2.getLoggingEvents());
    }

    @Test
    public void getAllLoggingEventsDoesNotGetEventsForLoggersNotEnabled() {
        TestLogger logger = getInstance().getLogger("name1");
        logger.setEnabledLevels(WARN);
        logger.info("hello");

        Assert.assertTrue(TestLoggerFactory.getLoggingEvents().size() == 0);
    }

    @Test
    public void getAllTestLoggers() {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLogger logger2 = getInstance().getLogger("name2");
        Map<String, TestLogger> expected = new HashMap<String, TestLogger>();
        expected.put("name1", logger1);
        expected.put("name2", logger2);
        Assert.assertEquals(expected, TestLoggerFactory.getAllTestLoggers());
    }

    @Test
    public void clearDoesNotRemoveLoggers() {
        TestLogger logger1 = getInstance().getLogger("name1");
        TestLoggerFactory.clear();

        Map<String, TestLogger> expected = new HashMap<String, TestLogger>();
        expected.put("name1", logger1);
        Assert.assertEquals(expected, TestLoggerFactory.getAllTestLoggers());
    }

    @Test
    public void resetRemovesAllLoggers() {
        getInstance().getLogger("name1");

        TestLoggerFactory.reset();

        final Map<String, TestLogger> emptyMap = Collections.emptyMap();
        Assert.assertEquals(emptyMap, TestLoggerFactory.getAllTestLoggers());
    }

    @Test
    public void resetRemovesAllLoggingEvents() {
        getInstance().getLogger("name1").info("hello");

        TestLoggerFactory.reset();

    }

    @Test
    public void getLoggingEventsReturnsCopyNotView() {
        getInstance().getLogger("name1").debug("hello");
        List<LoggingEvent> loggingEvents = TestLoggerFactory.getLoggingEvents();
        getInstance().getLogger("name1").info("world");
        Assert.assertEquals(asList(debug("hello")), loggingEvents);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getLoggingEventsReturnsUnmodifiableList() {
        List<LoggingEvent> loggingEvents = TestLoggerFactory.getLoggingEvents();
        loggingEvents.add(debug("hello"));
    }

    @Test
    public void getAllLoggersReturnsCopyNotView() {
        TestLogger logger1 = getInstance().getLogger("name1");
        Map<String, TestLogger> allTestLoggers = TestLoggerFactory.getAllTestLoggers();
        getInstance().getLogger("name2");

        Map<String, TestLogger> expected = new HashMap<String, TestLogger>();
        expected.put("name1", logger1);
        Assert.assertEquals(expected, allTestLoggers);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAllLoggersReturnsUnmodifiableList() {
        Map<String, TestLogger> allTestLoggers = TestLoggerFactory.getAllTestLoggers();
        allTestLoggers.put("newlogger", new TestLogger("newlogger", TestLoggerFactory.getInstance()));
    }

    @Test
    public void getLoggingEventsOnlyReturnsEventsLoggedInThisThread() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                TestLoggerFactory.getTestLogger("name1").info("hello");
            }
        });
        t.start();
        t.join();
        Assert.assertTrue(TestLoggerFactory.getLoggingEvents().size() == 0);
    }

    @Test
    public void getAllLoggingEventsReturnsEventsLoggedInAllThreads() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                TestLoggerFactory.getTestLogger("name1").info("message1");
            }
        });
        t.start();
        t.join();
        TestLoggerFactory.getTestLogger("name1").info("message2");
        Assert.assertEquals(asList(info("message1"), 
                                   info("message2")), 
                            TestLoggerFactory.getAllLoggingEvents());
    }

    @Test
    public void clearOnlyClearsEventsLoggedInThisThread() throws InterruptedException {
        final TestLogger logger = TestLoggerFactory.getTestLogger("name");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("hello");
            }
        });
        t.start();
        t.join();
        TestLoggerFactory.clear();
        Assert.assertEquals(asList(info("hello")), TestLoggerFactory.getAllLoggingEvents());
    }

    @Test
    public void clearAllClearsEventsLoggedInAllThreads() throws InterruptedException {
        final TestLogger logger1 = TestLoggerFactory.getTestLogger("name1");
        final TestLogger logger2 = TestLoggerFactory.getTestLogger("name2");
        logger1.info("hello11");
        logger2.info("hello21");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                logger1.info("hello12");
                logger2.info("hello22");
                TestLoggerFactory.clearAll();
            }
        });
        t.start();
        t.join();
        Assert.assertTrue(TestLoggerFactory.getLoggingEvents().size() == 0);
        Assert.assertTrue(TestLoggerFactory.getAllLoggingEvents().size() == 0);
        Assert.assertTrue(logger1.getLoggingEvents().size() == 0);
        Assert.assertTrue(logger1.getAllLoggingEvents().size() == 0);
        Assert.assertTrue(logger2.getLoggingEvents().size() == 0);
        Assert.assertTrue(logger2.getAllLoggingEvents().size() == 0);
    }

    @Test
    public void defaultPrintLevelIsOff() {
        Assert.assertEquals(Level.OFF, TestLoggerFactory.getInstance().getPrintLevel());
    }

    @Test
    @PrepareForTest(TestLoggerFactory.class)
    public void printLevelTakenFromOverridableProperties() throws Exception {
        final OverridableProperties properties = mock(OverridableProperties.class);
        whenNew(OverridableProperties.class).withArguments("slf4jtest").thenReturn(properties);
        when(properties.getProperty("print.level", "OFF")).thenReturn("INFO");

        Assert.assertEquals(Level.INFO, TestLoggerFactory.getInstance().getPrintLevel());
    }

    @Test
    @PrepareForTest(TestLoggerFactory.class)
    public void printLevelInvalidInOverridableProperties() throws Exception {
        final OverridableProperties properties = mock(OverridableProperties.class);
        whenNew(OverridableProperties.class).withArguments("slf4jtest").thenReturn(properties);
        final String invalidLevelName = "nonsense";
        when(properties.getProperty("print.level", "OFF")).thenReturn(invalidLevelName);

        final IllegalStateException illegalStateException = shouldThrow(IllegalStateException.class, new Runnable() {
            @Override
            public void run() {
                TestLoggerFactory.getInstance();
            }
        });
        Assert.assertEquals("Invalid level name in property print.level of "
                                + "file slf4jtest.properties or System property"
                                + " slf4jtest.print.level",
                            illegalStateException.getMessage());
        Assert.assertTrue(illegalStateException.getCause() instanceof IllegalArgumentException);
        Assert.assertEquals("No enum constant " + Level.class.getName() 
                                + "."+ invalidLevelName,
                            illegalStateException.getCause().getMessage());

    }

    @Test
    public void setLevel() {
        for (Level printLevel: Level.values()) {
            TestLoggerFactory.getInstance().setPrintLevel(printLevel);
            Assert.assertEquals(printLevel, TestLoggerFactory.getInstance().getPrintLevel());
        }
    }

    @After
    public void resetLoggerFactory() {
        try {
            TestLoggerFactory.reset();
        } catch (IllegalStateException e) {
            // ignore
        }
    }
}
