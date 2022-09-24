package com.client.eurekaclient.models.response.trap;

import com.client.eurekaclient.models.rabbithunt.trap.Trap;

import java.util.Date;

public class TrapResponse {
    public String name;
    public String trapId;
    public String image;
    public int cells;
    public long created = new Date().getTime();
    public long activeAfter;
    public long activeTill = -1;
    public String tool;
    public int cellsAvailable;
    public String tokenName = "meat";
    public double tokenTotalWeight;
    public double tokenPerCell;
    public int quantityOfLots = 1;
    public boolean status = true;

    public TrapResponse() {}

    public TrapResponse(String name, String trapId, String image, int cells, long created, long activeAfter, long activeTill, String tool, int cellsAvailable, String tokenName, double tokenTotalWeight, double tokenPerCell, int quantityOfLots, boolean status) {
        this.name = name;
        this.trapId = trapId;
        this.image = image;
        this.cells = cells;
        this.created = created;
        this.activeAfter = activeAfter;
        this.activeTill = activeTill;
        this.tool = tool;
        this.cellsAvailable = cellsAvailable;
        this.tokenName = tokenName;
        this.tokenTotalWeight = tokenTotalWeight;
        this.tokenPerCell = tokenPerCell;
        this.quantityOfLots = quantityOfLots;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrapId() {
        return trapId;
    }

    public void setTrapId(String trapId) {
        this.trapId = trapId;
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

    public long getActiveTill() {
        return activeTill;
    }

    public void setActiveTill(long activeTill) {
        this.activeTill = activeTill;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
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

    public int getQuantityOfLots() {
        return quantityOfLots;
    }

    public void setQuantityOfLots(int quantityOfLots) {
        this.quantityOfLots = quantityOfLots;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static TrapResponse build(Trap trap) {
        return new TrapResponse(trap.name, trap.trapId, trap.image, trap.cells, trap.created, trap.activeAfter, trap.activeTill, trap.tool, trap.cellsAvailable, trap.tokenName, trap.tokenTotalWeight, trap.tokenPerCell, trap.quantityOfLots, trap.status);
    }
}
