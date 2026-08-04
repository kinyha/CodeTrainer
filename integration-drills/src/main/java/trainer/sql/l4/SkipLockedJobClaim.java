package trainer.sql.l4;

// @task sql.l4.SkipLockedJobClaim
// @tags sql,for-update,skip-locked,concurrency,postgres
// @time 45m
// @src  new
// @doc  SkipLockedJobClaim.md
public final class SkipLockedJobClaim {

    private SkipLockedJobClaim() {
    }

    /** Выбирает старейшую READY-задачу, не ожидая строки, захваченные другим worker. */
    public static String query() {
        // ---8<--- solution
        return """
                SELECT id
                FROM jobs
                WHERE status = 'READY'
                ORDER BY created_at, id
                FOR UPDATE SKIP LOCKED
                LIMIT 1
                """;
        // --->8--- solution
    }
}
