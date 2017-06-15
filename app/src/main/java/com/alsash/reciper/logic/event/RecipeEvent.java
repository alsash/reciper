package com.alsash.reciper.logic.event;

import com.alsash.reciper.logic.action.RecipeAction;

public class RecipeEvent {

    public final RecipeAction action;
    public final String uuid;

    public RecipeEvent(RecipeAction action, String uuid) {
        this.action = action;
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeEvent)) return false;
        RecipeEvent event = (RecipeEvent) o;
        if (action != event.action) return false;
        return uuid != null ? uuid.equals(event.uuid) : event.uuid == null;
    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }
}
