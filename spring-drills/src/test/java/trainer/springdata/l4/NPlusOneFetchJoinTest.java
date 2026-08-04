package trainer.springdata.l4;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@Testcontainers
@Transactional
@DirtiesContext
@SpringBootTest(classes = NPlusOneFetchJoinTest.Application.class, properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.generate_statistics=true",
        "spring.jpa.open-in-view=false"
})
class NPlusOneFetchJoinTest {

    @Container
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17.6-alpine")
            .withDatabaseName("trainer").withUsername("trainer").withPassword("trainer");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired EntityManager entityManager;
    @Autowired EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void seed() {
        CustomerEntity ada = new CustomerEntity(1, "Ada");
        ada.addOrder(new PurchaseOrderEntity(101, ada));
        ada.addOrder(new PurchaseOrderEntity(102, ada));
        CustomerEntity bob = new CustomerEntity(2, "Bob");
        bob.addOrder(new PurchaseOrderEntity(103, bob));
        entityManager.persist(ada);
        entityManager.persist(bob);
        entityManager.flush();
        entityManager.clear();
        entityManagerFactory.unwrap(SessionFactory.class).getStatistics().clear();
    }

    @Test
    void loadsCollectionsWithOneSelect() {
        List<CustomerEntity> customers = entityManager
                .createQuery(NPlusOneFetchJoin.jpql(), CustomerEntity.class)
                .getResultList();

        assertThat(customers).hasSize(2);
        assertThat(customers.stream().mapToInt(customer -> customer.orders().size()).sum()).isEqualTo(3);
        assertThat(entityManagerFactory.unwrap(SessionFactory.class).getStatistics().getPrepareStatementCount())
                .isEqualTo(1);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackageClasses = CustomerEntity.class)
    static class Application {
    }
}

@Entity(name = "CustomerEntity")
@Table(name = "n1_customers")
class CustomerEntity {
    @Id private Long id;
    private String name;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<PurchaseOrderEntity> orders = new ArrayList<>();

    protected CustomerEntity() {
    }

    CustomerEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    void addOrder(PurchaseOrderEntity order) {
        orders.add(order);
    }

    List<PurchaseOrderEntity> orders() {
        return orders;
    }
}

@Entity(name = "PurchaseOrderEntity")
@Table(name = "n1_orders")
class PurchaseOrderEntity {
    @Id private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CustomerEntity customer;

    protected PurchaseOrderEntity() {
    }

    PurchaseOrderEntity(long id, CustomerEntity customer) {
        this.id = id;
        this.customer = customer;
    }
}
