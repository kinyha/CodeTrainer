package trainer.springweb.l3;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

// @task springweb.l3.ExceptionHandlerProblemDetail
// @tags spring-mvc,exception-handler,problem-detail,rfc9457
// @time 30m
// @src  LeetCode-75-Study-Project:docs/spring-boot-jpa-guide.md
// @doc  ExceptionHandlerProblemDetail.md
@RestControllerAdvice
public final class ExceptionHandlerProblemDetail {

    private static final URI TYPE = URI.create("https://trainer.dev/problems/order-not-found");

    /**
     * Преобразует доменную ошибку в стандартный application/problem+json.
     * Контракт: статус 404, стабильный type URI, instance равен пути запроса,
     * orderId доступен клиенту как дополнительное поле.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handle(OrderNotFoundException error, HttpServletRequest request) {
        // ---8<--- solution
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, error.getMessage());
        problem.setTitle("Order not found");
        problem.setType(TYPE); // WHY: стабильный URI идентифицирует тип ошибки независимо от текста
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("orderId", error.orderId());
        return problem;
        // --->8--- solution
    }

    public static final class OrderNotFoundException extends RuntimeException {
        private final long orderId;

        public OrderNotFoundException(long orderId) {
            super("Order %d was not found".formatted(orderId));
            this.orderId = orderId;
        }

        public long orderId() {
            return orderId;
        }
    }
}
