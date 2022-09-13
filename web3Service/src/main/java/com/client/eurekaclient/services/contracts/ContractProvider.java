package com.client.eurekaclient.services.contracts;

import com.client.eurekaclient.services.gas.GasProvider;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public interface ContractProvider {
//    Credentials getCredentials(String privateKey);
    ERC20 getERC20Token(String contractAddress);
}
