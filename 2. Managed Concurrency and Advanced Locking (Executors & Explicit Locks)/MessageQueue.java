import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public void put(String message) {
        lock.lock();
        try {
            queue.add(message);
        } finally {
            lock.unlock();
        }
    }

    public String take() {
        while (true) {
            lock.lock();
            try {
                if (!queue.isEmpty()) {
                    return queue.poll();
                }
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(10); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }
}
