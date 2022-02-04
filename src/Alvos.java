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
        this.chegouDestino = false;
        this.atingido = false;
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

    public long getFreq() {
        return this.freqAtualizarPosicao;
    }

    public boolean getChegouDestino() {
        return this.chegouDestino;
    }

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public void setAtingido(boolean atingiu) {
        this.atingido = atingiu;
    }

    public void moveAlvo() {
        if (this.getLocalizacao().getY() > getPontoDestino().getY()) {
            this.chegouDestino = true;
        } else {
            this.getLocalizacao().setY(getLocalizacao().getY() + 2);
        }
    }

    public void run() {
        while (true) {
            moveAlvo();
            if (chegouDestino || atingido) {
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
