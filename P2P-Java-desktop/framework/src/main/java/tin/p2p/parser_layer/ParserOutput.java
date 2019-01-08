package tin.p2p.parser_layer;



import java.io.EOFException;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class ParserOutput extends Thread implements Output {
    final static Logger log = Logger.getLogger(ParserOutput.class.getName());
    private volatile boolean running = true;

    private tin.p2p.socket_layer.Output output;

    private final Queue<SendableObject> sendableObjectsQueue = new ConcurrentLinkedQueue<>();

    public ParserOutput(tin.p2p.socket_layer.Output lowerLayerOutput) {
        this.output = lowerLayerOutput;
    }


    @Override
    public void addSendableObjectToQueue(SendableObject sendableObject) {
        synchronized (sendableObjectsQueue) {
            this.sendableObjectsQueue.add(sendableObject);
            this.sendableObjectsQueue.notifyAll();
        }
    }

    private void sendLoop() {
        while (running) {
            SendableObject sendableObject = sendableObjectsQueue.poll();
            try {
                if (sendableObject != null) {
                    output.send(sendableObject.getDataToSend());
                } else {
                    synchronized (sendableObjectsQueue) {
                        sendableObjectsQueue.wait();
                    }
                }
            } catch (EOFException | InterruptedException ex) {
                terminate();
                ex.printStackTrace();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                output.closeConnection();
                return;
            }
        }
    }


    @Override
    public void run() {
        sendLoop();
    }

    @Override
    public void terminate() {
        running = false;
        synchronized (sendableObjectsQueue) {
            sendableObjectsQueue.notifyAll();
        }
        output.closeConnection();
    }
}

