package trainer.springweb.l4;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// @task springweb.l4.IdempotencyKeyFilter
// @tags spring-mvc,filter,idempotency-key,duplicate-request
// @time 35m
// @src  new
public final class IdempotencyKeyFilter implements Filter {

    public static final String HEADER = "Idempotency-Key";

    private final Set<String> seenKeys = ConcurrentHashMap.newKeySet();

    /**
     * Повторный запрос с уже виденным ключом отклоняется ДО контроллера — обработчику
     * не приходится самому помнить про идемпотентность на каждом эндпоинте.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(response, "response");
        Objects.requireNonNull(chain, "chain");

        // ---8<--- solution
        String key = ((HttpServletRequest) request).getHeader(HEADER);
        if (key != null && !seenKeys.add(key)) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_CONFLICT, "duplicate request");
            return;
        }
        chain.doFilter(request, response);
        // --->8--- solution
    }
}
