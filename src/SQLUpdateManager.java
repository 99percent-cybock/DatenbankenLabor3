import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC Aufgabe 3d
 *
 * Aktualisieren des Datenbankschemas.
 *
 * Ziele
 * - Arbeiten mit DDL-Befehlen
 * - Erstellen von überwachten Fremdschlüsselbeziehungen
 * - Erste Heranführung an das Thema Normalisierung, hier besonders deren technische Umsetzung
 *
 * In dieser Datei sollen Sie:
 * Redundanzen in der Tabelle Teilestamm, die in der Spalte 'farbe' zu finden sind,
 * vermeiden. Dazu soll eine neue Tabelle 'farbe' angelegt werden.
 * Die Tabelle Teilestamm soll dann für die neue Tabelle 'farbe' einen
 * Fremdschlüssel verwenden.
 * Bei dieser Gelegenheit werden die Farben in der Tabelle 'farbe' um
 * zusätzliche Informationen ergänzt.
 *
 * Die Tabelle 'farbe' bekommt das folgende physikalische Datenmodell:
 *
 * 	Spalte	Beschreibung
 * 	nr		Der Type ist INTEGER, automatisches Hochzählen ist erlaubt, aber
 *  		nicht notwendig (es gibt leider keine einheitliche Syntax, die zwischen Oracle, MySQL
 *  		und PostgreSQL kompatibel ist). Diese Spalte bildet den
 *  		Primärschlüssel.
 *	name	Hat die gleichen Eigenschaften wie 'teilestamm.farbe', aber die Spalte
 *			darf keine Duplikate enthalten.
 *	rot, gruen, blau
 *			Diese Spalten sind vom Typ REAL in einem Wertebereich von
 *			[0.0; 1.0], der sichergestellt werden muss.
 *			Der Standardwert ist 0.
 *
 * Test Ausgabe
 *
 * Die folgende Ausgabe sollte auf System.out erscheinen, wenn die main()
 * Methode zum ersten mal aufgerufen wird:
 * Updating database layout ...
 * Table 'farbe' created.
 * Added 3 rows to 'farbe'
 * Column 'farbnr' added to table 'teilestamm'
 * Set 'teilestamm.farbnr' in 34 rows
 * Column 'farbe' removed from 'teilestamm'
 *
 * Hinweis:
 * Setzen Sie die Methode LabUtilities.reInitializeDB() ein,
 * um die Datenbank immer wieder neu aufzusetzen beim Testen.
 */
public class SQLUpdateManager  {

    /**
     * Die verwendete SQL Verbindung.
     */
    private Connection connection;

    /**
     * Der Konstruktor, löst den Update-Vorgang aus.
     * <p>
     * Stellt die Verbindung zur Datenbank her und schließt diese auch wieder.
     *
     * @throws SQLException Wird geworfen, wenn die Datenbankverbindung oder ein
     *                      Statement scheitert
     */
    public SQLUpdateManager() throws SQLException {
        // TODO begin
        // TODO end

        if (!hasTable("farbe")) {
            update();
        } else {
            String err = "Table 'farbe' already created!";
            System.err.println(err);
            throw new SQLException(err);
        }
        // TODO begin
        // TODO end
    }

    /**
     * Prüft, ob eine Tabelle existiert.
     *
     * @param table Die zu prüfende Tabelle
     * @return True, falls die Tabelle existiert, sonst False
     * @throws SQLException Im Fall von Verbindungsproblemen
     */
    private boolean hasTable(String table) throws SQLException {
        // TODO begin
        return false;
        // TODO end
    }

    /**
     * Aktualisiere das Datenbanklayout.<p>
     *
     * Führt die folgenden Aktionen aus:
     * - Geeignete Transaktions-Isolationsebene setzen ...
     * - Tabelle farbe anlegen
     * - Vorhandene Farben von teilestamm.farbe in farbe.name kopieren
     * - RGB Werte zu farbe Einträgen setzen
     * - In teilestamm die Spalte farbnr (als Foreign Key) anlegen
     * - Die Spalte teilestamm.farbnr mit Werten befüllen
     * - Die Spalte teilestamm.farbe entfernen
     * - Im Erfolgsfall Änderungen committen, sonst zurückrollen
     *
     * @throws SQLException Im Fall von Verbindungsproblemen
     */
    private void update() throws SQLException {
        System.out.println("Updating database layout ...");

        // TODO begin
        // TODO end
    }

    /**
     * Diese Methode wird zum Testen der Implementierung verwendet.
     *
     * @param args Kommandozeilenargumente, nicht verwendet
     * @throws SQLException Bei jedem SQL Fehler
     */
    public static void main(String[] args) throws SQLException {
        new SQLUpdateManager();
    }
}