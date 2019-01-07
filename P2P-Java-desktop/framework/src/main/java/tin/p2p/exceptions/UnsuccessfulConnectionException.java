package tin.p2p.exceptions;

import java.io.IOException;

public class UnsuccessfulConnectionException extends RuntimeException {
    public UnsuccessfulConnectionException(Exception e) {

        e.printStackTrace();
    }
} 

