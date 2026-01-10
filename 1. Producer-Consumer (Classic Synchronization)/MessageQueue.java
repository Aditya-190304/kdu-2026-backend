import java.util.*;

class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();

    public synchronized void put(String message) {
        queue.add(message);
        notifyAll();
    }

    public synchronized String take() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return queue.poll();
    }
}
