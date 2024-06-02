package ru.otus.protobuf.client;

public class GRPCClient {
    public static void main(String[] args) throws InterruptedException {
        new SequenceClientImpl().run();
    }
}
