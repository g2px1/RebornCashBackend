package com.client.eurekaclient.models.web3;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blockchains")
public class BlockchainData {
    public String url;
    public String name;
    public String nftContractAddress;
    public String hotWalletAddress;
    public String privateKey;
    public String platform = "evm";
    public String passwordToWalletFile;
    public String walletFileName;

    public BlockchainData(String url, String name, String nftContractAddress, String privateKey, String platform, String hotWalletAddress) {
        this.url = url;
        this.name = name;
        this.nftContractAddress = nftContractAddress;
        this.privateKey = privateKey;
        this.platform = platform;
        this.hotWalletAddress = hotWalletAddress;
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

    public String getHotWalletAddress() {
        return hotWalletAddress;
    }

    public void setHotWalletAddress(String hotWalletAddress) {
        this.hotWalletAddress = hotWalletAddress;
    }

    public String getPasswordToWalletFile() {
        return passwordToWalletFile;
    }

    public void setPasswordToWalletFile(String passwordToWalletFile) {
        this.passwordToWalletFile = passwordToWalletFile;
    }

    public String getWalletFileName() {
        return walletFileName;
    }

    public void setWalletFileName(String walletFileName) {
        this.walletFileName = walletFileName;
    }
}
