package trainer.kafka.l3;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Objects;

// @task kafka.l3.BatchListener
// @tags kafka,batch-listener,throughput
// @time 25m
// @src  new
public final class BatchListener {

    public static final String TOPIC = "trainer.orders.batched";
    private final OrderProcessor processor;

    public BatchListener(OrderProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor");
    }

    /**
     * batch-режим отдаёт СРАЗУ несколько записей за один вызов — выше throughput, но при
     * ошибке в середине списка приходится решать: коммитить частично или откатывать всё.
     */
    @KafkaListener(id = "batch-order-listener", topics = TOPIC, containerFactory = "batchListenerContainerFactory")
    public void onMessages(List<ConsumerRecord<String, String>> records) {
        Objects.requireNonNull(records, "records");

        // ---8<--- solution
        for (ConsumerRecord<String, String> record : records) {
            processor.process(record.key(), record.value());
        }
        // --->8--- solution
    }

    @FunctionalInterface
    public interface OrderProcessor {
        void process(String orderId, String payload);
    }
}
