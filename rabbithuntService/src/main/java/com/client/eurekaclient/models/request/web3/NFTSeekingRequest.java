package com.client.eurekaclient.models.request.web3;

public class NFTSeekingRequest {
    public long nftId;
    public String address;

    public NFTSeekingRequest() {
    }

    public NFTSeekingRequest(long nftId) {
        this.nftId = nftId;
    }

    public NFTSeekingRequest(long nftId, String address) {
        this.nftId = nftId;
        this.address = address;
    }

    public long getNftId() {
        return nftId;
    }

    public void setNftId(long nftId) {
        this.nftId = nftId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
