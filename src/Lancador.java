public class Lancador extends Thread {

    private Pontos posicao;
    private float atuacao = 180;
    private Tiros tiro;

    public Lancador(Pontos posicao) {
        this.posicao = posicao;
        this.tiro = new Tiros();
        start();
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public Tiros getTiro() {
        return this.tiro;
    }

    public void run() {
        while (true) {
            this.tiro.moveTiro();
            try {
                sleep(this.tiro.getFreq());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
