package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalTime;

public class ProcessorEvenSecond implements Processor {
    private final TimeProvider timeProvider;

    public ProcessorEvenSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        int currSecond = timeProvider.getDate().getSecond();
        if (currSecond % 2 == 0) {
            throw new RuntimeException("Even second.");
        }
        return null;
    }
}
