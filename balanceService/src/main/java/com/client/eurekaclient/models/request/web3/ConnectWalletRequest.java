package com.client.eurekaclient.models.request.web3;

public class ConnectWalletRequest {
    public String message;
    public String requestedAddress;
    public String chainName;

    public ConnectWalletRequest(String message, String requestedAddress, String chainName) {
        this.message = message;
        this.requestedAddress = requestedAddress;
        this.chainName = chainName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestedAddress() {
        return requestedAddress;
    }

    public void setRequestedAddress(String requestedAddress) {
        this.requestedAddress = requestedAddress;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }
}
