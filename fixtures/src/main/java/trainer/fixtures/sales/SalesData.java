package trainer.fixtures.sales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class SalesData {

    private SalesData() {
    }

    public static List<Employee> employees() {
        return List.of(
                new Employee(1, "Ada Lovelace", "Engineering", "Senior Engineer", 150_000,
                        LocalDate.of(2020, 2, 10), "London"),
                new Employee(2, "Grace Hopper", "Engineering", "Tech Lead", 170_000,
                        LocalDate.of(2019, 6, 1), "New York"),
                new Employee(3, "Mary Jackson", "Engineering", "Software Engineer", 130_000,
                        LocalDate.of(2022, 3, 14), "Hampton"),
                new Employee(4, "David Ogilvy", "Marketing", "Marketing Specialist", 90_000,
                        LocalDate.of(2021, 9, 20), "London"),
                new Employee(5, "Philip Kotler", "Marketing", "Marketing Manager", 110_000,
                        LocalDate.of(2018, 1, 8), "Chicago"),
                new Employee(6, "Peter Drucker", "Operations", "Operations Manager", 120_000,
                        LocalDate.of(2017, 11, 6), "Vienna")
        );
    }

    public static List<Customer> customers() {
        return List.of(
                new Customer(1, "Lin Chen", "lin@example.com", LocalDate.of(2022, 1, 10),
                        "Singapore", CustomerType.VIP, true),
                new Customer(2, "Sam Rivera", "sam@example.com", LocalDate.of(2023, 5, 3),
                        "Madrid", CustomerType.PREMIUM, true),
                new Customer(3, "Alex Kim", "alex@example.com", LocalDate.of(2024, 8, 17),
                        "Seoul", CustomerType.REGULAR, false)
        );
    }

    public static List<Transaction> transactions() {
        return List.of(
                new Transaction(1, 1, money("249.90"), TransactionType.PAYMENT,
                        TransactionStatus.COMPLETED, at(2026, 7, 1, 10, 15), "Software"),
                new Transaction(2, 2, money("75.00"), TransactionType.REFUND,
                        TransactionStatus.COMPLETED, at(2026, 7, 2, 9, 30), "Books"),
                new Transaction(3, 1, money("500.00"), TransactionType.TRANSFER,
                        TransactionStatus.PENDING, at(2026, 7, 3, 14, 0), "Banking")
        );
    }

    public static List<Order> orders() {
        return List.of(
                new Order(1, 1, List.of(
                        new OrderItem(101, "Keyboard", 1, money("120.00")),
                        new OrderItem(102, "Mouse", 2, money("45.00"))
                ), at(2026, 6, 10, 12, 0), OrderStatus.DELIVERED),
                new Order(2, 2, List.of(
                        new OrderItem(103, "Monitor", 1, money("399.90"))
                ), at(2026, 6, 12, 16, 45), OrderStatus.SHIPPED),
                new Order(3, 1, List.of(
                        new OrderItem(104, "Webcam", 1, money("89.50"))
                ), at(2026, 7, 1, 8, 20), OrderStatus.CANCELLED)
        );
    }

    private static BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    private static LocalDateTime at(int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
