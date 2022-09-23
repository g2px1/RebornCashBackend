package com.client.userService.models.request;

public class UserSeekingRequest {
    public int page = 0;
    public String status;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
