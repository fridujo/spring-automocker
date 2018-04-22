package com.github.fridujo.automocker.api.jdbc;


import com.github.fridujo.automocker.api.Resettable;

import javax.sql.DataSource;
import java.util.Map;

public class DataSourceResetter implements Resettable {

    private final Map<String, DataSource> datasourcesByName;

    DataSourceResetter(Map<String, DataSource> datasourcesByName) {
        this.datasourcesByName = datasourcesByName;
    }

    @Override
    public void reset() {
        datasourcesByName.forEach((dbName, dataSource) -> {
            reset(dbName, dataSource);
        });
    }

    private void reset(String dbName, DataSource dataSource) {
        try {
            DataSources.doInConnection(dataSource, c -> {
                Connections.execute(c, "SET REFERENTIAL_INTEGRITY FALSE");
                try {
                    Connections.tables(c)
                        .forEach(tableName -> Connections.truncate(c, tableName));
                } finally {
                    Connections.execute(c, "SET REFERENTIAL_INTEGRITY TRUE");
                }
            });
        } catch (RuntimeException e) {
            throw new IllegalStateException("Could not reset DB[" + dbName + "]", e);
        }
    }
}
