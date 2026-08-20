package trainer.springweb.l1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RequestParamDefaultValueTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(new RequestParamDefaultValue(List.of("Ada", "Bob", "Cleo"))).build();
    }

    @Test
    void usesDefaultLimitWhenParamIsMissing() throws Exception {
        mvc.perform(get("/names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void appliesExplicitLimit() throws Exception {
        mvc.perform(get("/names").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("Ada"))
                .andExpect(jsonPath("$[1]").value("Bob"));
    }
}
