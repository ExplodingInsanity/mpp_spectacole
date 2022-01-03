public class domainUtils {
    public static int getNrLocuri(String lista_locuri){
        return lista_locuri.split(" ").length;
    }
    public static int getNrLocuriSpatiu(String lista_locuri){
        return lista_locuri.split(" ").length;
    }
    public static float getPretTotal(String lista_locuri, Float pretPerLoc){
        return getNrLocuri(lista_locuri)*pretPerLoc;
    }
    public static float getLocuriDisponibile(String lista_locuri, Integer nr_locuri){
        return nr_locuri-getNrLocuri(lista_locuri);
    }

    public static String locuriSpectacol = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public static int nrLocuriSpectacol = locuriSpectacol.length();
}
