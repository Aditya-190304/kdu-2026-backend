public class ProducerConsumerDemo {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue();

        for (int i = 1; i <= 3; i++) {
            new Thread(new MessageSender(queue, "Sender-" + i)).start();
            new Thread(new MessageReceiver(queue, "Receiver-" + i)).start();
        }
    }
}
