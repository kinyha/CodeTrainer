package trainer.springweb.l1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

// @task springweb.l1.RestControllerCrud
// @tags spring-mvc,rest-controller,path-variable,404
// @time 10m
// @src  new
@RestController
@RequestMapping("/products")
public final class RestControllerCrud {

    private final Map<Long, String> productsById;

    public RestControllerCrud(Map<Long, String> productsById) {
        this.productsById = Objects.requireNonNull(productsById, "productsById");
    }

    /** Отсутствующий id — 404 через ResponseStatusException, а не null в теле ответа. */
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") long id) {
        // ---8<--- solution
        String product = productsById.get(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product " + id + " not found");
        }
        return product;
        // --->8--- solution
    }
}
