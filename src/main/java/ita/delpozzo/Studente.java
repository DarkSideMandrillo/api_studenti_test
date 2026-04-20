package ita.delpozzo;

public class Studente {
    private int id;
    private String nome;
    private int voto;

    // Utilizzato per caricare
    public Studente(String nome, int voto) {
        this.nome = nome;
        this.voto = voto;
    }

    // Usato per leggere
    public Studente(int id,String nome, int voto) {
        this.id = id;
        this.nome = nome;
        this.voto = voto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Studente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", voto=" + voto +
                '}';
    }
}
