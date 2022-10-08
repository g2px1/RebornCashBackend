package com.client.eurekaclient.services.web3.contracts;

import com.client.eurekaclient.services.web3.gas.GasProvider;
import com.client.eurekaclient.web3.contracts.nft.OreChainNFT;
import com.client.eurekaclient.web3.contracts.tokens.Game_sol_Game;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class OreChainContractProvider extends GasProvider implements ContractProvider {
    private Credentials credentials;
    public OreChainContractProvider(String url, String privateKey) {
        super(url);
        this.credentials = Credentials.create(privateKey);
    }

    public Web3j getWeb3j() {
        return super.web3j;
    }

    @Override
    public Game_sol_Game getERC20Token(String contractAddress) {
        return Game_sol_Game.load(contractAddress, super.web3j, this.credentials, this);
    }

    @Override
    public OreChainNFT getERC721Token(String contractAddress) {
        return OreChainNFT.load(contractAddress, super.web3j, this.credentials, this);
    }
}
