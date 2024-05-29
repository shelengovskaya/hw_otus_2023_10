package aop.proxy;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLoggingClass = Ioc.createTestLoggingClass();
        testLoggingClass.calculation(6);
        testLoggingClass.calculation(1, 2);
        testLoggingClass.calculation(1, 2, "3");
    }
}
