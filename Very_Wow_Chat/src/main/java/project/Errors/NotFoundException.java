package project.Errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.persistance.entities.ErrorResponder;

/**
 * A custom exception class for throwing and catching errors
 * @author Vilhelml
 *
 */
public class NotFoundException extends HttpException{
	
	public NotFoundException (String msg) {
		super(msg);
		super.setStatus(HttpStatus.NOT_FOUND);
	}
}
