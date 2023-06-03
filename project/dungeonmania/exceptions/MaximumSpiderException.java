package dungeonmania.exceptions;

public class MaximumSpiderException extends RuntimeException {
    public MaximumSpiderException() {
        super("Maximum number of spiders already reached\n");
    }
}
