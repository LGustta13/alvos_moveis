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

    public void retirarMunicao() {
        carregador.pop();
    }

    public int statusCarregador() {
        return carregador.size();
    }

    public void calcularDestino(Pontos origemAlvo, long timeAlvo) {

        long tempo = System.currentTimeMillis() - timeAlvo;
        double pixels = 0.07 * tempo;

        if (tempo <= 3500) {
            tiro.setPontoDestino(new Pontos(origemAlvo.getX(), pixels + 150));
        } else if (tempo > 3500 && tempo <= 4500) {
            tiro.setPontoDestino(new Pontos(origemAlvo.getX(), pixels + 100));
        } else if (tempo > 4500 && tempo <= 6000) {
            tiro.setPontoDestino(new Pontos(origemAlvo.getX(), pixels + 70));
        } else if (tempo > 6000 && tempo <= 7000) {
            tiro.setPontoDestino(new Pontos(origemAlvo.getX(), pixels + 50));
        } else {
            tiro.setPontoDestino(new Pontos(origemAlvo.getX(), 530));
        }
    }

    public void carregar() {

        tiro = new Tiros(5); // VELOCIDADE DO TIRO
        carregador.pop();
    }

    public void preparar() {
        try {
            calcularDestino(this.alvo.getPontoOrigem(), this.alvo.getTimestamp());
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
                                if (carregador.size() < 5) {
                                    carregador.add(new Municao());
                                }
                            } else if (this.getAlvo() != null) {
                                this.alvo.setErrou(true);
                            }
                            this.setAlvo(null);
                            semaforo.release();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
