package cyberCopApplication;

// custom exception class extending RuntimeException
public class DataException extends RuntimeException{

	public DataException(String message) {
		super(message);
	}
}