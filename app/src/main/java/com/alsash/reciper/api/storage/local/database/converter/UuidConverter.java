package com.alsash.reciper.api.storage.local.database.converter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.UUID;

/**
 * Property converter from UUID to String and backward
 */
public class UuidConverter implements PropertyConverter<UUID, String> {
    @Override
    public UUID convertToEntityProperty(String databaseValue) {
        return UUID.fromString(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(UUID entityProperty) {
        return entityProperty.toString();
    }
}
