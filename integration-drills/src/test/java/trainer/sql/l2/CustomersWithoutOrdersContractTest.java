package trainer.sql.l2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomersWithoutOrdersContractTest {

    @Test
    void usesLeftAntiJoin() {
        assertThat(CustomersWithoutOrders.query().toUpperCase())
                .contains("LEFT JOIN ORDERS", "O.ID IS NULL", "ORDER BY C.ID");
    }
}
