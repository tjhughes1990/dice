package dice.ui;

import java.util.function.Consumer;

/**
 * Equivalent to a {@link Consumer} that can throw throwables.
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {

    /**
     * Consume content.
     *
     * @param content the content to consume.
     *
     * @throws E
     */
    void accept(T content) throws E;
}
