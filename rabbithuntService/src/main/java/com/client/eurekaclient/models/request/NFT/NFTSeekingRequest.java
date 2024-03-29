package com.client.eurekaclient.models.request.NFT;

public class NFTSeekingRequest {
    public String nftName;
    public String uuid;
    public long index;

    public NFTSeekingRequest() {
    }

    public NFTSeekingRequest(String nftName) {
        this.nftName = nftName;
    }

    public NFTSeekingRequest(long index) {
        this.index = index;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
