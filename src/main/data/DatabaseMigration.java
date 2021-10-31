package data;

import logic.miscellaneous.Output;
import logic.startup.Launcher;
import model.data.MigrationFile;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class DatabaseMigration { // id version checksum

    private static final DatabaseMigration databaseMigration = new DatabaseMigration();
    private List<MigrationFile> fileMigrationList = new ArrayList<>();
    private List<MigrationFile> databaseMigrationList = new ArrayList<>();
    private File[] listOfFiles;
    private boolean started = false;

    private DatabaseMigration() {}

    public void start() throws Exception {
        if (!started) {
            started = true;

            createMigrationVersionTableIfNotExists();
            loadDatabaseMigration();
            loadTheoreticalMigration();
            compareChecksumsAndVersions();
            addUnloadedMigrations();
        }
    }

    public static DatabaseMigration getInstance() {
        return databaseMigration;
    }

    private void createMigrationVersionTableIfNotExists() throws SQLException {
        Database.getInstance().startInsertQuery("CREATE TABLE IF NOT EXISTS migration_version" +
            "(id INTEGER NOT NULL CONSTRAINT migration_version_pk PRIMARY KEY AUTOINCREMENT," +
            "version double NOT NULL," +
            "checksum long NOT NULL);" +
            "CREATE UNIQUE INDEX IF NOT EXISTS migration_version_id_uindex" +
            "    ON migration_version (id);" +
            "CREATE UNIQUE INDEX IF NOT EXISTS migration_version_version_uindex" +
            "    ON migration_version (version);").addBatch();
        Database.getInstance().executeInsertQuery();
    }

    private void loadDatabaseMigration() throws SQLException {
        ResultSet resultSet = Database.getInstance().executeSelectQuery("SELECT * FROM migration_version");
        if (resultSet != null) {
            while (resultSet.next()) {
                MigrationFile migrationFile = new MigrationFile();
                migrationFile.setVersion(resultSet.getDouble("version"));
                migrationFile.setChecksum(resultSet.getLong("checksum"));
                databaseMigrationList.add(migrationFile);
            }
        } else {
            databaseMigrationList = null;
        }
    }

    private void loadTheoreticalMigration() throws Exception {
        File folder = new File(Launcher.APPLICATION_PATH + Launcher.FILE_SEPARATOR + "migration" + Launcher.FILE_SEPARATOR);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            throw new Exception("No Migration's found!");
        }
        this.listOfFiles = listOfFiles;
        MigrationFile[] migrationFiles = new MigrationFile[listOfFiles.length];

        if (listOfFiles[0] != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    MigrationFile migrationFile = new MigrationFile();
                    migrationFile.setVersion(getVersionFromFile(listOfFiles[i]));
                    migrationFile.setChecksum(getChecksumFromFile(listOfFiles[i]));

                    if (migrationFile.getVersion() == 0) {
                        throw new Exception("Unknown version for file: " + listOfFiles[i].getName());
                    }

                    migrationFiles[i] = migrationFile;
                }
            }
        }

        fileMigrationList.addAll(Arrays.asList(sortMigrationList(migrationFiles)));
    }

    private void compareChecksumsAndVersions() throws Exception {
        if (databaseMigrationList.size() > fileMigrationList.size()) {
            throw new Exception("Missing Migrations! Saved: \"" + databaseMigrationList.size() + "\" File: \"" + fileMigrationList.size() + "\"");
        }
        for (int i = 0; i < databaseMigrationList.size(); i++) {
            MigrationFile databaseMigration = databaseMigrationList.get(i);
            MigrationFile fileMigration = fileMigrationList.get(i);

            if (databaseMigration.getVersion() != fileMigration.getVersion()) {
                throw new Exception("Unequal Versions! Saved: \"" + databaseMigration.getVersion() + "\" File: \"" + fileMigration.getVersion() + "\"");
            }
            if (databaseMigration.getChecksum() != fileMigration.getChecksum()) {
                throw new Exception("Unequal Checksum! Saved: \"" + databaseMigration.getChecksum() + "\" File: \"" + fileMigration.getChecksum() + "\"");
            }

        }
    }

    private void addUnloadedMigrations() throws SQLException, IOException {
        for (int i = databaseMigrationList.size(); i < fileMigrationList.size(); i++) {
            String[] sql = getSQLFromFile(fileMigrationList.get(i));
            Output.write("Migrating Version (" + fileMigrationList.get(i).getVersion() + "):\r\n" +
                    Arrays.toString(sql).replace("[", "").replace("]", "").replace(";,", ";"));

            for (String batch : sql) {
                Database.getInstance().startInsertQuery(batch).addBatch();
                Database.getInstance().executeInsertQuery();
            }
            Output.write("INSERT INTO migration_version (version,checksum) " +
                    "VALUES (" + fileMigrationList.get(i).getVersion() + "," + fileMigrationList.get(i).getChecksum() + ");");
            PreparedStatement preparedStatement = Database.getInstance().startInsertQuery("INSERT INTO migration_version (version,checksum) VALUES (?,?);");
            preparedStatement.setDouble(1, fileMigrationList.get(i).getVersion());
            preparedStatement.setLong(2, fileMigrationList.get(i).getChecksum());
            preparedStatement.addBatch();
            Database.getInstance().executeInsertQuery();
        }
    }

    private String[] getSQLFromFile(MigrationFile migrationFile) throws IOException {
        String sql = "";
        for (File file : listOfFiles) {
            if (file.getName().contains(String.valueOf(migrationFile.getVersion())) || file.getName().contains(String.valueOf(((int) migrationFile.getVersion())))
            ) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    sql += line + "\n";
                }
                reader.close();
            }
        }
        String[] batches = sql.split(";");
        String[] realBatches = new String[batches.length - 1];
        for (int i = 0; i < batches.length-1; i++) {
            realBatches[i] = batches[i] + ";";
        }
        return realBatches;
    }

    private MigrationFile[] sortMigrationList(MigrationFile[] unsortedList) {
        for (int i = 0; i < unsortedList.length; i++) {
            for (int j = 0; j < unsortedList.length - 1 - i; j++) {
                if (unsortedList[i].getVersion() > unsortedList[i + 1].getVersion()) {
                    MigrationFile temp = unsortedList[i + 1];
                    unsortedList[i + 1] = unsortedList[i];
                    unsortedList[i] = temp;
                }
            }
        }
        return unsortedList;
    }

    private int getVersionFromFile(File file) {
        int version = 0;
        String fileName = file.getName();
        if (fileName.contains("__") && fileName.contains(".sql")) {
            String[] strips = fileName.split("__");
            version = Integer.parseInt(strips[0]);
        }
        return version;
    }

    private long getChecksumFromFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, new CRC32());
        BufferedInputStream inputStream = new BufferedInputStream(checkedInputStream);

        while (inputStream.read() != -1) {}
        inputStream.close();

        return checkedInputStream.getChecksum().getValue();
    }
}
