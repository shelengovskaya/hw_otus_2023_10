package aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {
    static TestLoggingInterface createTestLoggingClass() {
        InvocationHandler handler =  new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLoggingClass;

        private final Set<Method> annotatedMethods;

        DemoInvocationHandler(TestLoggingInterface testLoggingClass) {
            this.testLoggingClass = testLoggingClass;
            this.annotatedMethods = getAnnotatedMethods();
        }
        
        private Set<Method> getAnnotatedMethods() {
            return Arrays.stream(TestLogging.class.getMethods())
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .collect(Collectors.toSet());
        }

        private boolean isEqualMethods(Method method1, Method method2) {
            if (!method1.getName().equals(method2.getName())) {
                return false;
            }
            if (method1.getParameterCount() != method2.getParameterCount()) {
                return false;
            }

            List<? extends Class<?>> parameterTypesMethod1 =
                    Arrays.stream(method1.getParameters())
                            .map(Parameter::getType)
                            .toList();
            List<? extends Class<?>> parameterTypesMethod2 =
                    Arrays.stream(method2.getParameters())
                            .map(Parameter::getType)
                            .toList();

            return parameterTypesMethod1.equals(parameterTypesMethod2);
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            for (Method annotatedMethod : annotatedMethods) {
                if (isEqualMethods(annotatedMethod, method)) {
                    System.out.printf(
                            "executed method: %s, param: %s\n",
                            method.getName(),
                            Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", "))
                    );
                }
            }

            return method.invoke(testLoggingClass, args);
        }
    }
}
