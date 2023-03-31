package id.ac.ui.cs.advprog.auth.exceptions;

public class DataHarianDoesNotExistException extends RuntimeException {
    public DataHarianDoesNotExistException(Integer id) {
        super("Order with id " + id + " does not exist");
    }
}
