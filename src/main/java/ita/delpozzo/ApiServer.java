package ita.delpozzo;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Optional;

public class ApiServer {

    public static void main(String[] args) {


        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));

        Javalin app = Javalin.create(config -> {
            // Configurazione CORS per Javalin 5.x / 6.x
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

            // Il tuo Request Logger
            config.requestLogger.http((ctx, ms) -> {
                System.out.println(ctx.method() + " " + ctx.path() + " - " + ms + "ms");
            });
        }).start(port);

        System.out.println("🚀 Server avviato: http://localhost:7070");

        // Creiamo l'oggetto per il Database
        DB_CRUD db_studenti  = new DB_CRUD();// ToDo
        ArrayList<Studente> studentiProva = new ArrayList<>();
        studentiProva = db_studenti.leggiStudenti(Optional.empty());
        for (Studente studente : studentiProva) {
            System.out.println(studente);
        }


        // Leggi tutti gli studenti
        app.get("/studenti/read", ctx -> {
            ArrayList<Studente> studenti = DB_CRUD.leggiStudenti(Optional.empty());
            ctx.json(studenti).status(200);



        });

        // Leggi studente per ID
        app.get("/studenti/read/{id}", ctx -> {
            try {
                int idValue = Integer.parseInt(ctx.pathParam("id"));
                Optional<Integer> id = Optional.of(idValue);
                ArrayList<Studente> studenti = DB_CRUD.leggiStudenti(id);

                if (studenti.isEmpty()) {
                    ctx.status(404).result("Studente non trovato");
                } else {
                    ctx.json(studenti).status(200);
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("ID non valido");
            }
        });

        // Elimina studente per ID
        app.delete("/studenti/delete/{id}", ctx -> {
            try {
                // ToDo
                int idValue = Integer.parseInt(ctx.pathParam("id"));
                Optional<Integer> id = Optional.of(idValue);
                ArrayList<Studente> studenti = DB_CRUD.leggiStudenti(id);

                if (studenti.isEmpty()) {
                    ctx.status(404).result("Studente non trovato");
                } else {
                    if (DB_CRUD.cancellaStudente(idValue)) {
                        System.out.println("Studente cancellato");
                        ctx.status(200).result("Studente con ID " + idValue + " eliminato correttamente");
                    }
                    else{
                        System.out.println("Errore eliminazione");
                        ctx.status(500).result("Errore durante l'eliminazione dello studente");
                    }
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("ID non valido");
            }
        });

        // Aggiungi studente
        app.post("/studenti/create", ctx -> {
            try {
                // Legge i parametri dal body (JSON) come chiavi "nome" e "voto"
                String nome = ctx.formParam("nome");
                String votoStr = ctx.formParam("voto");

                if (nome == null || votoStr == null) {
                    ctx.status(400).result("Nome o voto mancanti");
                    return;
                }

                int voto;
                try {
                    voto = Integer.parseInt(votoStr);
                } catch (NumberFormatException e) {
                    ctx.status(400).result("Voto non valido");
                    return;
                }

                // Chiama il metodo DB_CRUD
                DB_CRUD.aggiungiStudente(nome, voto);

                ctx.status(201).result("Studente aggiunto con successo");

            } catch (Exception e) {
                ctx.status(500).result("Errore durante l'aggiunta dello studente");
                e.printStackTrace();
            }
        });

        // Modifica voto studente
        app.put("/studenti/update/{id}", ctx -> {
            try {
                // Legge l'ID dalla path
                int id;
                try {
                    id = Integer.parseInt(ctx.pathParam("id"));
                } catch (NumberFormatException e) {
                    ctx.status(400).result("ID non valido");
                    return;
                }

                // Legge il nuovo voto dal body della richiesta (form param o JSON)
                String votoStr = ctx.formParam("voto");
                if (votoStr == null) {
                    ctx.status(400).result("Nuovo voto mancante");
                    return;
                }

                int nuovoVoto;
                try {
                    nuovoVoto = Integer.parseInt(votoStr);
                } catch (NumberFormatException e) {
                    ctx.status(400).result("Voto non valido");
                    return;
                }

                // Aggiorna il voto usando DB_CRUD
                DB_CRUD.aggiornaVoto(id, nuovoVoto);

                ctx.status(200).result("Voto aggiornato con successo");

            } catch (Exception e) {
                ctx.status(500).result("Errore durante l'aggiornamento del voto");
                e.printStackTrace();
            }
        });


    }

}
