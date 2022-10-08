package com.client.eurekaclient.services.web3;

import com.client.eurekaclient.messages.ErrorMessage;
import com.client.eurekaclient.models.DTO.users.User;
import com.client.eurekaclient.models.nft.NFT;
import com.client.eurekaclient.models.request.web3.NFTSeekingRequest;
import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.models.web3.BlockchainData;
import com.client.eurekaclient.models.web3.ConnectedWallet;
import com.client.eurekaclient.repositories.BlockchainsRepository;
import com.client.eurekaclient.repositories.ConnectedWalletsRepository;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.web3.contracts.OreChainContractProvider;
import com.client.eurekaclient.services.web3.gas.GasProvider;
import com.client.eurekaclient.utilities.web3.EthersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.Executor;

@Service
public class WalletService {
    @Autowired
    private ConnectedWalletsRepository connectedWalletsRepository;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private BlockchainsRepository blockchainsRepository;

    public ResponseEntity<Object> connectWallet(String message, String requestedAddress, String chainName, String username) {
        User user = userInterface.getUser(username).get();
        try {
            String address = String.format("0x%s", EthersUtils.getUserAddressFromMessage(message, user.getNonce()));
            if (address.equalsIgnoreCase(requestedAddress)) {
                if(connectedWalletsRepository.existsByUsername(username)) {
                    connectedWalletsRepository.deleteByUsername(username);
                }
                ConnectedWallet connectedWallet = new ConnectedWallet(username, address);
                connectedWalletsRepository.save(connectedWallet);

                Optional<BlockchainData> optionalBlockchainData = blockchainsRepository.findByName(chainName);
                if (optionalBlockchainData.isEmpty()) return ResponseHandler.generateResponse(ErrorMessage.CHAIN_NOT_SUPPORTED, HttpStatus.BAD_REQUEST, null);
                BlockchainData blockchainData = optionalBlockchainData.get();

                Runnable activateNft = () -> {
                    List<NFT> nftListToBeUpdated = new ArrayList<>();
                    GasProvider gasProvider = new GasProvider(blockchainData.url);
                    OreChainContractProvider oreChainContractProvider = new OreChainContractProvider(blockchainData.url, blockchainData.getPrivateKey());
                    try {
                        oreChainContractProvider.getERC721Token(blockchainData.nftContractAddress).getUsersTokens(address).send().forEach(item -> {
                            Optional<NFT> nft = nftInterface.findNftByUuid(new NFTSeekingRequest(Long.parseLong(item.toString()), chainName));
                            if (nft.isPresent()) {
                                Calendar cal = new GregorianCalendar();
                                cal.setTime(new Date());
                                cal.add(Calendar.DATE, 15);
                                nft.get().setActiveTill(cal.getTimeInMillis());
                                nft.get().setStatus("active");
                                nftListToBeUpdated.add(nft.get());
                            }
                        });
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.DEFAULT_ERROR);
                    }
                    nftInterface.saveAll(nftListToBeUpdated);
                };
                Executor reverseTokensExecutor = (runnable) -> {
                    new Thread(runnable).start();
                };
                reverseTokensExecutor.execute(activateNft);

                return ResponseHandler.generateResponse(null, HttpStatus.OK, true);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.METAMASK_ERROR);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.METAMASK_ERROR);
        }
    }
}
