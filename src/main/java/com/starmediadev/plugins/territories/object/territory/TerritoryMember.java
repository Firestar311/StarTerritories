package com.starmediadev.plugins.territories.object.territory;

import com.starmediadev.data.annotations.TableInfo;
import com.starmediadev.data.model.AbstractDataObject;

import java.util.UUID;

@TableInfo(tableName = "territorymembers")
public class TerritoryMember extends AbstractDataObject {
    private UUID uuid;
}
