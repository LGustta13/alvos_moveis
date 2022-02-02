public class Lancador extends Thread {

    private Pontos posicao;
    private float atuacao = 180;
    private Tiros tiro;

    public Lancador() {
        start();
    }

    public Lancador(Pontos posicao) {
        this.posicao = posicao;
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public void run() {

    }

}
