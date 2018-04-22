package com.github.fridujo.automocker.api.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {

    public static List<String> tables(Connection c) {
        List<String> tables = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("SHOW TABLES"); ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to list tables", e);
        }
        return tables;
    }

    public static void execute(Connection c, String statement) {
        try (PreparedStatement p = c.prepareStatement(statement)) {
            p.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not execute Statement [" + statement + "]", e);
        }
    }

    public static void truncate(Connection c, String tableName) {
        execute(c, "TRUNCATE TABLE " + tableName);
    }

    public static Object selectFirstLines(Connection c, String tableName, int maxLines) {
        try (PreparedStatement p = c.prepareStatement("SELECT TOP " + maxLines + " * FROM " + tableName);
             ResultSet rs = p.executeQuery()) {

            int columns = rs.getMetaData()
                .getColumnCount();
            List<List<Object>> result = new ArrayList<>();
            while (rs.next()) {
                List<Object> line = new ArrayList<>();
                for (int i = 1; i <= columns; i++) {
                    line.add(rs.getObject(i));
                }
                result.add(line);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not select Table [" + tableName + "]", e);
        }
    }

}
