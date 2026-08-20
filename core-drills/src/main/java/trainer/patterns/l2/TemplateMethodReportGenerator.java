package trainer.patterns.l2;

import java.util.List;
import java.util.Objects;

// @task patterns.l2.TemplateMethodReportGenerator
// @tags patterns,template-method,abstract-class
// @time 15m
// @src  new
public abstract class TemplateMethodReportGenerator {

    /** Скелет фиксирован здесь; подклассы переопределяют только шаги, которые у них отличаются. */
    public final String generate(List<String> rows) {
        Objects.requireNonNull(rows, "rows");

        // ---8<--- solution
        StringBuilder report = new StringBuilder();
        report.append(header());
        for (String row : rows) {
            report.append(formatRow(row)).append('\n');
        }
        report.append(footer());
        return report.toString();
        // --->8--- solution
    }

    protected abstract String header();

    protected abstract String formatRow(String row);

    protected String footer() {
        return "";
    }
}
