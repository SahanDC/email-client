package assignment;

import java.util.LinkedList;

public class BlockingQueue {
    private LinkedList<Email> linkedList;
    private final int max;

    public BlockingQueue(int max) {
        this.max = max;
        linkedList = new LinkedList<>();
    }

    public synchronized void enqueue(Email email){
        while (linkedList.size() == max) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        linkedList.add(email);
        notifyAll();
        return;
    }

    public synchronized Email dequeue() {
        while (linkedList.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Email email = linkedList.remove();
        return email;
    }
}
