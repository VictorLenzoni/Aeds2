import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
class Shows {
    private String show_id;
    private String type;
    private String title;
    private String director;
    private String[] cast;
    private String country;
    private Date date_added;
    private int release_year;
    private String rating;
    private String duration;
    private String[] listed_in;

    public Shows() {
        this.show_id = "";
        this.type = "";
        this.title = "";
        this.director = "";
        this.cast = new String[0];
        this.country = "";
        this.date_added = new Date();
        this.release_year = 0;
        this.rating = "";
        this.duration = "";
        this.listed_in = new String[0];
    }

    public Shows(String linha) {
        ArrayList<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean dentroAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '\"') {
                dentroAspas = !dentroAspas;
            } else if (c == ',' && !dentroAspas) {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        campos.add(atual.toString());

        this.show_id = getCampo(campos, 0);
        this.type = getCampo(campos, 1);
        this.title = getCampo(campos, 2);
        this.director = getCampo(campos, 3);

        String castStr = getCampo(campos, 4);
        if (castStr.isEmpty()) {
            this.cast = new String[0];
        } else {
            this.cast = castStr.split(", ");
            Arrays.sort(this.cast); // Ordena o elenco
        }
        this.country = getCampo(campos, 5);

        String dataStr = getCampo(campos, 6);
        if (!dataStr.isEmpty()) {
            String[] dataSplit = dataStr.replace("\"", "").split(" ");
            if (dataSplit.length == 3) {
                String mes = dataSplit[0];
                int dia = Integer.parseInt(dataSplit[1].replace(",", ""));
                int ano = Integer.parseInt(dataSplit[2]);
                this.date_added = new Date(mes, dia, ano);
            } else {
                this.date_added = new Date();
            }
        } else {
            this.date_added = new Date();
        }

        this.release_year = parseInt(getCampo(campos, 7));
        this.rating = getCampo(campos, 8);
        this.duration = getCampo(campos, 9);

        String listedInStr = getCampo(campos, 10);
        if (listedInStr.startsWith("\"") && listedInStr.endsWith("\"") && listedInStr.length() > 1) {
            listedInStr = listedInStr.substring(1, listedInStr.length() - 1);
        }
        this.listed_in = listedInStr.isEmpty() ? new String[0] : listedInStr.split(", ");
    }

    private String getCampo(ArrayList<String> campos, int index) {
        return index < campos.size() ? campos.get(index).trim() : "";
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public void imprimir() {
        System.out.print("=> ");

        System.out.print((show_id.isEmpty() ? "NaN" : show_id) + " ## ");
        System.out.print((title.isEmpty() ? "NaN" : title) + " ## ");
        System.out.print((type.isEmpty() ? "NaN" : type) + " ## ");
        System.out.print((director.isEmpty() ? "NaN" : director) + " ## ");

        if (cast.length == 0 || (cast.length == 1 && cast[0].trim().isEmpty())) {
            System.out.print("[NaN] ## ");
        } else {
            System.out.print("[");
            for (int i = 0; i < cast.length; i++) {
                System.out.print(cast[i]);
                if (i < cast.length - 1) System.out.print(", ");
            }
            System.out.print("] ## ");
        }

        System.out.print((country.isEmpty() ? "NaN" : country) + " ## ");

        if (date_added == null || date_added.getMes() == null || date_added.getMes().isEmpty()) {
            System.out.print("NaN ## ");
        } else {
            System.out.print(date_added.getMes() + " " + date_added.getDia() + ", " + date_added.getAno() + " ## ");
        }

        System.out.print((release_year == 0 ? "NaN" : release_year) + " ## ");
        System.out.print((rating.isEmpty() ? "NaN" : rating) + " ## ");
        System.out.print((duration.isEmpty() ? "NaN" : duration) + " ## ");

        if (listed_in.length == 0 || (listed_in.length == 1 && listed_in[0].trim().isEmpty())) {
            System.out.print("NaN");
        } else {
            System.out.print("[");
            for (int i = 0; i < listed_in.length; i++) {
                System.out.print(listed_in[i]);
                if (i < listed_in.length - 1) System.out.print(", ");
            }
            System.out.print("]");
        }

        System.out.println(" ##");
    }

    public String getShow_id() {
        return show_id;
    }
}

class Date {
    private String mes;
    private int dia;
    private int ano;

    public Date() {}

    public Date(String mes, int dia, int ano) {
        this.mes = mes;
        this.dia = dia;
        this.ano = ano;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Date clone() {
        return new Date(this.mes, this.dia, this.ano);
    }
}

public class Disneyplus {
    public static void main(String[] args) {
        File file=new File("/tmp/disneyplus.csv");
        ArrayList<Shows> shows = new ArrayList<>();

        try {
            Scanner sc = new Scanner(file);
            sc.nextLine(); // Pula o cabeçalho
            while (sc.hasNextLine()) {
                shows.add(new Shows(sc.nextLine()));
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (!input.equals("FIM")) {
            boolean encontrado = false;
            for (Shows show : shows) {
                if (show.getShow_id().equals(input)) {
                    show.imprimir();
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                System.out.println("Show ID não encontrado.");
            }
            input = sc.nextLine();
        }

        sc.close();
    }
}
