package com.client.eurekaclient.models.response;

import com.client.eurekaclient.models.web3.BlockchainData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.util.Optional;

public class BlockchainDataResponse {
    public String url;
    public String name;
    public String nftContractAddress;
    public String hotWalletAddress;
    public String platform = "evm";
    public String walletAddress;
    private static final Logger logger = LoggerFactory.getLogger(BlockchainDataResponse.class);

    public BlockchainDataResponse() {}
    public BlockchainDataResponse(String url, String name, String nftContractAddress, String hotWalletAddress, String platform, String walletAddress) {
        this.url = url;
        this.name = name;
        this.nftContractAddress = nftContractAddress;
        this.hotWalletAddress = hotWalletAddress;
        this.platform = platform;
        this.walletAddress = walletAddress;
    }

    public static Optional<BlockchainDataResponse> build(BlockchainData blockchainData, String pathToWalletFolder) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(blockchainData.passwordToWalletFile, pathToWalletFolder);
        } catch (java.io.IOException | org.web3j.crypto.CipherException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(new BlockchainDataResponse(blockchainData.url, blockchainData.name, blockchainData.nftContractAddress, blockchainData.hotWalletAddress, blockchainData.platform, credentials.getAddress()));
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
}
