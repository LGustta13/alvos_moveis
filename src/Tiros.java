public class Tiros extends Thread {

    private long id;
    private int municao;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao = 30;
    private boolean contatoAlvo;

    public Tiros() {

    }

    public Pontos getLocalizacao() {
        return this.localizacaoAtualizada;
    }

    public void run() {

    }

}
