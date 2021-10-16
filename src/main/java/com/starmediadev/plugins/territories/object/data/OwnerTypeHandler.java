package com.starmediadev.plugins.territories.object.data;

import com.starmediadev.data.model.DataTypeHandler;
import com.starmediadev.plugins.territories.object.owner.AdminOwner;
import com.starmediadev.plugins.territories.object.owner.PlayerOwner;
import com.starmediadev.plugins.territories.object.owner.ServerOwner;
import com.starmediadev.plugins.territories.object.owner.TerritoryOwner;

import java.util.UUID;

public class OwnerTypeHandler extends DataTypeHandler<TerritoryOwner> {
    public Object serializeSql(Object object) {
        return object.getClass().getSimpleName() + ":" + ((TerritoryOwner) object).getIdentifier();
    }

    public TerritoryOwner deserialize(Object object) {
        if (!(object instanceof String str)) {
            return null;
        }
        
        String[] raw = str.split(":");
        if (raw[0].equalsIgnoreCase("PlayerOwner") || raw[0].equalsIgnoreCase("AdminOwner")) {
            UUID uuid = UUID.fromString(raw[1]);
            if (raw[0].equalsIgnoreCase("PlayerOwner")) {
                return new PlayerOwner(uuid);
            } else {
                return new AdminOwner(uuid);
            }
        } else if (raw[0].equalsIgnoreCase("ServerOwner")) {
            return new ServerOwner();
        }
        
        return null;
    }
}
