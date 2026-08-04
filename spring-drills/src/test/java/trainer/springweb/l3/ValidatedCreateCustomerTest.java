package trainer.springweb.l3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ValidatedCreateCustomerTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        var validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(new ValidatedCreateCustomer())
                .setValidator(validator)
                .build();
    }

    @Test
    void normalizesValidRequest() throws Exception {
        mvc.perform(post("/customers").contentType(APPLICATION_JSON)
                        .content("""
                                {"name":"  Ada  ","email":"ADA@MAIL.TEST"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Ada"))
                .andExpect(jsonPath("$.email").value("ada@mail.test"));
    }

    @Test
    void rejectsBlankNameAndMalformedEmail() throws Exception {
        mvc.perform(post("/customers").contentType(APPLICATION_JSON)
                        .content("""
                                {"name":" ","email":"not-an-email"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
