package assignment;

public class Save extends Thread{
    private BlockingQueue blockingQueue;
    private volatile boolean isRunning;

    public Save(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning){
            Email email = blockingQueue.dequeue();

            // check whether its the last email
            if (email.getRecipientEmail().equals("-1")) return;
        }
        return;
    }
    public void stopThread(){
        this.isRunning = false;
    }
}
