package com.client.eurekaclient.models.lock;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class CellsLock {
    @Id
    public String id;
    @Indexed(unique = true)
    public String cellsPackUuid;

    public CellsLock(String cellsPackUuid) {
        this.cellsPackUuid = cellsPackUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCellsPackUuid() {
        return cellsPackUuid;
    }

    public void setCellsPackUuid(String cellsPackUuid) {
        this.cellsPackUuid = cellsPackUuid;
    }
}
