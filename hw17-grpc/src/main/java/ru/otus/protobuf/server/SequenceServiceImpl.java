package ru.otus.protobuf.server;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.GenerateRequest;
import ru.otus.protobuf.GenerateResponse;
import ru.otus.protobuf.SequenceServiceGrpc;

import java.util.stream.IntStream;

public class SequenceServiceImpl extends SequenceServiceGrpc.SequenceServiceImplBase {
    @Override
    public void generate(GenerateRequest request, StreamObserver<GenerateResponse> responseObserver) {
        IntStream.range(request.getFirstValue(), request.getLastValue() + 1)
                .forEach(value -> {
                    responseObserver.onNext(GenerateResponse.newBuilder().setNumber(value).build());
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
        responseObserver.onCompleted();
    }
}
