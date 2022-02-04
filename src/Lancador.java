public class Lancador extends Thread {

    private Pontos posicao;
    private Tiros tiro = new Tiros();

    public Lancador(Pontos posicao) {
        this.posicao = posicao;
        start();
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public Tiros getTiro() {
        return this.tiro;
    }

    public void run() {
        tiro.start();
    }

}
