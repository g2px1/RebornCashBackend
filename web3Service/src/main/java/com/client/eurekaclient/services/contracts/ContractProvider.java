package com.client.eurekaclient.services.contracts;

import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.contracts.eip721.generated.ERC721;

public interface ContractProvider {
    ERC20 getERC20Token(String contractAddress);
    ERC721 getERC721Token(String contractAddress);
}
