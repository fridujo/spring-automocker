package com.github.fridujo.automocker.api.jms;

import com.github.fridujo.automocker.api.Resettable;
import org.springframework.util.ErrorHandler;

import java.util.Optional;

public class ErrorHandlerMock implements ErrorHandler, Resettable {

    private final Optional<ErrorHandler> delegate;
    private Throwable lastCatched = null;

    ErrorHandlerMock(Optional<ErrorHandler> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleError(Throwable t) {
        lastCatched = t;
        delegate.ifPresent(e -> e.handleError(t));
    }

    public Optional<Throwable> getLastCatched() {
        return Optional.ofNullable(lastCatched);
    }

    @Override
    public void reset() {
        this.lastCatched = null;
    }
}
