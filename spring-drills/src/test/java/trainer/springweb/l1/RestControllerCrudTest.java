package trainer.springweb.l1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RestControllerCrudTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(new RestControllerCrud(Map.of(1L, "Widget"))).build();
    }

    @Test
    void returnsProductWhenItExists() throws Exception {
        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Widget"));
    }

    @Test
    void returnsNotFoundForMissingProduct() throws Exception {
        mvc.perform(get("/products/99"))
                .andExpect(status().isNotFound());
    }
}
