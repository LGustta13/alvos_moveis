public class Alvos extends Thread {

    private static long totalAlvos = 0;
    private long id;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao = 30;
    private boolean chegouDestino;
    private boolean atingido;

    public Alvos(Pontos pontoOrigem, Pontos pontoDestino) {
        Alvos.totalAlvos++;
        this.id = Alvos.totalAlvos;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = pontoOrigem;
        this.pontoDestino = pontoDestino;
        this.localizacaoAtualizada = pontoOrigem;
        start();
    }

    public long getId() {
        return this.id;
    }

    public Pontos getPontoOrigem() {
        return this.pontoOrigem;
    }

    public Pontos getPontoDestino() {
        return this.pontoDestino;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public Pontos getLocalizacao() {
        return this.localizacaoAtualizada;
    }

    public void setLocalizacao(int x, int y) {
        this.localizacaoAtualizada.setX(x);
        this.localizacaoAtualizada.setY(y);
    }

    public long getFreq() {
        return this.freqAtualizarPosicao;
    }

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public void moveAlvo() {
        if (this.getLocalizacao().getY() > getPontoDestino().getY()) {
            this.getLocalizacao().setY(0);
        } else {
            this.getLocalizacao().setY(getLocalizacao().getY() + 2);
        }
    }

    public void run() {
        while (true) {
            moveAlvo();
            try {
                sleep(getFreq());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
