package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorEvenSecondTest {

    @Test
    void processEvenSecond() {
        var processorEvenSecond = new ProcessorEvenSecond(() -> LocalDateTime.parse("2024-05-21T00:00"));
        var message = new Message.Builder(0).build();
        assertThrows(RuntimeException.class, () -> processorEvenSecond.process(message));
    }

    @Test
    void processOddSecond() {
        var processorEvenSecond = new ProcessorEvenSecond(() -> LocalDateTime.parse("2024-05-21T00:00:01"));
        var message = new Message.Builder(0).build();
        assertDoesNotThrow(() -> processorEvenSecond.process(message));
    }
}