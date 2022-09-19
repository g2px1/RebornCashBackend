package com.client.eurekaclient.services.web3.gas;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;

public class GasProvider implements ContractGasProvider {
    protected Web3j web3j;

    public GasProvider(String url) {
        this.web3j = Web3j.build(new HttpService(url));
    }


    @Override
    public BigInteger getGasPrice(String s) {
        try {
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BigInteger getGasPrice() {
        try {
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return DefaultGasProvider.GAS_LIMIT.divide(BigInteger.valueOf(10));
    }

    @Override
    public BigInteger getGasLimit() {
        return DefaultGasProvider.GAS_LIMIT.divide(BigInteger.valueOf(10));
    }
}
