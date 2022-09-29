package com.client.eurekaclient.models.request.cells;

import com.client.eurekaclient.models.request.NotNullRequest;

public class SellCellsRequest extends NotNullRequest {
    public String trapName;
    public long NFTId;
    public double cellsQuantity;
    public double price;
    public String code;
    public String chainName;

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getTrapName() {
        return trapName;
    }

    public void setTrapName(String trapName) {
        this.trapName = trapName;
    }

    public long getNFTId() {
        return NFTId;
    }

    public void setNFTId(long NFTId) {
        this.NFTId = NFTId;
    }

    public double getCellsQuantity() {
        return cellsQuantity;
    }

    public void setCellsQuantity(double cellsQuantity) {
        this.cellsQuantity = cellsQuantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
