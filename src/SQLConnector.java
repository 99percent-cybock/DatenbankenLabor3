import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * JDBC Aufgabe 3a
 * <p>
 * Ziele
 * Das Ziel dieser Übung ist es, eine erste JDBC Verbindung herzustellen.
 * <p>
 * Um dieses
 * Ziel zu erreichen, muss die vorliegende Klasse abgeleitet werden und die Attribute
 * databaseURL, user, password und driverClassName
 * passend instanziiert werden.
 * Dies kann dann durch Ausführen der main() Methode getestet werden.
 * <p>
 * Alle zum Lösen dieser Aufgabe benötigten Informationen können in der
 * offiziellen Java Dokumentation bzw. im Skript zur Vorlesung und in der
 * Dokumentation zu der verwendeten Datenbanken gefunden werden.
 * <p>
 * Zur Lösung sollten nur an den markierten Stellen fehlende Codezeilen
 * ergänzt werden. Eine Änderung des Klassen-Interface ist nicht gestattet.
 * <p>
 * Jede Code-Lücke ist mit einem TO°DO markiert. Wo nicht vorgegeben, werden
 * unabhängig von der Sichtbarkeit, JavaDoc Kommentare für alle Attribute und
 * Methoden erwartet. Wo es sinnvoll erscheint, sollte auch direkt im Code
 * kommentiert werden.
 * <p>
 * Die nötigen JDBC Treiber für PostgreSQL, MySQL, MSSQL und Oracle liegen diesem
 * Aufgabenpaket bei. Andernfalls finden Sie die Datenbanktreiber für
 * Java Anwendungen auf der Website der jeweiligen Datenbank.
 * <p>
 * Wie sie die Quelldateien und die Treiber in ein Projekt einbinden können
 * ist im Abschnitt "Tipps" im Skript beschrieben.
 * <p>
 * Datenbanken zum Entwickeln müssen entweder mit hska_oracle_bike.sql, hska_mysql_bike.sql,
 * hska_mssql_bike oder hska_pgsql_bike.sql initialisiert werden. Wege,
 * die Skripte auszuführen, sind in den Dokumentationen der Datenbanken zu finden.
 * Die Methode reInitializeDB der Klasse bietet weiterhin eine Abwicklung dieses Vorganges
 * über JDBC direkt an. Schauen Sie sich LabUtilities.main an ...
 * <p>
 * Die Abgabe findet stets im Labor LI.137 unter Oracle statt.
 * <p>
 * In dieser Datei sollen Sie:
 * - Den Grundmechanismus der Klasse SQLConnector für verschiedene Datenbankserver verstehen
 * - Die Ableitung  SQLConnectorOracleLocal für die Daten Ihrer Gruppe anpassen
 * - Durch Aufruf von main diesen Zugang testen
 * - Die Ableitung  LoginDatPostgreSqlPool im Pool testen
 * - Eine Ableitung für Ihre privaten PostgreSql-, MySql-, MsSql-Server schreiben und testen.
 * <p>
 * Die folgenden Ausgaben werden erwartet, wenn eine Datenbank verfügbar ist:
 * Connection successfully established.
 * Connection successfully closed.
 */
public abstract class SQLConnector {

    /*
     * Die folgenden Attribute stellen, die Informationen bereit, die JDBC
     * benötigt, um eine Verbindung mit der Datenbank her zu stellen.
     *
     * Wie sich die "Connection URL" zusammensetzt, zu der Datenbank, die Sie
     * benutzen, können Sie in der jeweiligen Dokumentation des
     * verwendeten Datenbanktreibers finden.
     *
     * Diese finden Sie im Internet z.B. auf der Website der Datenbank.
     *
     * Aus diversen Gründen macht es keinen Sinn, die Verbindungsinformationen
     * direkt im Quellcode an zu geben. Da es aber verschiedene Möglichkeiten gibt, diese
     * Informationen bereit zu stellen und um den Umfang der Aufgabe nicht zu sprengen, sei
     * es hier erlaubt, diese Attribute in abgeleiteten Klassen zu überschreiben.
     * Man hat dann typischerweise mindestens einen SQLConnector für Oracle
     * und einen für PostgreSQL.
     *
     * Sie können aber im selbstständigen Nacharbeiten gerne XML- oder
     * Property-Dateien benutzen.
     * Auch der Einsatz von Programm-Argumenten (d.h. argv von main())
     * oder Umgebungsvariablen sind hier in der Praxis üblich.
     * Hier lohnt es sich dann herauszufinden, wie man diese in der
     * jeweiligen Java-IDE setzen kann.
     *
     * Weiter ist in der Praxis oft ein Hemmschuh, dass DB-Server nur schwer
     * von außen zugänglich sind (so auch der Hs-Oracle-Server).
     * Hier hilft ssh-tunneling oft. Machen Sie sich damit vertraut.
     * Setzen Sie zum Beispiel für Ihr Privat-System einen ssh-Server (sshd)
     * auf, vergeben Sie sich private und public Keys, tunneln Sie Ihren
     * PostgreSQL-/MySQL-/MsSQL-Server nach außen ...
     * Sprechen sie uns an, wenn Sie dabei Unterstützung brauchen!
     *
     */

    /**
     * Die Connection URL zur Datenbank.
     */
    protected String databaseURL = null;

    /**
     * Der Benutzername für die Datenbank.
     */
    protected String user = null;

    /**
     * Das Passwort zum Benutzernamen für die Datenbank.
     */
    protected String password = null;

    /**
     * Der Name der Treiber-Klasse.
     */
    protected String driverClassName = null;


    /**
     * Gibt eine Datenbankverbindung zurück.
     * <p>
     * Führt die folgenden Aktionen aus:
     *
     *  - Lädt die Klasse des verwendeten Datenbanktreibers.
     * (siehe auch Dokumentation des Datenbanktreibers)
     *  - Erstellt eine Verbindung mit Hilfe der Klassenvariablen.
     */
    protected Connection getConnection() {
        try {
            Class.forName(driverClassName);
            Properties props = new Properties();
            props.setProperty("user", this.user);
            props.setProperty("password", this.password);
            return DriverManager.getConnection(databaseURL, props);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        catch (ClassNotFoundException e2) {
            System.out.println(e2.getMessage() + " | Treiber nicht gefunden. ");
            return null;
        }
    }


    /**
     * Gibt den aktuellen SQLConnector zum Testen zurück.
     * Hier kann zwischen Oracle, PostgreSQL, MySql, ...
     * während der Entwicklung bequem gewechselt werden.
     *
     * @return Der SQLConnector
     */
    public static SQLConnector getTestInstance() {
        return new
                //SQLConnectorOracleHsInternal()
                //SQLConnectorOracleSsh()
                SQLConnectorPostgreSqlLocal()
                //SQLConnectorMsSqlLocal()
                //LoginDatPostgreSqlPool()
                ;
    }

    /**
     * Diese Methode wird zum Testen der Implementierung verwendet.
     *
     * @param unused Kommandozeilenargumente nicht verwendet
     * @throws SQLException Bei jedem SQL Fehler
     */
    public static void main(String[] unused) throws SQLException {
        Connection connection = SQLConnector.getTestInstance().getConnection();

        if (null != connection && !connection.isClosed()) {
            System.out.println("Connection successfully established.");
            connection.close();
        } else {
            System.err.println("Establishing the connection failed!");
        }

       //connection.close();  im Falle eines Errors, dass die Verbindung nicht hergestellt werden konnte, macht das nicht wirklich viel Sinn.
    }
}

/**
 * Ableitung von der Klasse SQLConnector für den Zugriff auf HS-interne Oracle-Instanz.
 */
class SQLConnectorOracleHsInternal extends SQLConnector{
    /**
     * Erstellt ein LoginData Objekt für Oracle und legt die Informationen in
     * den Klassenvariablen ab. Für die einfache Lösung weisen Sie der jeweiligen
     * Klassen-Variable einfach den richtigen Wert zu.
     */
    protected SQLConnectorOracleHsInternal() {
        // Bitte auf Ihre Gruppe anpassen ...
        this.databaseURL =     "jdbc:oracle:thin:@iwi-lkit-db-01:1521:LAB1";
        this.user =            "dbprax02";
        this.password =        "dbprax02";
        this.driverClassName = "oracle.jdbc.OracleDriver";
    }
}
/**
 * Ableitung von SQLConnector für Zugriff auf lokale, PostgreSQL-Instanz auf Pool-Rechnern LI.137
 */
class LoginDatPostgreSqlPool extends SQLConnector{
    protected LoginDatPostgreSqlPool() {
        this.databaseURL = "jdbc:postgresql://localhost:5432/dbprax";
        this.user = "dbprax02";
        this.password = "dbprax02";
        this.driverClassName = "org.postgresql.Driver";
    }
}


/**
 * Ableitung von SQLConnector für Zugriff auf lokale, private PostgreSQL-Instanz
 */
class SQLConnectorPostgreSqlLocal extends SQLConnector{
    protected SQLConnectorPostgreSqlLocal() {

        this.databaseURL = "jdbc:postgresql://localhost:5432/postgres";
        this.user = "postgres";
        this.password = "verU9ckv!!"; //System.getenv("verU9ckv!!"); // damit das nicht gedruckt wird ...
        this.driverClassName = "org.postgresql.Driver";
    }
}


/* Weitere Ableitungen für eigene Test-Datenbanken */