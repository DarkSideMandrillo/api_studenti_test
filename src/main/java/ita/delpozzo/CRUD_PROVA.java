package ita.delpozzo;

import java.sql.*;
import java.util.ArrayList;

public class CRUD_PROVA {
    static String url = "jdbc:sqlite:Resource/studenti.db";


    // READ: legge tutti gli studenti
    public static ArrayList<Studente> leggiStudenti() {

        ArrayList<Studente> studenti = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM studenti";
            ResultSet rs = stmt.executeQuery(query);


            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int voto = rs.getInt("voto");

                studenti.add(new Studente(id, nome, voto));
            }
            return studenti;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ by ID: legge studente per ID
    public static Studente leggiStudente(int id) {

        String query = "SELECT * FROM studenti WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            String nome = rs.getString("nome");
            int voto = rs.getInt("voto");

            return new Studente(id, nome, voto);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public static void cancellaStudente(int id) {
        String query = "DELETE FROM studenti WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int righeEliminate = stmt.executeUpdate();

            if (righeEliminate > 0) {
                System.out.println("✓ Studente con ID " + id + " eliminato");
            } else {
                System.out.println("✗ Studente con ID " + id + " non trovato");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}