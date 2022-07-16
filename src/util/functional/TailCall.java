package util.functional;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@FunctionalInterface
public interface TailCall<T> extends Supplier<TailCall<T>> {

    default boolean isResult() {
        return false;
    }

    default T getResult() {
        throw new IllegalStateException("no result present");
    }

    default T invoke() {
        return Stream.iterate(this, TailCall::get)
                .filter(TailCall::isResult)
                .map(TailCall::getResult)
                .findFirst().orElseThrow();
    }

    static <T> TailCall<T> result(T t) {

        return new TailCall<>() {

            @Override
            public TailCall<T> get() {
                return this;
            }

            @Override
            public boolean isResult() {
                return true;
            }

            @Override
            public T getResult() {
                return t;
            }
        };
    }
}


