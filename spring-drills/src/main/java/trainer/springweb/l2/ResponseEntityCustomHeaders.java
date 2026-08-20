package trainer.springweb.l2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

// @task springweb.l2.ResponseEntityCustomHeaders
// @tags spring-mvc,ResponseEntity,headers,caching
// @time 12m
// @src  new
public final class ResponseEntityCustomHeaders {

    private ResponseEntityCustomHeaders() {
    }

    /** Кэш-заголовок — часть контракта ответа, не побочный эффект где-то ещё. */
    public static ResponseEntity<String> withCacheHeader(String body, int maxAgeSeconds) {
        Objects.requireNonNull(body, "body");

        // ---8<--- solution
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "max-age=" + maxAgeSeconds)
                .body(body);
        // --->8--- solution
    }
}
