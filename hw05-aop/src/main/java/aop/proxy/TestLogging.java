package aop.proxy;

class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param) {
    };

    @Log
    @Override
    public void calculation(int param1, int param2) {
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
    }
}
