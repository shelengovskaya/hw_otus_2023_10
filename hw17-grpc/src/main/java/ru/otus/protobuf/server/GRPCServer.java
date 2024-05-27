package ru.otus.protobuf.server;

import io.grpc.ServerBuilder;

import java.io.IOException;

import static ru.otus.protobuf.Constants.SERVER_PORT;

public class GRPCServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        var remoteSequenceService = new SequenceServiceImpl();
        var server = ServerBuilder.forPort(SERVER_PORT).addService(remoteSequenceService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
