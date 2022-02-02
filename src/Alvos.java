import java.security.Timestamp;

public class Alvos extends Thread {

    private static long totalAlvos = 0;
    private long id;
    private int[] pontoOrigem;
    private int[] pontoDestino;
    private int[] localizacaoAtualizada;
    private long timestamp;
    private long freqAtualizarPosicao;
    private boolean chegouDestino;
    private boolean atingido;

    public Alvos() {
        Alvos.totalAlvos++;
        this.id = Alvos.totalAlvos;
        this.timestamp = System.currentTimeMillis();
        start();
    }

    public long getId() {
        return this.id;
    }

    public int[] getPontoOrigem() {
        return this.pontoOrigem;
    }

    public int[] getPontoDestino() {
        return this.pontoDestino;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void run() {
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
