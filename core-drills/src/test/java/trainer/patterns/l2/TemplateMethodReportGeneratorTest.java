package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateMethodReportGeneratorTest {

    @Test
    void appliesHeaderRowFormattingAndDefaultEmptyFooter() {
        TemplateMethodReportGenerator generator = new TemplateMethodReportGenerator() {
            @Override
            protected String header() {
                return "REPORT\n";
            }

            @Override
            protected String formatRow(String row) {
                return "- " + row;
            }
        };

        assertThat(generator.generate(List.of("a", "b"))).isEqualTo("REPORT\n- a\n- b\n");
    }

    @Test
    void subclassCanOverrideFooter() {
        TemplateMethodReportGenerator generator = new TemplateMethodReportGenerator() {
            @Override
            protected String header() {
                return "";
            }

            @Override
            protected String formatRow(String row) {
                return row;
            }

            @Override
            protected String footer() {
                return "END";
            }
        };

        assertThat(generator.generate(List.of("x"))).isEqualTo("x\nEND");
    }
}
