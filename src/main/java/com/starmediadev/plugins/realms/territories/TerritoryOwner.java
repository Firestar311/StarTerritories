package com.starmediadev.plugins.realms.territories;

import com.starmediadev.data.annotations.TableInfo;
import com.starmediadev.data.model.DataInfo;
import com.starmediadev.data.model.IDataObject;

@TableInfo(tableName = "owners")
public abstract class TerritoryOwner implements IDataObject {
    private DataInfo dataInfo;

    public DataInfo getDataInfo() {
        return dataInfo;
    }
}
