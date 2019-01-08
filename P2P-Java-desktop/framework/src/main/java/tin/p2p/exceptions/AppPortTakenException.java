package tin.p2p.exceptions;

import java.net.BindException;

public class AppPortTakenException extends RuntimeException {
    public AppPortTakenException(BindException e) {
        e.printStackTrace();
    }
} 

