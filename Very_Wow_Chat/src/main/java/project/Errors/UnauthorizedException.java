package project.Errors;

/**
 * A custom exception class for throwing and catching errors
 * @author Vilhelml
 *
 */
public class UnauthorizedException extends Exception{
	
	public UnauthorizedException (String msg) {
		super(msg);
	}
}
