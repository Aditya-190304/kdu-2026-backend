import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorProducerConsumer {

    public static void main(String[] args) throws InterruptedException {

        MessageQueue queue = new MessageQueue();

        // Thread pools
        ExecutorService producerPool = Executors.newFixedThreadPool(3);
        ExecutorService consumerPool = Executors.newFixedThreadPool(3);

        // Start producers
        for (int i = 1; i <= 3; i++) {
            producerPool.submit(new MessageSender(queue, "Sender-" + i));
        }

        // Start consumers
        for (int i = 1; i <= 3; i++) {
            consumerPool.submit(new MessageReceiver(queue, "Receiver-" + i));
        }

        // Graceful shutdown
        producerPool.shutdown();
        consumerPool.shutdown();

        producerPool.awaitTermination(1, TimeUnit.MINUTES);
        consumerPool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All tasks completed");
    }
}
