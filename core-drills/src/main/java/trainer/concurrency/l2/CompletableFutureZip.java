package trainer.concurrency.l2;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

// @task concurrency.l2.CompletableFutureZip
// @tags concurrency,CompletableFuture,thenCombine,async
// @time 18m
// @src  new
public final class CompletableFutureZip {

    private CompletableFutureZip() {
    }

    /** Комбинирует два независимых результата без блокирующих get/join. */
    public static <A, B, R> CompletionStage<R> zip(
            CompletionStage<A> left,
            CompletionStage<B> right,
            BiFunction<? super A, ? super B, ? extends R> combine
    ) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        Objects.requireNonNull(combine, "combine");

        // ---8<--- solution
        return left.toCompletableFuture().thenCombine(right, combine);
        // --->8--- solution
    }

    public static <T> CompletionStage<T> completed(T value) {
        return CompletableFuture.completedFuture(value);
    }
}
