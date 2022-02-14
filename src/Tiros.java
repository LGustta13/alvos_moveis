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

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public void setContatoAlvo(boolean contato) {
        this.contatoAlvo = contato;
    }

    public void calcularDestino(Pontos origemAlvo, Pontos destinoAlvo, long timeAlvo) {

        long tempo = System.currentTimeMillis();
        tempo = tempo - timeAlvo;
        int pixels = (int) (tempo / 30) + 10;
        int ponto = (int) ((600 - pixels) / 2) + pixels;
        this.pontoDestino = new Pontos(origemAlvo.getX(), ponto);

    }

    public void calcularCatetos() {

        double adj = pontoOrigem.getX() - pontoDestino.getX();
        double opo = pontoOrigem.getY() - pontoDestino.getY();
        double hip = (int) Math.sqrt((Math.pow(adj, 2) + Math.pow(opo, 2)));

        cateto1 = adj / hip;
        cateto2 = opo / hip;
    }

    public void mover(double adj, double opo) {
        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            getLocalizacao().setX(305);
            getLocalizacao().setY(530);

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
            mover(cateto1, cateto2);
            if (contatoAlvo) {
                break;
            }

            try {
                sleep(getFreq());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
