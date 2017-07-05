package com.alsash.reciper.logic.event;

import com.alsash.reciper.logic.action.CategoryAction;

public class CategoryEvent {

    public final CategoryAction action;
    public final String uuid;

    public CategoryEvent(CategoryAction action, String uuid) {
        this.action = action;
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEvent)) return false;
        CategoryEvent that = (CategoryEvent) o;
        if (action != that.action) return false;
        return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;

    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }
}
