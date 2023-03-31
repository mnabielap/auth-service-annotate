package id.ac.ui.cs.advprog.auth.exceptions;

public class MakananDoesNotExistException extends RuntimeException {

    public MakananDoesNotExistException(Integer id) {
        super("Medicine with id " + id + " does not exist");
    }


}
