package project.errors;

import org.springframework.http.HttpStatus;

/**
 * A custom exception class for throwing and catching errors
 * 
 * @author Vilhelml
 */
public class NotFoundException extends HttpException {

	private static final long serialVersionUID = 9091563554072961356L;

	public NotFoundException(String msg) {
		super(msg);
		super.setStatus(HttpStatus.NOT_FOUND);
	}
}
