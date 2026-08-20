package trainer.patterns.l2;

import java.util.Objects;

// @task patterns.l2.BuilderWithValidation
// @tags patterns,builder,validation,fluent-api
// @time 15m
// @src  new
public final class BuilderWithValidation {

    private BuilderWithValidation() {
    }

    public record Address(String street, String city, String zip) {
    }

    public static final class Builder {
        private String street;
        private String city;
        private String zip;

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder zip(String zip) {
            this.zip = zip;
            return this;
        }

        /** Валидация — в build(), а не в каждом сеттере: промежуточное состояние может быть неполным. */
        public Address build() {
            // ---8<--- solution
            Objects.requireNonNull(street, "street");
            Objects.requireNonNull(city, "city");
            Objects.requireNonNull(zip, "zip");
            if (zip.isBlank()) {
                throw new IllegalArgumentException("zip must not be blank");
            }
            return new Address(street, city, zip);
            // --->8--- solution
        }
    }
}
