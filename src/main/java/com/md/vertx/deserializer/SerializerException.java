package com.md.vertx.deserializer;

/**
 * User: mdyminski
 */
public class SerializerException extends RuntimeException {

    private static final long serialVersionUID = -235408370738353113L;

    public SerializerException(String msg) {
        super(msg);
    }

    public SerializerException(String msg, Throwable t) {
        super(msg, t);
    }

    public SerializerException(Throwable t) {
        super(t);
    }

}
