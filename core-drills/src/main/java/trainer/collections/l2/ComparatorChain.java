package trainer.collections.l2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

// @task collections.l2.ComparatorChain
// @tags Comparator,thenComparing,nullsLast,sorting
// @time 15m
// @src  new
public final class ComparatorChain {

    private ComparatorChain() {
    }

    /** Сортирует по фамилии (null последним), рейтингу по убыванию и id. */
    public static List<Applicant> sort(List<Applicant> applicants) {
        Objects.requireNonNull(applicants, "applicants");

        // ---8<--- solution
        Comparator<Applicant> order = Comparator
                .comparing(Applicant::lastName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))
                .thenComparing(Comparator.comparingInt(Applicant::score).reversed())
                .thenComparingLong(Applicant::id);
        List<Applicant> result = new ArrayList<>(applicants);
        result.sort(order);
        return List.copyOf(result);
        // --->8--- solution
    }

    public record Applicant(long id, String lastName, int score) {
    }
}
