package com.client.eurekaclient.services;

import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Web3Service {
    @Autowired
    private BlockchainsRepository blockchainsRepository;
    private final Map<String, BlockchainData> blockchainDataList = blockchainsRepository.findAll().stream().collect(Collectors.toMap(BlockchainData::getName, Function.identity()));


}
