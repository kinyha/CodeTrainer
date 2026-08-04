package trainer.fixtures.sales;

import java.time.LocalDate;
import java.util.Objects;

public record Customer(
        long id,
        String name,
        String email,
        LocalDate registrationDate,
        String city,
        CustomerType type,
        boolean active
) {
    public Customer {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(registrationDate, "registrationDate");
        Objects.requireNonNull(city, "city");
        Objects.requireNonNull(type, "type");
    }
}
