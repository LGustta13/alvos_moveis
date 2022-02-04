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
    private int angle = 170;// new Random().nextInt(180);

    public Tiros() {
        Tiros.totalTiros++;
        this.id = Tiros.totalTiros;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = new Pontos(300, 300);
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

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public void moveTiro() {

        double dx = Math.cos(Math.toRadians(this.angle));
        double dy = Math.sin(Math.toRadians(this.angle));

        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            getLocalizacao().setX(300);
            getLocalizacao().setY(300);

        } else {
            getLocalizacao().setX((int) (getLocalizacao().getX() - dx * 2));
            getLocalizacao().setY((int) (getLocalizacao().getY() - dy * 2));
        }
    }

    public void run() {

    }

}
