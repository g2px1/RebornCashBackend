package com.client.eurekaclient.models.request.goldenrush.token;

public class DistributeTokenRequest {
    public double poolSize;

    public DistributeTokenRequest(double poolSize) {
        this.poolSize = poolSize;
    }

    public double getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(double poolSize) {
        this.poolSize = poolSize;
    }
}
