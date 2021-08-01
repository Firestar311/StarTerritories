package com.starmediadev.plugins.territories;

import com.starmediadev.data.handlers.DataTypeHandler;

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
