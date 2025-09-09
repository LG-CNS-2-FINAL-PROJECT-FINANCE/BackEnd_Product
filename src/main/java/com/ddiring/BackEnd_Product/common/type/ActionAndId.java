package com.ddiring.BackEnd_Product.common.type;

import lombok.*;

@Getter
@Setter
public class ActionAndId {
    private String action;
    private Long id;
    public static ActionAndId of(String action, Long id) {
        ActionAndId actionAndId = new ActionAndId();
        actionAndId.action = action;
        actionAndId.id = id;
        return actionAndId;
    }
}
