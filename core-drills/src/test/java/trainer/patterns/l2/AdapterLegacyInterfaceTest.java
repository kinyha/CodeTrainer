package trainer.patterns.l2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class AdapterLegacyInterfaceTest {

    @Test
    void adaptsListExportToLegacyArrayExport() {
        AdapterLegacyInterface.LegacyCsvExporter legacy = values -> String.join(",", values);
        AdapterLegacyInterface.ListExporter exporter = AdapterLegacyInterface.adapt(legacy);

        assertThat(exporter.export(List.of("a", "b", "c"))).isEqualTo("a,b,c");
    }

    @Test
    void emptyListDelegatesToLegacyWithEmptyArray() {
        AdapterLegacyInterface.LegacyCsvExporter legacy = values -> values.length == 0 ? "" : String.join(",", values);
        assertThat(AdapterLegacyInterface.adapt(legacy).export(List.of())).isEmpty();
    }

    @Test
    void rejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> AdapterLegacyInterface.adapt(null));
    }
}
