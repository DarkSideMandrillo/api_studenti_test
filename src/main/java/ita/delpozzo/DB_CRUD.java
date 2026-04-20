package ita.delpozzo;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class DB_CRUD {

    private static String getDatabaseUrl() {
        /* * Cerca il file "studenti.db" alla radice del classpath
         * (che corrisponde a 'src/main/resources')
         */
        URL resourceUrl = DB_CRUD.class.getResource("/studenti.db");

        if (resourceUrl == null) {
            // Se non trova il file, lancia un errore chiaro
            throw new RuntimeException("Errore: file 'studenti.db' non trovato!");
        }

        // Costruisce la stringa JDBC con il percorso assoluto trovato
        return "jdbc:sqlite:" + resourceUrl.getPath();
    }

    // 3. USA IL METODO PER IMPOSTARE L'URL CORRETTO
    static String url = getDatabaseUrl();

    // READ: legge tutti gli studenti
    public static ArrayList<Studente> leggiStudenti(Optional<Integer> idParam) {
        ArrayList<Studente> studenti = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url)) {

            String query;
            PreparedStatement ps;

            if (idParam.isPresent()) {
                // Se c'è l'ID, uso PreparedStatement con parametro
                query = "SELECT * FROM studenti WHERE id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, idParam.get());
            } else {
                // Se non c'è l'ID, leggo tutti gli studenti
                query = "SELECT * FROM studenti";
                ps = conn.prepareStatement(query); // PreparedStatement va bene anche senza parametri
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int voto = rs.getInt("voto");

                studenti.add(new Studente(id, nome, voto));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studenti;
    }


    // CREATE: aggiunge uno studente
    public static void aggiungiStudente(String nome, int voto)  {
        String query = "INSERT INTO studenti (nome, voto) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nome);
            stmt.setInt(2, voto);

            int righeModificate = stmt.executeUpdate();

            if (righeModificate > 0) {
                System.out.println("Studente aggiunto con successo");
            } else {
                System.out.println("Studente non aggiunto con successo");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE: modifica il voto di uno studente
    public static void aggiornaVoto(int id, int nuovoVoto) {
        String query = "UPDATE studenti SET voto = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, nuovoVoto);
            stmt.setInt(2, id);

            int righeModificate = stmt.executeUpdate();

            if (righeModificate > 0) {
                System.out.println("✓ Voto aggiornato per ID " + id + " -> " + nuovoVoto);
            } else {
                System.out.println("✗ Studente con ID " + id + " non trovato");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE: cancella uno studente
    public static boolean cancellaStudente(int id) {
        String query = "DELETE FROM studenti WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int righeEliminate = stmt.executeUpdate();

            if (righeEliminate > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

