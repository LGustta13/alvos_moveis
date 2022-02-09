public class Municao {

    private static int quantidade = 0;
    private int id;
    private boolean utilizada = false;

    public Municao() {
        this.quantidade++;
        this.id = this.quantidade;
    }
}
