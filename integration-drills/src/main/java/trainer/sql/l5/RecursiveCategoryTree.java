package trainer.sql.l5;

// @task sql.l5.RecursiveCategoryTree
// @tags sql,recursive-cte,hierarchy,path,postgres
// @time 60m
// @src  new
// @doc  RecursiveCategoryTree.md
public final class RecursiveCategoryTree {

    private RecursiveCategoryTree() {
    }

    /** Возвращает поддерево от category id в первом параметре вместе с depth и path. */
    public static String query() {
        // ---8<--- solution
        return """
                WITH RECURSIVE tree AS (
                    SELECT id, parent_id, name, 0 AS depth, name::text AS path
                    FROM categories
                    WHERE id = ?
                    UNION ALL
                    SELECT child.id, child.parent_id, child.name,
                           parent.depth + 1,
                           parent.path || ' > ' || child.name
                    FROM categories child
                    JOIN tree parent ON child.parent_id = parent.id
                )
                SELECT id, name, depth, path
                FROM tree
                ORDER BY path
                """;
        // --->8--- solution
    }
}
