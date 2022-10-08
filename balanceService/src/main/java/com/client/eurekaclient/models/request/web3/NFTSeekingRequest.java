package com.client.eurekaclient.models.request.web3;

public class NFTSeekingRequest {
    public long index;
    public String address;
    public String uuid;
    public String chainName;

    public NFTSeekingRequest(long index, String chainName) {
        this.index = index;
        this.chainName = chainName;
    }

    public NFTSeekingRequest(long index) {
        this.index = index;
    }

    public NFTSeekingRequest(String uuid) {
        this.uuid = uuid;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
