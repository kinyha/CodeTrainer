package trainer.concurrency.l2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompletableFutureZipTest {

    @Test
    void combinesValuesWhenBothComplete() {
        var result = CompletableFutureZip.zip(
                CompletableFutureZip.completed(40),
                CompletableFutureZip.completed(2),
                Integer::sum);

        assertThat(result.toCompletableFuture().join()).isEqualTo(42);
    }

    @Test
    void propagatesFailureWithoutCallingCombiner() {
        var failed = CompletableFuture.<Integer>failedFuture(new IllegalStateException("boom"));
        var result = CompletableFutureZip.zip(failed, CompletableFuture.completedFuture(2),
                (left, right) -> left + right);

        assertThatThrownBy(() -> result.toCompletableFuture().join())
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(IllegalStateException.class);
    }
}
