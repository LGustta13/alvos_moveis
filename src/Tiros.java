import java.util.Random;

public class Tiros extends Thread {

    private static long totalTiros = 0;
    private long id;
    private int municao;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao = 30;
    private boolean contatoAlvo;
    private double cateto1, cateto2;

    // double id, Pontos origemAlvo, Pontos destinoAlvo, long timeAlvo
    public Tiros() {
        Tiros.totalTiros++;
        this.id = Tiros.totalTiros;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = new Pontos(305, 530);
        this.localizacaoAtualizada = pontoOrigem;
    }

    public Pontos getLocalizacao() {
        return this.localizacaoAtualizada;
    }

    public Pontos getPontoDestino() {
        return this.pontoDestino;
    }

    public long getFreq() {
        return this.freqAtualizarPosicao;
    }

    public boolean getContatoAlvo() {
        return this.contatoAlvo;
    }

    public Pontos getPontoOrigem() {
        return this.pontoOrigem;
    }

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public void setContatoAlvo(boolean contato) {
        this.contatoAlvo = contato;
    }

    public void setPontoDestino(Pontos pontoDestino) {
        this.pontoDestino = pontoDestino;
    }

    public void calcularCatetos() {
        double adj = getPontoOrigem().getX() - pontoDestino.getX();
        double opo = getPontoOrigem().getY() - pontoDestino.getY();
        double hip = (int) Math.sqrt((Math.pow(adj, 2) + Math.pow(opo, 2)));

        this.cateto1 = adj / hip;
        this.cateto2 = opo / hip;
    }

    public void mover(double adj, double opo) {

        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            getLocalizacao().setX(305);
            getLocalizacao().setY(530);
            setContatoAlvo(true);

        } else {
            getLocalizacao().setX((int) (getLocalizacao().getX() - adj * 2));
            getLocalizacao().setY((int) (getLocalizacao().getY() - opo * 2));
        }
    }

    // public void moveTiro1() {

    // double dx = Math.cos(Math.toRadians(this.angulo));
    // double dy = Math.sin(Math.toRadians(this.angulo));

    // }

    public void run() {
        calcularCatetos();
        while (true) {
            try {
                sleep(getFreq());
                mover(cateto1, cateto2);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
