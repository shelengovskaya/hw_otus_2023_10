package framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.annotations.After;
import framework.annotations.Before;
import framework.annotations.Test;
import framework.statistics.TestStatistics;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"java:S112", "java:S3011"})
public class TestRunner {
    private static final Logger LOGGER = LogManager.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void runTests(Class<?> clazz) {
        TestStatistics testStatistics = new TestStatistics();

        List<Method> beforeMethods = getMethods(clazz, Before.class);
        List<Method> afterMethods = getMethods(clazz, After.class);
        List<Method> testMethods = getMethods(clazz, Test.class);

        testMethods.forEach(testMethod -> {
            Object instance = createInstance(clazz);
            beforeMethods.forEach(beforeMethod -> invokeMethod(instance, beforeMethod));
            try {
                invokeMethod(instance, testMethod);
                testStatistics.increaseSuccessTestsCount();
            } catch (Exception e) {
                testStatistics.increaseFailedTestsCount();
            }
            afterMethods.forEach(afterMethod -> invokeMethod(instance, afterMethod));
        });

        printStatistics(testStatistics);
    }

    private static List<Method> getMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<>();
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        });
        return methods;
    }

    private static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static void invokeMethod(Object object, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(object);
            method.setAccessible(false);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printStatistics(TestStatistics testStatistics) {
        try {
            String testStatisticsJson = new ObjectMapper().writeValueAsString(testStatistics);
            LOGGER.info(testStatisticsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
