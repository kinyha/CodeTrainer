package trainer.patterns.l2;

import java.util.List;
import java.util.Objects;

// @task patterns.l2.AdapterLegacyInterface
// @tags patterns,adapter,legacy-interface
// @time 12m
// @src  new
public final class AdapterLegacyInterface {

    private AdapterLegacyInterface() {
    }

    /** Старый API, который нельзя менять — работает со строками через запятую. */
    public interface LegacyCsvExporter {
        String exportCsv(String[] values);
    }

    public interface ListExporter {
        String export(List<String> values);
    }

    /** Адаптер: приводит новый интерфейс к сигнатуре, которую понимает legacy-код. */
    public static ListExporter adapt(LegacyCsvExporter legacy) {
        Objects.requireNonNull(legacy, "legacy");

        // ---8<--- solution
        return values -> legacy.exportCsv(values.toArray(new String[0]));
        // --->8--- solution
    }
}
