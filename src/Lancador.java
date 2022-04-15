import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Lancador extends Thread {

    private Pontos posicao;
    private Tiros tiro;
    private Stack<Municao> carregador = new Stack<Municao>();
    private Alvos alvo;
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

    public Alvos getAlvo() {
        return this.alvo;
    }

    public void setAlvo(Alvos alvo) {
        this.alvo = alvo;
    }

    public void setCarregador() {
        carregador.pop();
    }

    public void calcularDestino(Pontos origemAlvo, Pontos destinoAlvo, long timeAlvo) {

        long tempo = System.currentTimeMillis() - timeAlvo;
        int pixels = (int) (tempo / 30) + 10;
        int ponto = (int) ((600 - pixels) / 2) + pixels;
        tiro.setPontoDestino(new Pontos(origemAlvo.getX(), ponto));
        System.out.println(this.getAlvo().getId());

        System.out.println(tiro.getPontoDestino().getX() + "---" + tiro.getPontoDestino().getY());
    }

    public void carregar() {

        tiro = new Tiros();
        carregador.pop();
        if (carregador.empty()) {
            System.out.println("Carregador vazio");
        }

    }

    public void preparar() {
        try {
            this.tiro = new Tiros();
            calcularDestino(this.alvo.getPontoOrigem(), this.alvo.getPontoDestino(), this.alvo.getTimestamp());
            sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void atirar() {
        tiro.start();

    }

    public void run() {

        while (!carregador.empty()) {
            if (this.alvo != null) {
                try {
                    semaforo.acquire();
                    carregar();
                    preparar();
                    atirar();

                    while (tiro.isAlive()) {
                        if (tiro.isInterrupted()) {
                            if (tiro.getContatoAlvo()) {
                                carregador.add(new Municao());
                            } else if (this.getAlvo() != null) {
                                this.alvo.setErrou(true);
                                carregador.pop();
                            }
                            this.setAlvo(null);
                            semaforo.release();
                        }
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Jogo finalizado");
    }

}
