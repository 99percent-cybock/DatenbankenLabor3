import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * JDBC Aufgabe 3c
 *
 * Diese Klasse bietet Zugriff auf die Zusammenhänge zwischen Kunden und
 * Lieferanten.
 *
 * Ziele
 * - Komplexe Join-Abfragen unter JDBC
 * - Vermeiden von Code-Redundanzen / Designentscheidungen Java vs. SQL
 * - Saubere Ressourcen-Freigabe
 *
 * In dieser Datei sollen Sie:
 * 1. getKundeLieferanten schreiben.
 *    Die folgende Ausgabe wird beim Aufruf der main() Methode erwartet:
 *
 *     kunde                          | knr         | lieferant                      | lnr
 *     CHAR                           | NUMBER      | CHAR                           | NUMBER
 *    --------------------------------+-------------+--------------------------------+-------------
 *     Biker Ecke                     |           5 |                                |
 *     Fahrrad Shop                   |           1 | Firma Gerti Schmidtner         |           1
 *     Fahrräder Hammerl              |           6 | Firma Gerti Schmidtner         |           1
 *     Fahrräder Hammerl              |           6 | MSM GmbH                       |           5
 *     Maier Ingrid                   |           3 |                                |
 *     Rafa - Seger KG                |           4 | Firma Gerti Schmidtner         |           1
 *     Rafa - Seger KG                |           4 | Rauch GmbH                     |           2
 *     Zweirad-Center Staller         |           2 |                                |
 *     (8 rows)
 *
 *    "kunde";"knr";"lieferant";"lnr"
 *    "Rafa - Seger KG";4;"Firma Gerti Schmidtner";1
 *    "Rafa - Seger KG";4;"Rauch GmbH";2
 *
 *  2. Für eine saubere Ressourcen-Freigabe sorgen.
 *
 *  3. Diese Fragen müssen bei der Abgabe beantwortet sein:
 *
 *  3.a Warum und in welchen Fällen bieten Prepared Statements Performance-
 *      vorteile gegenüber dynamisch generierten Abfragen?
 *  3.b Was sind die Sicherheitsvorteile von Prepared Statements gegenüber
 *      dynamisch erzeugten Abfragen?
 */
public class CustomerSupplierRelations {

    /**
     * Die verwendete SQL Connection.
     */
    private Connection connection;

    /**
     * Ein Statement, das Lieferanten für Kunden listet.
     */
    private PreparedStatement stmtKundeLieferanten;

    /**
     * Konstruktor
     * <p>
     * Erstellen der Datenbankverbindung über SQLConnector.
     *
     * @throws SQLException Falls ein Verbindungsaufbau oder ein Statement scheitert
     */
    public CustomerSupplierRelations()
            throws SQLException {

        // TODO begin
        // TODO end
    }

    /**
     * Gibt ein ResultSet zurück, das alle Zulieferer, von denen Teile für
     * einen Kunden mit der gegebenen Nummer nachgefragt wurden, auflistet.
     * <p>
     * Stellt sicher, dass ein Kunde auch dann gelistet wird, wenn keine
     * Zulieferer gefunden werden.
     *
     * @param kdNr Die Kundennr des Kunden, für den Zulieferer ermittelt
     *             werden sollen, bei der Nummer 0 wird die Liste für alle
     *             Kunden erzeugt
     * @return Zulieferer für einen gegebenen Kunden
     * @throws SQLException Falls das ausgeführe Statement scheitert
     */
    public ResultSet getKundeLieferanten(int kdNr) throws SQLException {
        // TODO begin
        return null;
        // TODO end
    }


    /**
     * Gibt alle Resourcen frei.
     */
    public void close() throws SQLException {
        // Hinweis: Stellen Sie sicher, dass dies wirklich aufgerufen wird.
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

        CustomerSupplierRelations csr = new CustomerSupplierRelations();

        ResultSet rs = csr.getKundeLieferanten(0);
        Output.printResultTable(rs, System.out);

        rs = csr.getKundeLieferanten(4);
        Output.resultToCsv(rs, System.out);

        // Hinweis: schließen sie alle Ressourcen
        // TODO begin
        // TODO end
    }

}