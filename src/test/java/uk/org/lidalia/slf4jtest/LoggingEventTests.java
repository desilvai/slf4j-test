package uk.org.lidalia.slf4jtest;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static uk.org.lidalia.slf4jext.Level.DEBUG;
import static uk.org.lidalia.slf4jext.Level.ERROR;
import static uk.org.lidalia.slf4jext.Level.INFO;
import static uk.org.lidalia.slf4jext.Level.TRACE;
import static uk.org.lidalia.slf4jext.Level.WARN;
import static uk.org.lidalia.slf4jtest.LoggingEvent.debug;
import static uk.org.lidalia.slf4jtest.LoggingEvent.error;
import static uk.org.lidalia.slf4jtest.LoggingEvent.info;
import static uk.org.lidalia.slf4jtest.LoggingEvent.trace;
import static uk.org.lidalia.slf4jtest.LoggingEvent.warn;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.StringStartsWith;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Marker;

import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.test.SystemOutputRule;


@RunWith(JUnitParamsRunner.class)
public class LoggingEventTests
{

    private static final Map<String, String> emptyMap = Collections.unmodifiableMap(new TreeMap<String, String>());

    @Rule
    public SystemOutputRule systemOutputRule = new SystemOutputRule();

    Level level = Level.TRACE;
    Map<String, String> mdc = new HashMap<>();
    {
        mdc.put("key", "value");
    }

    Marker marker = mock(Marker.class);
    Throwable throwable = new Throwable();
    String message = "message";
    Object arg1 = "arg1";
    Object arg2 = "arg2";
    List<Object> args = asList(arg1, arg2);


    @Test
    public void constructorMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level, message, arg1, arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertTrue(event.getMdc().isEmpty());
        Assert.assertThat(event.getMarker(), isAbsent());
        assertThat(event.getThrowable(), isAbsent());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorThrowableMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              throwable,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(emptyMap, event.getMdc());
        assertThat(event.getMarker(), isAbsent());
        Assert.assertEquals(Optional.of(throwable), event.getThrowable());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMarkerMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              marker,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(emptyMap, event.getMdc());
        Assert.assertEquals(Optional.of(marker), event.getMarker());
        assertThat(event.getThrowable(), isAbsent());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMarkerThrowableMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              marker,
                                              throwable,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(emptyMap, event.getMdc());
        Assert.assertEquals(Optional.of(marker), event.getMarker());
        Assert.assertEquals(Optional.of(throwable), event.getThrowable());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMdcMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level, mdc, message, arg1, arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(mdc, event.getMdc());
        assertThat(event.getMarker(), isAbsent());
        assertThat(event.getThrowable(), isAbsent());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMdcThrowableMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              mdc,
                                              throwable,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(mdc, event.getMdc());
        assertThat(event.getMarker(), isAbsent());
        Assert.assertEquals(Optional.of(throwable), event.getThrowable());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMdcMarkerMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              mdc,
                                              marker,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(mdc, event.getMdc());
        Assert.assertEquals(Optional.of(marker), event.getMarker());
        assertThat(event.getThrowable(), isAbsent());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void constructorMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              mdc,
                                              marker,
                                              throwable,
                                              message,
                                              arg1,
                                              arg2);
        Assert.assertEquals(level, event.getLevel());
        Assert.assertEquals(mdc, event.getMdc());
        Assert.assertEquals(Optional.of(marker), event.getMarker());
        Assert.assertEquals(Optional.of(throwable), event.getThrowable());
        Assert.assertEquals(message, event.getMessage());
        Assert.assertEquals(args, event.getArguments());
    }


    @Test
    public void traceMessageArgs()
    {
        LoggingEvent event = trace(message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceThrowableMessageArgs()
    {
        LoggingEvent event = trace(throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMarkerMessageArgs()
    {
        LoggingEvent event = trace(marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMarkerThrowableMessageArgs()
    {
        LoggingEvent event = trace(marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMdcMessageArgs()
    {
        LoggingEvent event = trace(mdc, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 mdc,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMdcThrowableMessageArgs()
    {
        LoggingEvent event = trace(mdc, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 mdc,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMdcMarkerMessageArgs()
    {
        LoggingEvent event = trace(mdc, marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 mdc,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void traceMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = trace(mdc, marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(TRACE,
                                                 mdc,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMessageArgs()
    {
        LoggingEvent event = debug(message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugThrowableMessageArgs()
    {
        LoggingEvent event = debug(throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMarkerMessageArgs()
    {
        LoggingEvent event = debug(marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMarkerThrowableMessageArgs()
    {
        LoggingEvent event = debug(marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMdcMessageArgs()
    {
        LoggingEvent event = debug(mdc, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 mdc,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMdcThrowableMessageArgs()
    {
        LoggingEvent event = debug(mdc, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 mdc,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMdcMarkerMessageArgs()
    {
        LoggingEvent event = debug(mdc, marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 mdc,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void debugMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = debug(mdc, marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(DEBUG,
                                                 mdc,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMessageArgs()
    {
        LoggingEvent event = info(message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoThrowableMessageArgs()
    {
        LoggingEvent event = info(throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMarkerMessageArgs()
    {
        LoggingEvent event = info(marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMarkerThrowableMessageArgs()
    {
        LoggingEvent event = info(marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMdcMessageArgs()
    {
        LoggingEvent event = info(mdc, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO, mdc, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMdcThrowableMessageArgs()
    {
        LoggingEvent event = info(mdc, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 mdc,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMdcMarkerMessageArgs()
    {
        LoggingEvent event = info(mdc, marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 mdc,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void infoMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = info(mdc, marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(INFO,
                                                 mdc,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMessageArgs()
    {
        LoggingEvent event = warn(message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnThrowableMessageArgs()
    {
        LoggingEvent event = warn(throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMarkerMessageArgs()
    {
        LoggingEvent event = warn(marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMarkerThrowableMessageArgs()
    {
        LoggingEvent event = warn(marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMdcMessageArgs()
    {
        LoggingEvent event = warn(mdc, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN, mdc, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMdcThrowableMessageArgs()
    {
        LoggingEvent event = warn(mdc, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 mdc,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMdcMarkerMessageArgs()
    {
        LoggingEvent event = warn(mdc, marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 mdc,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void warnMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = warn(mdc, marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(WARN,
                                                 mdc,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMessageArgs()
    {
        LoggingEvent event = error(message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR, message, arg1, arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorThrowableMessageArgs()
    {
        LoggingEvent event = error(throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMarkerMessageArgs()
    {
        LoggingEvent event = error(marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMarkerThrowableMessageArgs()
    {
        LoggingEvent event = error(marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMdcMessageArgs()
    {
        LoggingEvent event = error(mdc, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 mdc,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMdcThrowableMessageArgs()
    {
        LoggingEvent event = error(mdc, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 mdc,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMdcMarkerMessageArgs()
    {
        LoggingEvent event = error(mdc, marker, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 mdc,
                                                 marker,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void errorMdcMarkerThrowableMessageArgs()
    {
        LoggingEvent event = error(mdc, marker, throwable, message, arg1, arg2);
        LoggingEvent expected = new LoggingEvent(ERROR,
                                                 mdc,
                                                 marker,
                                                 throwable,
                                                 message,
                                                 arg1,
                                                 arg2);
        Assert.assertEquals(expected, event);
    }


    @Test
    public void mdcIsSnapshotInTime()
    {
        Map<String, String> mdc = new HashMap<>();
        mdc.put("key", "value1");
        Map<String, String> mdcAtStart = new HashMap<>(mdc);
        LoggingEvent event = new LoggingEvent(level, mdc, message);
        mdc.put("key", "value2");
        Assert.assertEquals(mdcAtStart, event.getMdc());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void mdcNotModifiable() throws Throwable
    {
        Map<String, String> mdc = new HashMap<>();
        mdc.put("key", "value1");
        final LoggingEvent event = new LoggingEvent(level, mdc, message);
        event.getMdc().put("anything", "whatever");
    }


    @Test
    public void argsIsSnapshotInTime()
    {
        Object[] args = new Object[] { arg1, arg2 };
        Object[] argsAtStart = Arrays.copyOf(args, args.length);
        LoggingEvent event = new LoggingEvent(level, message, args);
        args[0] = "differentArg";
        Assert.assertEquals(asList(argsAtStart), event.getArguments());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void argsNotModifiable() throws Throwable
    {
        final LoggingEvent event = new LoggingEvent(level, message, arg1);
        event.getArguments().add(arg2);
    }


    @Test
    public void timestamp()
    {
        LoggingEvent event = info("Message");
        Assert.assertEquals(Instant.EPOCH, event.getTimestamp());
    }


    @Test(expected = IllegalStateException.class)
    public void creatingLoggerNotPresent()
    {
        info("message").getCreatingLogger();
    }


    @Test
    public void creatingLoggerPresent()
    {
        final TestLogger logger = TestLoggerFactory.getInstance()
                .getLogger("logger");
        logger.info("message");
        final LoggingEvent event = logger.getLoggingEvents().get(0);
        Assert.assertEquals(logger, event.getCreatingLogger());
    }


    @Test
    public void printToStandardOutNoThrowable()
    {
        LoggingEvent event = new LoggingEvent(INFO,
                                              "message with {}",
                                              "argument");
        event.print();

        Assert.assertEquals("1970-01-01T00:00:00.000Z ["
                          + Thread.currentThread().getName()
                          + "] INFO - message with argument" + lineSeparator(),
                      systemOutputRule.getSystemOut());
    }


    @Test
    public void printToStandardOutWithThrowable()
    {
        LoggingEvent event = new LoggingEvent(INFO, new Exception(), "message");
        event.print();

        StringBuilder expectedStart = new StringBuilder();
        expectedStart.append(DateTimeFormatter.ISO_INSTANT.format(Instant.EPOCH));
        expectedStart.append(Thread.currentThread().getName());
        expectedStart.append("] INFO - message");
        expectedStart.append(lineSeparator());
        expectedStart.append(Exception.class.getName());
        expectedStart.append(lineSeparator());
        expectedStart.append("\tat");
        
        assertThat(systemOutputRule.getSystemOut(),
                   StringStartsWith.startsWith(expectedStart.toString()));
    }


    @Test
    @Parameters({ "TRACE", "DEBUG", "INFO" })
    public void printInfoAndBelow(Level level)
    {
        LoggingEvent event = new LoggingEvent(level,
                                              "message with {}",
                                              "argument");
        event.print();
        Assert.assertTrue(systemOutputRule.getSystemOut().length() != 0);
        Assert.assertFalse(systemOutputRule.getSystemOut().equals(""));
        
        Assert.assertTrue(systemOutputRule.getSystemErr().length() == 0);
        Assert.assertTrue(systemOutputRule.getSystemErr().equals(""));
    }


    @Test
    @Parameters({ "WARN", "ERROR" })
    public void printWarnAndAbove(Level level)
    {
        LoggingEvent event = new LoggingEvent(level,
                                              "message with {}",
                                              "argument");
        event.print();
        
        Assert.assertTrue(systemOutputRule.getSystemOut().length() == 0);
        Assert.assertTrue(systemOutputRule.getSystemErr().length() != 0);
    }


    @Test
    public void nullArgument()
    {
        LoggingEvent event = new LoggingEvent(level,
                                              "message with null arg",
                                              null,
                                              null);
        Assert.assertEquals(event, new LoggingEvent(level,
                                                    "message with null arg",
                                                    Optional.empty(),
                                                    Optional.empty()));
    }


    @After
    public void reset()
    {
        TestLoggerFactory.reset();
    }


    @SuppressWarnings("unchecked")
    private Matcher<Optional<?>> isAbsent()
    {
        final Matcher<Optional<?>> optionalMatcher = Is.is(Optional.empty());
        return (Matcher<Optional<?>>) optionalMatcher;
    }
}
