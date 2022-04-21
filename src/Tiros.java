public class Tiros extends Thread {

    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long freqAtualizarPosicao = 30;
    private boolean contatoAlvo, contatoJanela;
    private double sen, cos, hip, velocidade, Tn, tempo, percurso, flag;
    private int N = 50;

    private Pontos posicaoAlvo;

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

    public void setPosicaoAlvo(Pontos posicaoAlvo){
        this.posicaoAlvo = posicaoAlvo;
    }

    public Pontos getPosicaoAlvo(){
        return this.posicaoAlvo;
    }

    public void calculaCatetos() {
        double adj = getPontoOrigem().getX() - pontoDestino.getX();
        double opo = getPontoOrigem().getY() - pontoDestino.getY();
        this.hip = Math.sqrt((Math.pow(adj, 2) + Math.pow(opo, 2)));

        this.cos = adj / this.hip;
        this.sen = opo / this.hip;

    }

    public void calculaParametros(){
        this.tempo = (this.getPontoDestino().getY() - this.getPosicaoAlvo().getY()) * (getFreq()/2);  // MILISSEGUNDOS
        this.Tn = this.tempo/this.N;      // Tn É O 0,75
        this.percurso = this.hip/this.N;  // É A QUANTIDADE DE PIXELS A CADA PONTO DA TRAJETÓRIA
        this.flag = this.percurso;
    };

    public double calculaVelocidade(){

        double[] y = new double[this.N + 1]; // TEMPOS - A PARTIR DAQUI SURGEM AS VELOCIDADES
        double[] v = new double[this.N + 1]; // TOLERÂNCIA
        double[] A = new double[this.N + 1]; // RESTRIÇÕES

        if(this.flag >= this.percurso && y.length != 1){
            //System.out.println("N: " + this.N);
//        System.out.println("hip: "+this.hip);
            //System.out.println("Tn: "+ this.Tn);
            // System.out.println("percurso: "+ percurso);
//        System.out.println("velocidade: " + velocidade);
//        System.out.println("tempo: "+this.tempo);

            for(int i = 0; i<this.N + 1; i++){
                if(i != 0) {
                    y[i] = Tn;
                    A[i] = -1;
                }
                else {
                    y[i] = this.tempo;
                    A[i] = 1;
                }
                v[i] = 0.2;

//                System.out.println(y[i]);
//                System.out.println(v[i]);
//                System.out.println(A[i]);
            }
            // System.out.println("****************");

            Reconciliation rec = new Reconciliation(y, v, A);
            //System.out.println(y[1]);
            //System.out.println(rec.getReconciledFlow()[1]);
            //rec.printMatrix(rec.getReconciledFlow());

            this.N --;
            this.tempo -= this.Tn;
            this.flag = this.velocidade;
//            System.out.println(this.percurso);
//            System.out.println(rec.getReconciledFlow()[1]);

            this.velocidade = this.percurso*((double)getFreq())/(rec.getReconciledFlow()[1]);
            System.out.println(this.velocidade);
            return this.velocidade;

        } else {
            this.flag += this.velocidade;
            return this.velocidade;
        }
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
