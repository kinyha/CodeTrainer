package trainer.springweb.l3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class ExceptionHandlerProblemDetailTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new TestOrderController())
                .setControllerAdvice(new ExceptionHandlerProblemDetail())
                .build();
    }

    @Test
    void rendersRfc9457ResponseForMissingOrder() throws Exception {
        mockMvc.perform(get("/orders/{id}", 42))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("https://trainer.dev/problems/order-not-found"))
                .andExpect(jsonPath("$.title").value("Order not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Order 42 was not found"))
                .andExpect(jsonPath("$.instance").value("/orders/42"))
                .andExpect(jsonPath("$.orderId").value(42));
    }

    @RestController
    static final class TestOrderController {

        @GetMapping("/orders/{id}")
        String find(@PathVariable("id") long id) {
            throw new ExceptionHandlerProblemDetail.OrderNotFoundException(id);
        }
    }
}
