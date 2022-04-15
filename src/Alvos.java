public class Alvos extends Thread {

    private static long totalAlvos = 0;
    private long id;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao;
    private boolean chegouDestino, atingido, errou;

    public Alvos(Pontos pontoOrigem, Pontos pontoDestino) {
        Alvos.totalAlvos++;
        this.id = Alvos.totalAlvos;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = pontoOrigem;
        this.pontoDestino = pontoDestino;
        this.localizacaoAtualizada = pontoOrigem;
        this.freqAtualizarPosicao = 30;
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

    public void setFreq(int freqAtualizarPosicao) {
        this.freqAtualizarPosicao = freqAtualizarPosicao;
    }

    public boolean getChegouDestino() {
        return this.chegouDestino;
    }

    public boolean getAtingido() {
        return this.atingido;
    }

    public void setAtingido(boolean atingiu) {
        this.atingido = atingiu;
    }

    public boolean getErrou() {
        return this.errou;
    }

    public void setErrou(boolean errou) {
        this.errou = errou;
    }

    public void moveAlvo() {
        if (this.getLocalizacao().getY() >= getPontoDestino().getY()) {
            this.chegouDestino = true;
        } else {
            this.getLocalizacao().setY(getLocalizacao().getY() + 2);
        }
    }

    public void run() {

        while (true) {
            try {
                sleep(getFreq());
                moveAlvo();
                if (chegouDestino || atingido) {
                    this.interrupt();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
