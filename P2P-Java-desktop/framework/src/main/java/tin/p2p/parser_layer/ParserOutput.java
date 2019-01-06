package tin.p2p.parser_layer;



import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class ParserOutput extends Thread implements Output {
    final static Logger log = Logger.getLogger(ParserOutput.class.getName());

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
        while (true) { // todo flaga
            SendableObject sendableObject = sendableObjectsQueue.poll();
            try {
                if (sendableObject != null) {
                    output.send(sendableObject.getDataToSend());
                } else {
                    synchronized (sendableObjectsQueue) { // todo zastanowić się czy można nie używać bloku synchronized albo inny typ kolejki
                        sendableObjectsQueue.wait();
                    }
                }
            } catch (IOException | InterruptedException e) {
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
}

