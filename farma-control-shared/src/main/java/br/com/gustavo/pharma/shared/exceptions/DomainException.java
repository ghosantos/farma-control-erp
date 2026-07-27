package br.com.gustavo.pharma.shared.exceptions;

import jakarta.ejb.ApplicationException;

import java.io.Serial;
import java.io.Serializable;

// Avisa o Wildfly que a DomainException não é um erro de servidor, mas sim uma regra de negócio esperada.
// rollback = true: Se esta exceção for lançada, o WildFly cancela a transação do banco.
@ApplicationException(rollback = true)
public class DomainException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public DomainException(String message) {
        super(message);
    }
}
