package tin.p2p.serialization_layer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tin.p2p.utils.Constants;


public class SerializedObjectUnitTest {


    @Test
    public void whenNoDataPassedShouldBeLength4() {
        SerializedObject serializedObject = new SerializedObject(Constants.OPCODE_CONNECT_TO_NET, new byte[0]);

        Assertions.assertEquals(4, serializedObject.getDataLength());

    }


}



