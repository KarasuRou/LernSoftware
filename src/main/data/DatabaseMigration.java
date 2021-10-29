package data;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMigration {

    private static final DatabaseMigration databaseMigration = new DatabaseMigration();
    private final List<File> list = new ArrayList();
    private boolean started = false;

    private DatabaseMigration() {}

    public void start() throws SQLException {
        if (!started) {
            started = true;
//            ResultSet resultSet = Database.getInstance().executeSelectQuery("SELECT * FROM migration_version");
//            while (resultSet.next()) {
//
//            }
        }
    }

    public static DatabaseMigration getInstance() {
        return databaseMigration;
    }
}
