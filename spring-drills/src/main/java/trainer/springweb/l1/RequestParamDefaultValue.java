package trainer.springweb.l1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

// @task springweb.l1.RequestParamDefaultValue
// @tags spring-mvc,request-param,default-value
// @time 8m
// @src  new
@RestController
public final class RequestParamDefaultValue {

    private final List<String> names;

    public RequestParamDefaultValue(List<String> names) {
        this.names = Objects.requireNonNull(names, "names");
    }

    /** limit не указан клиентом -> используется defaultValue, а не null/0. */
    @GetMapping("/names")
    public List<String> firstNames(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        // ---8<--- solution
        return names.stream().limit(limit).toList();
        // --->8--- solution
    }
}
