package com.client.eurekaclient.services.web3.contracts;

import org.web3j.tx.Contract;

public interface ContractProvider {
    Contract getERC20Token(String contractAddress);
    Contract getERC721Token(String contractAddress);
}
