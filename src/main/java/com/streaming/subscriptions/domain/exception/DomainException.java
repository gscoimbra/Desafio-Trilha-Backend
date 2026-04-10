package com.streaming.subscriptions.domain.exception;

/**
 * Base para falhas de regra de negócio que são mapeadas para respostas HTTP no adapter REST.
 * Os códigos seguem a semântica HTTP (4xx cliente, 5xx servidor) sem acoplar o domínio ao Spring.
 */
public abstract class DomainException extends RuntimeException {

    private final int httpStatusCode;

    /**
     * Exceção "simples"
     * Exemplo: UserNotFoundException não precisa "encadear" outra exceção, ela só informa o erro.
     */
    protected DomainException(int httpStatusCode, String message) {
        super(message);
        if (httpStatusCode < 100 || httpStatusCode > 599) {
            throw new IllegalArgumentException("httpStatusCode must be between 100 and 599");
        }
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Exceção "encadeada"
     * Quando a falha de regra aconteceu por causa de outra exceção e quero preservar a causa para debug.
     */
    protected DomainException(int httpStatusCode, String message, Throwable cause) {
        super(message, cause);
        if (httpStatusCode < 100 || httpStatusCode > 599) {
            throw new IllegalArgumentException("httpStatusCode must be between 100 and 599");
        }
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public boolean isClientError() {
        return httpStatusCode >= 400 && httpStatusCode < 500;
    }

    public boolean isServerError() {
        return httpStatusCode >= 500;
    }
}
