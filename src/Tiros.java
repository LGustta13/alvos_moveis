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
    private boolean contatoAlvo = false;
    private int angulo = 55;

    // double id, Pontos origemAlvo, Pontos destinoAlvo, long timeAlvo
    public Tiros() {
        Tiros.totalTiros++;
        this.id = Tiros.totalTiros;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = new Pontos(305, 530);
        this.localizacaoAtualizada = pontoOrigem;
        // calcularAngulo(id, origemAlvo, destinoAlvo, timeAlvo);
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

    // public void calcularAngulo(double id, Pontos origemAlvo, Pontos destinoAlvo,
    // long timeAlvo) {

    // // private int angle = 170;// new Random().nextInt(180);

    // this.angulo = angulo;
    // }

    public void moveTiro() {

        double dx = Math.cos(Math.toRadians(this.angulo));
        double dy = Math.sin(Math.toRadians(this.angulo));

        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            getLocalizacao().setX(305);
            getLocalizacao().setY(530);

        } else {
            getLocalizacao().setX((int) (getLocalizacao().getX() - dx * 2));
            getLocalizacao().setY((int) (getLocalizacao().getY() - dy * 2));
        }
    }

    public void run() {
        while (true) {
            moveTiro();
            System.out.println(getLocalizacao().getX());
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
