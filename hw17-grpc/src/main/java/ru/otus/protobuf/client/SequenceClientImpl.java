package ru.otus.protobuf.client;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.GenerateRequest;
import ru.otus.protobuf.GenerateResponse;
import ru.otus.protobuf.SequenceServiceGrpc;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.otus.protobuf.Constants.SERVER_HOST;
import static ru.otus.protobuf.Constants.SERVER_PORT;

public class SequenceClientImpl {
    public void run() throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = SequenceServiceGrpc.newStub(channel);

        AtomicInteger number = new AtomicInteger();
        stub.generate(GenerateRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), new StreamObserver<>() {
            @Override
            public void onNext(GenerateResponse response) {
                int responseNumber = response.getNumber();
                number.set(responseNumber);
                System.out.println("new value: " + responseNumber);
            }

            @Override
            public void onError(Throwable e) {
                System.err.println();
            }

            @Override
            public void onCompleted() {
                System.out.println("request completed");
            }
        });

        int currentValue = 0;
        int prevNumber = -1;
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1000L);
            currentValue += 1;
            int currentNumber = number.get();
            if (prevNumber != currentNumber) {
                currentValue += currentNumber;
                prevNumber = currentNumber;
            }
            System.out.println(System.currentTimeMillis() + " currentValue: " + currentValue);
        }

        channel.shutdown();
    }
}
