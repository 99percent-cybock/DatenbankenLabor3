import java.sql.Statement;
import java.sql.Timestamp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Diese Klasse dient zurzeit vor allem dem (Wieder-) Aufsetzen der Labor-Datenbank.
 * An dieser Klasse braucht nichts geändert zu werden.
 */
public class LabUtilities {
    private Connection connection;

    /**
     * Konstruktor
     *
     * @param connection Datenbankverbindung. Muss extern geschlossen werden.
     */
    LabUtilities(Connection connection) {
        this.connection = connection;
    }

    /**
     * Sehr einfach gehaltene Methode, um die Datenbank aus SQL-Datei wiederherstellen.
     * - Alle Tabllen mit Inhalt ohne Nachfrage löschen
     * - Alle Tabellen wieder herstellen
     * - Tabellen mit Daten füllen
     * <p>
     * Getestet mit MsSQL 12, MySql 5.7, Oracle 11g, PostgreSQL 9.4
     * <p>
     * Das entsprechende Sql-Skript befindet sich im Ordner ./sql.
     */
    public void reInitializeDB() {
        try {
            System.out.println("\nInitializing DB.");
            this.connection.setAutoCommit(true);
            String productName = this.connection.getMetaData().getDatabaseProductName();
            boolean isMsSql = productName.equals("Microsoft SQL Server");
            Statement statement = this.connection.createStatement();
            int numStmts = 0;
            for (String sqlString : this.readFileToSqlStrings("sql/hska_bike.sql")) {
                try {
                    if (isMsSql) {   // Microsoft does not know "DATE" operator ...
                        sqlString = sqlString.replace(", DATE '", ", '");
                    }
                    statement.execute(sqlString);
                    System.out.print((++numStmts % 80 == 0 ? "/\n" : "."));
                } catch (SQLException e) {
                    System.out.print("\n" + sqlString.replace('\n', ' ').trim() + ": ");
                    System.out.println(e.getMessage());
                }
            }
            statement.close();
            System.out.println("\nBike database is reinitialized on " + productName +
                    "\nat URL " + this.connection.getMetaData().getURL()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mehrere Datenbanken, die zum Testen verwendet werden, wiederherstellen ...
     */
    public static void reInitializeAllMyUsedDBs() {
        try {
            SQLConnector SQLConnectors[] =
                    {
                            //new SQLConnectorMsSqlLocal(),
                            //new SQLConnectorMySqlLocal(),
                            //new SQLConnectorMySqlSsh(),
                            new SQLConnectorOracleHsInternal(),
                            //new SQLConnectorOracleSsh(),
                            //new SQLConnectorPostgreSqlLocal(),
                            new LoginDatPostgreSqlPool(),
                    };
            for (SQLConnector connector : SQLConnectors) {
                Connection connection = connector.getConnection();
                LabUtilities theInstance = new LabUtilities(connection);
                theInstance.reInitializeDB();
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Datei einlesen und als String-Array von SQL-Anweisungen zurückgeben.
     * Dient hier dem Einlesen von SQL-Dateien.
     *
     * @param Pfad einschließlich Dateinamen
     * @return Zeichenketten des Dateiinhalts, der durch ';' separiert ist.
     * @throws IOException
     */
    String[] readFileToSqlStrings(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8).split(";");
    }

    /**
     * Starter für das Neuaufsetzen der Datenbank.
     *
     * @param args -- Keine Programargumente genutzt
     * @throws SQLException
     */
    public static void main(String[] args) throws Exception {
        new LabUtilities(SQLConnector.getTestInstance().getConnection()).reInitializeDB();
        // LabUtilities.reInitializeAllMyUsedDBs();
    }
}