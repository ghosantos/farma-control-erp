package br.com.gustavo.pharma.shared.exceptions;

import java.io.Serial;

public class EstoqueInsuficienteException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
