import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MessageQueue {

    private final Queue<String> queue = new LinkedList<>();

    // Explicit lock instead of synchronized
    private final Lock lock = new ReentrantLock();

    // Condition replaces wait/notify
    private final Condition notEmpty = lock.newCondition();

    public void put(String message) {
        lock.lock();                     
        try {
            queue.add(message);          
            notEmpty.signalAll();        
        } finally {
            lock.unlock();               
        }
    }

    public String take() {
        lock.lock();                     
        try {
            while (queue.isEmpty()) {
                notEmpty.await();        
            }
            return queue.poll();         
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();               
        }
    }
}
