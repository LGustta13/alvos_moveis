import java.util.Random;

public class Alvos extends Thread {

    private static long totalAlvos = 0;
    private long id;
    private Pontos pontoOrigem;
    private Pontos pontoDestino;
    private Pontos localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao;
    private boolean chegouDestino, atingido, errou;

    private int[] velocidades;

    public Alvos(Pontos pontoOrigem, Pontos pontoDestino) {
        Alvos.totalAlvos++;
        this.id = Alvos.totalAlvos;
        this.timestamp = System.currentTimeMillis();
        this.pontoOrigem = pontoOrigem;
        this.pontoDestino = pontoDestino;
        this.localizacaoAtualizada = pontoOrigem;
        this.freqAtualizarPosicao = 30;
        this.velocidades = new int[(int)pontoDestino.getY()/2];
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

    public void velAleatoria() {
        Random gerador = new Random();

        // O VETOR DE VELOCIDADES É CRIADO
        // O ALVO AVANÇA 2 PIXELS A CADA 30MS, TOTALIZANDO 9000MS
        //      ATÉ CHEGAR NO SEU DESTINO (600PX * 30MS/ 2PX)
        for(int i = 0; i<getPontoDestino().getY()/2; i++){
            velocidades[(int)(gerador.nextDouble()*(getPontoDestino().getY())/2)] +=2;
        }
    }

    public void moveAlvo(int velocidade) {
        if (this.getLocalizacao().getY() >= getPontoDestino().getY()) {
            this.chegouDestino = true;
        } else {
            this.getLocalizacao().setY(getLocalizacao().getY() + velocidade);
        }
    }

    public void run() {

        // CRIA O VETOR DE VELOCIDADES PARA CADA ATUALIZAÇÃO DO ALVO
        velAleatoria();
        int i = 0;
        while (true) {
            try {
                sleep(getFreq());
                if(i!=getPontoDestino().getY()/2){

                    // ATRIBUI APÓS CADA ATUALIZAÇÃO UM NOVO VALOR DE VELOCIDADE
                    moveAlvo(velocidades[i]);
                } else {
                    moveAlvo(1);
                }
                if (chegouDestino || atingido) {
                    this.interrupt();
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
