package framework.tests;

import framework.annotations.After;
import framework.annotations.Before;
import framework.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("java:S112")
public class Tests {
    private static final Logger LOGGER = LogManager.getLogger(Tests.class);

    @Before
    void before() {
        LOGGER.info("Before testing.");
    }

    @Test
    void test1() {
        LOGGER.info("Test1 is in progress.");
    }

    @Test
    void test2() {
        LOGGER.info("Test2 is in progress.");
        throw new RuntimeException("Exception while test2");
    }

    @After
    void after() {
        LOGGER.info("After testing.");
    }
}
