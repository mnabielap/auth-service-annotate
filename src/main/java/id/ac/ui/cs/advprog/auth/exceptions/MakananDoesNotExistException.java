package id.ac.ui.cs.advprog.auth.exceptions;

public class MakananDoesNotExistException extends RuntimeException {

    public MakananDoesNotExistException(Integer id) {
        super("Makanan with id " + id + " does not exist");
    }


}
