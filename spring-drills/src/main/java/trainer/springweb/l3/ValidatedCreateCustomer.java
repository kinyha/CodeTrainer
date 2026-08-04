package trainer.springweb.l3;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// @task springweb.l3.ValidatedCreateCustomer
// @tags spring-mvc,bean-validation,request-body,MockMvc
// @time 25m
// @src  new
@RestController
@RequestMapping("/customers")
public final class ValidatedCreateCustomer {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerView create(@Valid @RequestBody CreateCustomer request) {
        // ---8<--- solution
        return new CustomerView(request.name().trim(), request.email().trim().toLowerCase());
        // --->8--- solution
    }

    public record CreateCustomer(
            @NotBlank String name,
            @NotBlank @Email String email
    ) {
    }

    public record CustomerView(String name, String email) {
    }
}
