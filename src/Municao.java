public class Municao {

    private static int quantidade = 0;
    private int id;

    public Municao() {
        Municao.quantidade++;
        setId(quantidade);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
