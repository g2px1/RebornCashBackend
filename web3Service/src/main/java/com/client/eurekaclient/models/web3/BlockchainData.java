package com.client.eurekaclient.models.web3;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blockchains")
public class BlockchainData {
    public String url;
    public String name;
    public String nftContractAddress;
    public String gameContractAddress;
    public String stableCoinAddress;
    public String privateKey;
    public String platform = "evm";

    public BlockchainData(String url, String name, String nftContractAddress, String gameContractAddress, String stableCoinAddress, String privateKey, String platform) {
        this.url = url;
        this.name = name;
        this.nftContractAddress = nftContractAddress;
        this.gameContractAddress = gameContractAddress;
        this.stableCoinAddress = stableCoinAddress;
        this.privateKey = privateKey;
        this.platform = platform;
    }

    public String getStableCoinAddress() {
        return stableCoinAddress;
    }

    public void setStableCoinAddress(String stableCoinAddress) {
        this.stableCoinAddress = stableCoinAddress;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNftContractAddress() {
        return nftContractAddress;
    }

    public void setNftContractAddress(String nftContractAddress) {
        this.nftContractAddress = nftContractAddress;
    }

    public String getGameContractAddress() {
        return gameContractAddress;
    }

    public void setGameContractAddress(String gameContractAddress) {
        this.gameContractAddress = gameContractAddress;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}