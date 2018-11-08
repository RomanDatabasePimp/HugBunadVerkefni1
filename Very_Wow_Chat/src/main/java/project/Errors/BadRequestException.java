package project.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.payloads.ErrorResponder;

/**
 * A custom exception class for throwing and catching errors
 * @author Vilhelml
 *
 */
public class BadRequestException extends HttpException{
	
	public BadRequestException (String msg) {
		super(msg);
		super.setStatus(HttpStatus.BAD_REQUEST);
	}
}
