import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * JDBC Aufgabe 3b
 *
 * Diese Klasse stellt einfache Ausgabefunktionen für ResultSet-Instanzen zur Verfügung.
 *
 * Ziele
 * - Erstes Arbeiten mit Connection, Statement und ResultSet
 * - Aktives Arbeiten mit Metadaten
 *
 * In dieser Datei sollen Sie:
 * 1. Die Methode printResultTable ändern, so dass auch die Typnamen mit ausgegeben werden.
 *
 *   PERSNR                 | NAME                           | ORT                  | AUFGABE
 *   NUMBER                 | CHAR                           | CHAR                 | CHAR
 *  ------------------------+--------------------------------+----------------------+---------
 *                        1 | Maria Forster                  | Regensburg           | Manager
 *                        2 | Anna Kraus                     | Regensburg           | Vertreter
 *                      ...
 *                        9 | Ernst Pach                     | Stuttgart            | Azubi
 * 2. Weiter soll die Methode resultToCsv() zum Export ins CSV Format implementiert werden.
 *
 *    Beispielausgabe:
 *     "nr";"name";"strasse";"plz";"ort";"sperre"
 *     1;"Fahrrad Shop";"Obere Regenstr. 4";93059;"Regensburg";"0"
 *     2;"Zweirad-Center Staller";"Kirschweg 20";44267;"Dortmund";"0"
 *     3;"Maier Ingrid";"Universitätsstr. 33";93055;"Regensburg";"1"
 *     4;"Rafa - Seger KG";"Liebigstr. 10";10247;"Berlin";"0"
 *     5;"Biker Ecke";"Lessingstr. 37";22087;"Hamburg";"0"
 *     6;"Fahrräder Hammerl";"Schindlerplatz 7";81739;"München";"0"*
 *
 * Zum Testen bitte jeweils die main-Methode ausführen.
 *
 */
public class Output {

    /**
     * Die maximale Spaltenbreite für formatierte Ausgaben.
     */
    private static final int MAX_COL_WIDTH = 30;

    /**
     * Gibt zurück, ob ein Typ (aus java.sql.Types) in Anführungszeichen
     * dargestellt werden sollte.
     *
     * @param type Alle aus Types.*
     * @return False numerische Werte, für alles andere true
     */
    private static boolean isQuotedType(int type) {
        // Numerische Werte nicht in Anführungszeichen
        switch (type) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.NULL:
            case Types.BOOLEAN:
                return false;
        }
        // Alles andere in Anführungszeichen
        return true;
    }

    /**
     * Gibt eine uebergeordnete Bezeichnung fuer die uebergebene sql.TypeNumber zurueck.
     * Zusatzfunktion, spezifisch fuer Aufgabe 3b.1
     * @param type Alle aus java.sql.Types.*
     * @return "number" fuer numerische Werte, "char" fuer Charakter, sonst entsprechend des Types.
     */
    private static String getType(int type) {
        switch (type) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return "number";
            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
                return "char";
            case Types.BOOLEAN:
                return "boolean";
            case Types.NULL:
                return "null";
        }
        return "";
    }

    /**
     * Gibt eine gegebene ResultSet-Instanz im CSV-Format aus.
     * <p>
     * Felder werden durch Semikolon getrennt, quotierte Typen werden
     * "getrimmt" (String.trim()) und in doppelte Anführungszeichen gestellt.
     *
     * @param rs  Die ResultSet Instanz, die auszugeben ist
     * @param out Die PrintStream Instanz, auf die ausgegeben wird, z.B. System.out
     * @throws SQLException Im Falle von Verbindungsproblemen
     */
    public static void resultToCsv(ResultSet rs, PrintStream out)
            throws SQLException {

        // (Aufgabe 3b.2)
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();

        //Ausgabe der Spaltennamen
        for (int column = 1; column <= columns; column++) {
            out.print('"' + meta.getColumnLabel(column) + '"');
            out.print(column < columns ? ";" : "\n");
        }

        //Ausgabe aller Zeilen aus ResultSet;
        while(rs.next()) {
            for (int column = 1; column <= columns; column++) {
                String cell = rs.getString(column);
                if (cell != null) {
                    //cell = cell.replaceAll("\\s+$", "");
                    cell = cell.trim();
                }
                out.print(isQuotedType(meta.getColumnType(column)) ? '"' + cell + '"' : cell);
                out.print(column < columns ? ";" : "\n");
            }
        }
    }

    /**
     * Gibt ein ResultSet ähnlich wie ein SQL Kommandozeilen Client aus.
     * <p>
     * Numerische Datentypen sind rechtsbündig, andere (in Anführungszeichen
     * stehende) linksbündig ausgerichtet.
     * <p>
     * Diese Funktion beachtet MAX_COL_WIDTH als Grenze für die maximale
     * Spaltenbreite. Das heißt Felder, die breiter sind, werden bei der
     * Darstellung abgeschnitten.
     *
     * @param rs  Eine ResultSet Instanz
     * @param out Die PrintStream Instanz zur Ausgabe, z.B. System.out
     * @throws SQLException Im Falle von Verbindungsproblemen
     */
    public static void printResultTable(ResultSet rs, PrintStream out)
            throws SQLException {
        // Erstellen eines horizontalen Abstandhalters in ausreichender Länge
        String horizSeparator = "--------------------------------------------";
        while (horizSeparator.length() < Output.MAX_COL_WIDTH) {
            horizSeparator += horizSeparator;
        }

        // Ausgabe der Überschriften und Ausrichtung und Breite der Spalten ermitteln
        ResultSetMetaData meta = rs.getMetaData();
        int columns = meta.getColumnCount();
        int[] width = new int[columns];
        boolean[] leftAligned = new boolean[columns];
        for (int column = 1; column <= columns; column++) {
            leftAligned[column - 1] = isQuotedType(meta.getColumnType(column));
            width[column - 1] = meta.getColumnDisplaySize(column);
            width[column - 1] = width[column - 1] > Output.MAX_COL_WIDTH
                    ? Output.MAX_COL_WIDTH : width[column - 1];                             // var = Bed ? A : B ==> Falls die Bedingung true ist: var = A, Falls false: var = B
            out.printf((column > 1 ? "| " : " ") + "%-" + width[column - 1]
                    + "." + width[column - 1] + "s ", meta.getColumnLabel(column));
        }
        out.println();

        // (Aufgabe 3b.1)
        // Zeile mit den Typen ausgeben
        for (int column = 1; column <= columns; column++) {
            leftAligned[column - 1] = isQuotedType(meta.getColumnType(column));
            width[column - 1] = meta.getColumnDisplaySize(column);
            width[column - 1] = width[column - 1] > Output.MAX_COL_WIDTH
                    ? Output.MAX_COL_WIDTH : width[column - 1];
            String type =  getType(meta.getColumnType(column));
            // String type = meta.getColumnTypeName;
            out.printf((column > 1 ? "| " : " ") + "%-" + width[column - 1]
                    + "." + width[column - 1] + "s ", type);
        }
        out.println();

        // Ausgabe horizontaler Abstandhalter
        for (int column = 1; column <= columns; column++) {
            out.printf((column > 1 ? "+-" : "-") + "%-" + width[column - 1]
                    + "." + width[column - 1] + "s-", horizSeparator);
        }
        out.println();

        // Ausgabe aller Zeilen aus ResultSet
        int rows = 0;
        while (rs.next()) {
            for (int column = 1; column <= columns; column++) {
                String cell = rs.getString(column);
                out.printf((column > 1 ? "| " : " ")
                        + (leftAligned[column - 1] ? "%-" : "%") + width[column - 1]
                        + "." + width[column - 1] + "s ", (cell != null ? cell : ""));
            }
            rows++;
            out.println();
        }
        out.printf("(%d rows)%n%n", rows);
    }

    /**
     * Diese Methode wird zum Testen der Implementierung verwendet.
     *
     * @param unused Kommandozeilenargumente nicht verwendet
     * @throws SQLException Bei jedem SQL Fehler
     */
    public static void main(String[] unused) throws SQLException {
        Connection connection = SQLConnector.getTestInstance().getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultset = statement.executeQuery(
                "SELECT * FROM personal"); //SELECT persnr, name, ort, aufgabe FROM personal
        Output.printResultTable(resultset, System.out);

        ResultSet resultset2 = statement.executeQuery("SELECT * FROM personal"); //SELECT * FROM personal
        Output.resultToCsv(resultset2, System.out);

        connection.close();
        statement.close();
        connection.close();
    }

}