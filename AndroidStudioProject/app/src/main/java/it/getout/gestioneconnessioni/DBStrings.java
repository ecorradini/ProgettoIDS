package it.getout.gestioneconnessioni;

/**
 * la classe contiene le definizioni delle costanti che serviranno per le query al dB
 */
public class DBStrings {
    //nomi delle tabelle
    public static final String TABLE_EDIFICIO="EDIFICIO";
    public static final String TABLE_PIANO="PIANO";
    public static final String TABLE_AULA="AULA";
    public static final String TABLE_TRONCO="TRONCO";
    public static final String TABLE_BEACON="BEACON";
    public static final String TABLE_MAPPA="MAPPA";
    public static final String TABLE_PARAMETRI="PARAMETRI";

    //colonne tabelle
    public static final String COL_NOME="NOME";
    public static final String COL_EDIFICIO="EDIFICIO";
    public static final String COL_PIANO="PIANO";
    public static final String COL_X="X";
    public static final String COL_Y="Y";
    public static final String COL_XF="XF"; // x del beacon di fine del tronco
    public static final String COL_YF="YF"; // y del beacon di fine del tronco
    public static final String COL_ID="ID";
    public static final String COL_LARGHEZZA="LARGHEZZA";
    public static final String COL_IMMAGINE="MAPPA";
    public static final String COL_TRONCO="TRONCO";
    public static final String COL_ENTRATA="ENTRATA";
    public static final String COL_LUNGHEZZA="LUNGHEZZA";
    public static final String COL_USCITA="USCITA";
    public static final String COL_UTENTI="UTENTI";
    public static final String COL_VULN="VULN";
    public static final String COL_RV="RV";
    public static final String COL_PF="PF";
}

