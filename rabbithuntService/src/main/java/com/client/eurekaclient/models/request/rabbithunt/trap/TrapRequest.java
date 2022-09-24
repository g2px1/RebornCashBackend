package com.client.eurekaclient.models.request.rabbithunt.trap;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;

public class TrapRequest {
    @NotBlank
    public String name;
    public int cells;
    public long created;
    public long activeAfter;
    public long activeTill = -1;
    @NotBlank
    public String optionType;
    @NotBlank
    public String tools;
    public double strikePrice;
    public int cellsAvailable;
    public double tokenPerCell;
    @NotBlank
    public String tokenName;
    public double tokenTotalWeight;
    public int quantityOfLots = 1;
    @NotBlank
    public String optionName;
    public MultipartFile imageFile;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public int getCellsAvailable() {
        return cellsAvailable;
    }

    public void setCellsAvailable(int cellsAvailable) {
        this.cellsAvailable = cellsAvailable;
    }

    public double getTokenPerCell() {
        return tokenPerCell;
    }

    public void setTokenPerCell(double tokenPerCell) {
        this.tokenPerCell = tokenPerCell;
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

//    public double getMeatPerCell() {
//        return meatPerCell;
//    }
//
//    public void setMeatPerCell(double meatPerCell) {
//        this.meatPerCell = meatPerCell;
//    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public long getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(long activeTill) {
        this.activeTill = activeTill;
    }

    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null) {
                return true;
            }
        return false;
    }
}
