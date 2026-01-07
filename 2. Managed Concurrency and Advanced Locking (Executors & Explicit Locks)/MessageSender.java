class MessageSender implements Runnable {

    private final MessageQueue queue;
    private final String name;

    public MessageSender(MessageQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            String msg = name + " message " + i + " at " + System.currentTimeMillis();
            queue.put(msg);
            System.out.println("Produced: " + msg);
        }
    }
}
