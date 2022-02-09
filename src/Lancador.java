import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Lancador extends Thread {

    private Pontos posicao;
    private Tiros tiro = new Tiros();;
    private Stack<Municao> carregador;
    private Semaphore semaforo;

    public Lancador(Pontos posicao) {
        this.posicao = posicao;
        this.semaforo = new Semaphore(1);
        this.carregador.add(new Municao());
        this.carregador.add(new Municao());
        this.carregador.add(new Municao());
        start();
    }

    public Pontos getPosicao() {
        return this.posicao;
    }

    public Tiros getTiro() {
        return this.tiro;
    }

    public void carregar() {
        if (carregador.empty()) {

        }

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
            semaforo.acquire();
            carregar();
            preparar();
            atirar();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            semaforo.release();
        }

        while (true) {
            if (this.tiro.getContatoAlvo() == true) {
                tiro = new Tiros();
                tiro.start();
                System.out.println(1);
            }

        }

    }

}
