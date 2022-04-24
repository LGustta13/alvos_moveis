import java.io.IOException;

public class Tiros extends Thread {

    private static int id = 0;

    private Excell dados;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long freqAtualizarPosicao = 30;
    private long timestamp;
    private boolean contatoAlvo, contatoJanela;
    private double sen, cos, hip, velocidade, Tn, tempo, percurso, flag, posicaoInicialAlvo;
    private int N = 15;
    private Alvos alvo;

    public Tiros() {
        this.id ++;
        this.pontoOrigem = new Pontos(305, 530);
        this.localizacaoAtualizada = pontoOrigem;;
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

    public void setFreq(long freqAtualizarPosicao) {
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
    public void setAlvo(Alvos alvo){
        this.alvo = alvo;
    }
    public Alvos getAlvo(){
        return this.alvo;
    }

    public void calculaCatetos() {
        double adj = getPontoOrigem().getX() - pontoDestino.getX();
        double opo = getPontoOrigem().getY() - pontoDestino.getY();
        this.hip = Math.sqrt((Math.pow(adj, 2) + Math.pow(opo, 2)));

        this.cos = adj / this.hip;
        this.sen = opo / this.hip;
    }
    public void calculaParametros() throws IOException {
        this.posicaoInicialAlvo = alvo.getLocalizacao().getY();
        this.timestamp = System.currentTimeMillis();
        this.tempo = (this.getPontoDestino().getY() - posicaoInicialAlvo) * (getFreq()/2);  // MILISSEGUNDOS
        this.percurso = this.hip/this.N;  // É A QUANTIDADE DE PIXELS A CADA PONTO DA TRAJETÓRIA

        this.flag = this.percurso;
        dados = new Excell(this.id);
    }

    public double calculaVelocidade() throws IOException {

        double[] y = new double[this.N+1]; // TEMPOS - A PARTIR DAQUI SURGEM AS VELOCIDADES
        double[] v = new double[this.N+1]; // TOLERÂNCIA
        double[] A = new double[this.N+1]; // RESTRIÇÕES
        this.Tn = this.tempo/this.N;      // Tn É O 0,75

        if(this.flag >= this.percurso && y.length != 1){

            double pixelsTotal = (double)2*((System.currentTimeMillis() - this.timestamp)/ getFreq());
            double pixelsReal = alvo.getLocalizacao().getY() - this.posicaoInicialAlvo;
            double variacao = 15*(pixelsTotal-pixelsReal);

            for(int i = 0; i<this.N+1; i++){
                if(i != 0) {
                    y[i] = Tn;
                    A[i] = -1;
                }
                else {
                    y[i] = this.tempo;
                    A[i] = 1;
                }
                v[i] = 0.01;
            }

            Reconciliation rec = new Reconciliation(y, v, A);

            this.N --;
            this.tempo -= (y[1] + variacao);

            this.velocidade = this.percurso*((double)getFreq())/(rec.getReconciledFlow()[1]);
            this.flag = this.velocidade;
            this.posicaoInicialAlvo = alvo.getLocalizacao().getY();
            this.timestamp = System.currentTimeMillis();

            dados.escrever(v, A, y, rec.getReconciledFlow(), this.velocidade, variacao);
        } else {
            this.flag += this.velocidade;
        }

        return this.velocidade;
    }

    public void mover(double cos, double sen, double att) {

        if (getLocalizacao().getY() < 0 || getLocalizacao().getX() < 0 || getLocalizacao().getX() > 600) {
            setContatoJanela(true);

        } else {
            getLocalizacao().setX(getLocalizacao().getX() - cos * att);
            getLocalizacao().setY(getLocalizacao().getY() - sen * att);

        }
    }

    public void run() {
        try {
            calculaCatetos();
            calculaParametros();
            while (true) {
                try {
                    sleep(getFreq());
                    mover(cos, sen, calculaVelocidade()); // NÚMERO DE PIXELS PERCORRIDOS

                    if (getContatoAlvo() || getContatoJanela()) {
                        this.interrupt();
                        break;
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            dados.fechar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
