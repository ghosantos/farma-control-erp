package br.com.gustavo.pharma.shared.exceptions;

import java.io.Serial;

public class LoteVencidoException extends DomainException {

    @Serial
    private static final long serialVersionUID = 1L;

    public LoteVencidoException(String message) {
        super(message);
    }
}
