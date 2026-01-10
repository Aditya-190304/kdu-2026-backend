class MessageReceiver implements Runnable {
    private final MessageQueue queue;
    private final String name;

    public MessageReceiver(MessageQueue queue, String name) {
        this.queue = queue;
        this.name = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            String msg = queue.take();
            System.out.println(name + " consumed: " + msg);
        }
    }
}
