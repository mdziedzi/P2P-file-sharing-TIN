package tin.p2p.socket_layer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.net.Socket;

import static org.mockito.MockitoAnnotations.initMocks;

public class SocketInputUnitTest {

    @InjectMocks
    private SocketInput socketInput;

    @Mock
    private Socket socket;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        socket = new Socket();
        socketInput = new SocketInput(socket);
        initMocks(this);
    }

    @Test
    public void whenSocketClosedShouldBeExceptionThrown() throws Exception {
        thrown.expect(IOException.class);
//        socketInput.getNextInt();
        socket.close();
    }
} 

