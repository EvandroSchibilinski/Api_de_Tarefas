package com.schibilinski.projeto.exception;

public class TransicaoStatusInvalidaException
        extends RegraDeNegocioException {

    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }
}