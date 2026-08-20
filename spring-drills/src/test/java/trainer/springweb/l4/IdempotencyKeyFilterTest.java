package trainer.springweb.l4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class IdempotencyKeyFilterTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(new TestController())
                .addFilter(new IdempotencyKeyFilter())
                .build();
    }

    @Test
    void allowsFirstRequestWithAGivenKey() throws Exception {
        mvc.perform(post("/orders").header(IdempotencyKeyFilter.HEADER, "key-1"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsRepeatedRequestWithTheSameKey() throws Exception {
        mvc.perform(post("/orders").header(IdempotencyKeyFilter.HEADER, "key-2")).andExpect(status().isOk());
        mvc.perform(post("/orders").header(IdempotencyKeyFilter.HEADER, "key-2")).andExpect(status().isConflict());
    }

    @Test
    void requestsWithoutAKeyAreAlwaysAllowed() throws Exception {
        mvc.perform(post("/orders")).andExpect(status().isOk());
        mvc.perform(post("/orders")).andExpect(status().isOk());
    }

    @RestController
    static final class TestController {

        @PostMapping("/orders")
        String create() {
            return "ok";
        }
    }
}
