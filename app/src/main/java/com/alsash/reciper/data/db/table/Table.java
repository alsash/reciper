package com.alsash.reciper.data.db.table;

import java.util.Date;

/**
 * A Table interface than defines mandatory fields for each table
 */
public interface Table {

    Long getId();

    void setId(Long id);

    String getUuid();

    void setUuid(String uuid);

    Date getChangedAt();

    void setChangedAt(Date date);
}
