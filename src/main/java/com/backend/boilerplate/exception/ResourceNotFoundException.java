package com.backend.boilerplate.exception;

import lombok.Getter;

/**
 * @author sarvesh
 * @version 0.0.3
 * @since 0.0.3
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * see {@link ErrorDetails}
     */
    @Getter
    private final transient ErrorDetails error;


    /**
     * @param error ErrorDetails
     * @see RuntimeException#RuntimeException(String)
     */
    public ResourceNotFoundException(ErrorDetails error) {
        super(error.toString());
        this.error = error;
    }

    /**
     * @param message message
     * @see RuntimeException#RuntimeException(String)
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.error = null;
    }

}
