package trainer.springweb.l3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @task springweb.l3.GlobalExceptionHandlerMultipleTypes
// @tags spring-mvc,exception-handler,multiple-types,problem-detail
// @time 25m
// @src  new
@RestControllerAdvice
public final class GlobalExceptionHandlerMultipleTypes {

    /** Разные доменные исключения — разные статусы; каждый @ExceptionHandler отвечает за один тип. */
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException error) {
        // ---8<--- solution
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, error.getMessage());
        // --->8--- solution
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException error) {
        // ---8<--- solution
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, error.getMessage());
        // --->8--- solution
    }

    public static final class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static final class ConflictException extends RuntimeException {
        public ConflictException(String message) {
            super(message);
        }
    }
}
