package com.github.fridujo.automocker.api.jdbc;

import com.github.fridujo.automocker.utils.ThrowingConsumer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class DataSources {

    private DataSources() {
    }

    public static void doInConnection(DataSource datasource, ThrowingConsumer<Connection> task) {
        try (Connection c = datasource.getConnection()) {
            ThrowingConsumer.silent(task)
                .accept(c);
            c.commit();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void populateTable(DataSource datasource, String tableName, List<String> columns,
                                     List<List<String>> values) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName)
            .append(" (");
        sql.append(columns.stream()
            .map(String::toUpperCase)
            .collect(Collectors.joining(", ")));
        sql.append(") VALUES (");
        sql.append(IntStream.range(0, columns.size())
            .mapToObj(i -> "?")
            .collect(Collectors.joining(", ")));
        sql.append(")");
        doInConnection(datasource, c -> {
            try (PreparedStatement ps = c.prepareStatement(sql.toString())) {
                for (List<String> line : values) {
                    int i = 1;
                    for (String value : line) {
                        ps.setString(i, value);
                        i++;
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        });
    }
}
