package com.client.eurekaclient.models.DTO.transactions;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class TransactionResult {
    public TransactionReceipt transactionReceipt;
    public boolean error;
    public String errorMessage;

    public TransactionResult() {
    }

    public TransactionResult(TransactionReceipt transactionReceipt, boolean error, String errorMessage) {
        this.transactionReceipt = transactionReceipt;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public TransactionReceipt getTransactionReceipt() {
        return transactionReceipt;
    }

    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
