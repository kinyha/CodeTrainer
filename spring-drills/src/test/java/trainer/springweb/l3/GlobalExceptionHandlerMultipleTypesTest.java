package trainer.springweb.l3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class GlobalExceptionHandlerMultipleTypesTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandlerMultipleTypes())
                .build();
    }

    @Test
    void mapsNotFoundExceptionTo404() throws Exception {
        mvc.perform(get("/trigger/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("missing"));
    }

    @Test
    void mapsConflictExceptionTo409() throws Exception {
        mvc.perform(get("/trigger/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("duplicate"));
    }

    @RestController
    static final class TestController {

        @GetMapping("/trigger/not-found")
        void notFound() {
            throw new GlobalExceptionHandlerMultipleTypes.NotFoundException("missing");
        }

        @GetMapping("/trigger/conflict")
        void conflict() {
            throw new GlobalExceptionHandlerMultipleTypes.ConflictException("duplicate");
        }
    }
}
