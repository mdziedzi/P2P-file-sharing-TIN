package tin.p2p.parser_layer;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParserOutput extends Thread implements Output {
    final static Logger log = Logger.getLogger(ParserOutput.class.getName());

    private tin.p2p.socket_layer.Output output;

    private ConcurrentLinkedQueue<SendableObject> sendableObjectsQueue = new ConcurrentLinkedQueue<>();

    public ParserOutput(tin.p2p.socket_layer.Output lowerLayerOutput) {
        this.output = lowerLayerOutput;
    }


    @Override
    public void addSendableObjectToQueue(SendableObject sendableObject) {
        this.sendableObjectsQueue.add(sendableObject);
    }

    public void sendLoop() {
        while (true) {
            SendableObject sendableObject = sendableObjectsQueue.poll();
            try {
                if (sendableObject != null) {
                    output.send(sendableObject.getDataToSend());
                }
                Thread.sleep(100);
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

