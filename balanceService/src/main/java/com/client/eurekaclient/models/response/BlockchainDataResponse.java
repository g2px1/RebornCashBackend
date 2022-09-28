package com.client.eurekaclient.models.response;

import com.client.eurekaclient.models.web3.BlockchainData;

public class BlockchainDataResponse {
    public String url;
    public String name;
    public String nftContractAddress;
    public String gameContractAddress;
    public String gameIMMO;
    public String stableCoinAddress;
    public String hotWalletAddress;
    public String platform = "evm";
    public String jsonPathToGamePrice;
    public String gameRequestType;

    public BlockchainDataResponse() {}
    public BlockchainDataResponse(String url, String name, String nftContractAddress, String gameContractAddress, String gameIMMO, String stableCoinAddress, String hotWalletAddress, String platform, String jsonPathToGamePrice, String gameRequestType) {
        this.url = url;
        this.name = name;
        this.nftContractAddress = nftContractAddress;
        this.gameContractAddress = gameContractAddress;
        this.gameIMMO = gameIMMO;
        this.stableCoinAddress = stableCoinAddress;
        this.hotWalletAddress = hotWalletAddress;
        this.platform = platform;
        this.jsonPathToGamePrice = jsonPathToGamePrice;
        this.gameRequestType = gameRequestType;
    }

    public static BlockchainDataResponse build(BlockchainData blockchainData) {
        return new BlockchainDataResponse(blockchainData.url, blockchainData.name, blockchainData.nftContractAddress, blockchainData.gameContractAddress, blockchainData.gameIMMO, blockchainData.stableCoinAddress, blockchainData.hotWalletAddress, blockchainData.platform, blockchainData.jsonPathToGamePrice, blockchainData.gameRequestType);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGameIMMO() {
        return gameIMMO;
    }

    public void setGameIMMO(String gameIMMO) {
        this.gameIMMO = gameIMMO;
    }

    public String getStableCoinAddress() {
        return stableCoinAddress;
    }

    public void setStableCoinAddress(String stableCoinAddress) {
        this.stableCoinAddress = stableCoinAddress;
    }

    public String getHotWalletAddress() {
        return hotWalletAddress;
    }

    public void setHotWalletAddress(String hotWalletAddress) {
        this.hotWalletAddress = hotWalletAddress;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getJsonPathToGamePrice() {
        return jsonPathToGamePrice;
    }

    public void setJsonPathToGamePrice(String jsonPathToGamePrice) {
        this.jsonPathToGamePrice = jsonPathToGamePrice;
    }

    public String getGameRequestType() {
        return gameRequestType;
    }

    public void setGameRequestType(String gameRequestType) {
        this.gameRequestType = gameRequestType;
    }
}
