package com.client.eurekaclient.models.request;

public class ProductSeekingRequest {
    public String uuid;
    public boolean status;
    public int page;
    public boolean direction; // true - asc, false - desc
    public String type;
    public String tokenName;
    public String trapName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }
}
