package com.client.eurekaclient.services.web3.contracts;

import com.client.eurekaclient.services.web3.gas.GasProvider;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.contracts.eip721.generated.ERC721;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class StandardContractProvider extends GasProvider implements ContractProvider {
    private Credentials credentials;
    public StandardContractProvider(String url, String privateKey) {
        super(url);
        this.credentials = Credentials.create(privateKey);
    }

    public Web3j getWeb3j() {
        return super.web3j;
    }

    @Override
    public ERC20 getERC20Token(String contractAddress) {
        return ERC20.load(contractAddress, super.web3j, this.credentials, this);
    }

    @Override
    public ERC721 getERC721Token(String contractAddress) {
        return ERC721.load(contractAddress, super.web3j, this.credentials, this);
    }
}
