package com.client.eurekaclient.models.goldenrush.mine;

import com.client.eurekaclient.models.request.goldenrush.mine.MineRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Locale;

@Document(collection = "oreHuntTraps")
public class Mine {
    @Id
    public String id;
    public String name;
    public String trapId;
    public String image;
    public int cells;
    public long created = new Date().getTime();
    public long activeAfter;
    public long activeTill = -1;
    public String optionType;
    public String tool;
    public long strikePrice;
    public int cellsAvailable;
    public String tokenName = "gold_coin";
    public double tokenTotalWeight;
    public double tokenPerCell;
    public String optionName;
    public int quantityOfLots = 1;
    public boolean status = true;

    public Mine() {}

    public Mine(String name, String trapId, String image, int cells, long created, long activeAfter, String optionType, String tool, long strikePrice, int cellsAvailable, String tokenName, double tokenTotalWeight, double tokenPerCell, String optionName, boolean status, long activeTill, int quantityOfLots) {
        this.name = name;
        this.trapId = trapId;
        this.image = image;
        this.cells = cells;
        this.created = created;
        this.activeAfter = activeAfter;
        this.optionType = optionType;
        this.tool = tool;
        this.strikePrice = strikePrice;
        this.cellsAvailable = cellsAvailable;
        this.tokenName = tokenName;
        this.tokenTotalWeight = tokenTotalWeight;
        this.tokenPerCell = tokenPerCell;
        this.optionName = optionName;
        this.status = status;
        this.activeTill = activeTill;
        this.quantityOfLots = quantityOfLots;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName.toUpperCase(Locale.ROOT);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCells() {
        return cells;
    }

    public void setCells(int cells) {
        this.cells = cells;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getActiveAfter() {
        return activeAfter;
    }

    public void setActiveAfter(long activeAfter) {
        this.activeAfter = activeAfter;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public long getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(long strikePrice) {
        this.strikePrice = strikePrice;
    }

    public int getCellsAvailable() {
        return cellsAvailable;
    }

    public void setCellsAvailable(int cellsAvailable) {
        this.cellsAvailable = cellsAvailable;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public double getTokenTotalWeight() {
        return tokenTotalWeight;
    }

    public void setTokenTotalWeight(double tokenTotalWeight) {
        this.tokenTotalWeight = tokenTotalWeight;
    }

    public double getTokenPerCell() {
        return tokenPerCell;
    }

    public void setTokenPerCell(double tokenPerCell) {
        this.tokenPerCell = tokenPerCell;
    }

    public String getTrapId() {
        return trapId;
    }

    public void setTrapId(String trapId) {
        this.trapId = trapId;
    }

    public void updateFromRequest(MineRequest mineRequest) {
        this.activeAfter = mineRequest.activeAfter;
        this.cells = mineRequest.cells;
        this.cellsAvailable = mineRequest.cellsAvailable;
        this.created = mineRequest.created;
//        this.image = mineRequest.imageFile.getOriginalFilename();
        this.name = mineRequest.name;
        this.optionType = mineRequest.optionType;
        this.strikePrice = Math.round(mineRequest.strikePrice);
        this.tokenTotalWeight = mineRequest.tokenTotalWeight;
        this.tool = mineRequest.tools;
    }
}
