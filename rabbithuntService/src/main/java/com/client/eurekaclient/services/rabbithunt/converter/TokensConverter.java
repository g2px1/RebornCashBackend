package com.client.eurekaclient.services.rabbithunt.converter;

import com.client.eurekaclient.models.response.ResponseHandler;
import com.client.eurekaclient.services.openfeign.NFT.NFTInterface;
import com.client.eurekaclient.services.openfeign.users.UserInterface;
import com.client.eurekaclient.services.openfeign.wallets.ConnectedWalletInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokensConverter {
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private NFTInterface nftInterface;
    @Autowired
    private ConnectedWalletInterface connectedWalletInterface;

    public ResponseEntity<Object> convertLayer1TokensIntoGame() {
        return ResponseHandler.generateResponse(null, HttpStatus.OK, null);
    }
}
