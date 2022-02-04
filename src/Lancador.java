public class Lancador extends Thread {

    private Pontos posicao;

    public Lancador(Pontos posicao) {
        this.posicao = posicao;
        start();
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public void run() {
        while (true) {

        }
    }

}
