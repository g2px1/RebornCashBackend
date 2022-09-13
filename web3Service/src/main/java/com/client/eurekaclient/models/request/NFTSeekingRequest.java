package com.client.eurekaclient.models.request;

public class NFTSeekingRequest {
    public long nftId;
    public String address;

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
