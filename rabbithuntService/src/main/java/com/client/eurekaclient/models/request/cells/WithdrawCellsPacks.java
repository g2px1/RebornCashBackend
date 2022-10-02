package com.client.eurekaclient.models.request.cells;

import com.client.eurekaclient.models.request.NotNullRequest;

public class WithdrawCellsPacks extends NotNullRequest {
    public String uuid;
    public String code;
    public String chainName;

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
