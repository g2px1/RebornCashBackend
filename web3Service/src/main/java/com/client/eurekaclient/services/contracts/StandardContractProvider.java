package com.client.eurekaclient.services.contracts;

import com.client.eurekaclient.services.gas.GasProvider;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.contracts.eip721.generated.ERC721;
import org.web3j.crypto.Credentials;

public class StandardContractProvider extends GasProvider implements ContractProvider {
    private Credentials credentials;
    public StandardContractProvider(String url, String privateKey) {
        super(url);
        this.credentials = Credentials.create(privateKey);
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
