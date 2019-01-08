package tin.p2p.exceptions;

public class UnsuccessfulConnectionException extends RuntimeException {
    public UnsuccessfulConnectionException(Exception e) {

        e.printStackTrace();
    }
} 

