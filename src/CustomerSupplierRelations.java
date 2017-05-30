import java.sql.*;


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

        connection = SQLConnector.getTestInstance().getConnection();
        String stmt = "SELECT x.kunde AS \"kunde\", x.knr AS \"knr\", x.lieferant AS \"lieferant\", x.lnr AS \"lnr\"\n" +
                "FROM (\n" +
                "\tSELECT DISTINCT Kunde.name AS kunde, Kunde.nr AS knr, Lieferant.name AS lieferant, Lieferant.nr AS lnr, Auftrag.auftrnr FROM Lieferung\n" +
                "\tJOIN LIEFERANT\n" +
                "\tON  Lieferant.nr = Lieferung.liefnr\n" +
                "\tINNER JOIN teilestamm \n" +
                "\tON teilestamm.teilnr = Lieferung.teilnr\n" +
                "\tINNER JOIN Auftragsposten\n" +
                "\tON Auftragsposten.teilnr = teilestamm.teilnr\n" +
                "\tINNER JOIN Auftrag\n" +
                "\tON Auftrag.auftrnr = Auftragsposten.auftrnr\n" +
                "\tRIGHT OUTER JOIN Kunde\n" +
                "\tON Kunde.nr = Auftrag.kundnr\n" +
                "\t) AS x WHERE x.knr = ?" + "OR 1 = ?" +
                "\tORDER BY x.kunde";
        stmtKundeLieferanten = connection.prepareStatement(stmt);

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
        stmtKundeLieferanten.setInt(1,kdNr);
        if (kdNr <= 0) {
            stmtKundeLieferanten.setInt(2,1);
        }
        else {
            stmtKundeLieferanten.setInt(2,0);
        }
        return stmtKundeLieferanten.executeQuery();
    }


    /**
     * Gibt alle Resourcen frei.
     */
    public void close() throws SQLException {
        // Hinweis: Stellen Sie sicher, dass dies wirklich aufgerufen wird.
        if (!connection.isClosed()) {
            connection.close();
            System.out.println("Connection successfully closed.");
        }
        else {
            System.out.println("Connection was not open.");
        }

        if (!stmtKundeLieferanten.isClosed()) {
            stmtKundeLieferanten.close();
            System.out.println("Statement successfully closed.");
        }
        else {
            System.out.println("Statement was not open.");
        }

        /* Redundant, da ResultSet mit dem statement automatisch geschlossen wird.
        if (!rs.isClosed()) {
            rs.close();
            System.out.println("ResultSet successfully closed.");
        }
        else {
            System.out.println("ResultSet was not open.");
        }
        */
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
        //System.out.println(rs.isClosed());
        csr.close();
    }

}