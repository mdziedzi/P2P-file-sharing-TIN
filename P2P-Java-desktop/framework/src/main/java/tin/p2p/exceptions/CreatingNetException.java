package tin.p2p.exceptions;

public class CreatingNetException extends RuntimeException {
    public CreatingNetException(Exception e) {
        e.printStackTrace();
    }
} 

