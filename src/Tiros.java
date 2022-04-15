public class Tiros extends Thread {

    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long freqAtualizarPosicao = 30;
    private boolean contatoAlvo, contatoJanela;
    private double sen, cos;

    public Tiros() {
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

    public boolean getContatoJanela() {
        return this.contatoJanela;
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

    public void setContatoJanela(boolean contato) {
        this.contatoJanela = contato;
    }

    public void setPontoDestino(Pontos pontoDestino) {
        this.pontoDestino = pontoDestino;
    }

    public void calcularCatetos() {
        double adj = getPontoOrigem().getX() - pontoDestino.getX();
        double opo = getPontoOrigem().getY() - pontoDestino.getY();
        double hip = Math.sqrt((Math.pow(adj, 2) + Math.pow(opo, 2)));

        this.cos = adj / hip;
        this.sen = opo / hip;

    }

    public void mover(double cos, double sen, int att) {

        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            setContatoJanela(true);

        } else {
            getLocalizacao().setX(getLocalizacao().getX() - cos * att);
            getLocalizacao().setY(getLocalizacao().getY() - sen * att);

        }
    }

    public void run() {

        calcularCatetos();
        while (true) {
            try {
                sleep(getFreq());
                mover(cos, sen, 3);

                if (getContatoAlvo() || getContatoJanela()) {
                    this.interrupt();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
