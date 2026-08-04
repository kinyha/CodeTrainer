package trainer.sql.l5;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveCategoryTreeContractTest {

    @Test
    void definesRecursiveAnchorAndParentJoin() {
        assertThat(RecursiveCategoryTree.query().toUpperCase())
                .contains("WITH RECURSIVE TREE", "WHERE ID = ?", "UNION ALL", "JOIN TREE PARENT");
    }
}
