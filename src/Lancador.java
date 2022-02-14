import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Lancador extends Thread {

    private Pontos posicao;
    private Tiros tiro;
    private Stack<Municao> carregador;
    private Alvos alvo;
    private Semaphore semaforo;

    public Lancador(Pontos posicao, Alvos alvo) {
        this.posicao = posicao;
        this.alvo = alvo;
        this.tiro = new Tiros();
        this.semaforo = new Semaphore(1);
        // this.carregador.add(new Municao());
        // this.carregador.add(new Municao());
        // this.carregador.add(new Municao());
        start();
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public Tiros getTiro() {
        return this.tiro;
    }

    public Alvos getAlvo() {
        return this.alvo;
    }

    public void setAlvo(Alvos alvo) {
        this.alvo = alvo;
    }

    public void carregar() {

        tiro.calcularDestino(this.alvo.getPontoOrigem(), this.alvo.getPontoDestino(), this.alvo.getTimestamp());

        // if (carregador.empty()) {
        // System.out.println("Carregador vazio");
        // }

    }

    public void preparar() {
        try {
            sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void atirar() {
        tiro.start();
    }

    public void run() {

        try {
            semaforo.acquire(1);
            carregar();
            preparar();
            atirar();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            semaforo.release();
        }

        // while (true) {
        // if (this.tiro.getContatoAlvo() == true) {
        // tiro = new Tiros();
        // tiro.start();
        // }

        // }

    }

}
