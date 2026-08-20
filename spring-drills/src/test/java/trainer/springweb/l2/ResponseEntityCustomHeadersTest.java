package trainer.springweb.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ResponseEntityCustomHeadersTest {

    @Test
    void setsCacheControlHeaderWithGivenMaxAge() {
        var response = ResponseEntityCustomHeaders.withCacheHeader("hello", 3600);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getCacheControl()).isEqualTo("max-age=3600");
        assertThat(response.getBody()).isEqualTo("hello");
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> ResponseEntityCustomHeaders.withCacheHeader(null, 60));
    }
}
