import java.io.*;
import java.net.*;
import java.util.*;

public class MultiplayerServer {
    private static final int PORT = 12345;
    private List<ClientHandler> clients = new ArrayList<>();
    private boolean batalhaIniciada = false;

    public static void main(String[] args) {
        new MultiplayerServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();

                // Verificar se há dois jogadores para iniciar a batalha
                if (clients.size() == 2 && !batalhaIniciada) {
                    iniciarBatalha();
                    batalhaIniciada = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }

    private void iniciarBatalha() {
        System.out.println("Iniciando batalha multiplayer!");

        // Enviar informações do oponente para cada jogador
        ClientHandler jogador1 = clients.get(0);
        ClientHandler jogador2 = clients.get(1);

        jogador1.setOponente(jogador2);
        jogador2.setOponente(jogador1);

        enviarListaMonstros(jogador1);
        enviarListaMonstros(jogador2);

        jogador1.enviarMensagem("OPONENTE_MONSTRO " + jogador2.getMonstroAtual().getNome() + " " +
                jogador2.getMonstroAtual().getVida() + " " + jogador2.getMonstroAtual().getVida());
        jogador2.enviarMensagem("OPONENTE_MONSTRO " + jogador1.getMonstroAtual().getNome() + " " +
                jogador1.getMonstroAtual().getVida() + " " + jogador1.getMonstroAtual().getVida());

        // Iniciar turnos
        jogador1.enviarMensagem("SEU_TURNO");
        jogador2.enviarMensagem("AGUARDANDO");
    }

    public void broadcast(String mensagem, ClientHandler remetente) {
        for (ClientHandler client : clients) {
            if (client != remetente) {
                client.enviarMensagem(mensagem);
            }
        }
    }

    public synchronized void processarAtaque(ClientHandler atacante, String nomeAtaque) {
        ClientHandler defensor = atacante.getOponente();
        if (defensor == null || !defensor.isConectado()) {
            atacante.enviarMensagem("FIM_BATALHA DERROTA"); // Oponente desconectou
            encerrarBatalha();
            return;
        }

        // Simular o ataque
        Mons monstroAtacante = atacante.getMonstroAtual();
        Mons monstroDefensor = defensor.getMonstroAtual();

        

        // Enviar atualizações de vida para ambos os jogadores
        atacante.enviarMensagem("ATUALIZAR_VIDA " + monstroAtacante.getVida() + " " + monstroDefensor.getVida());
        defensor.enviarMensagem("ATUALIZAR_VIDA " + monstroAtacante.getVida() + " " + monstroDefensor.getVida());

        // Verificar se o defensor foi derrotado
        if (!monstroDefensor.estaVivo()) {
            atacante.enviarMensagem("FIM_BATALHA VITORIA");
            defensor.enviarMensagem("FIM_BATALHA DERROTA");
            encerrarBatalha();
            return;
        }

        // Alternar turnos
        atacante.enviarMensagem("AGUARDANDO");
        defensor.enviarMensagem("SEU_TURNO");
    }

    public synchronized void processarTrocaMonstro(ClientHandler cliente, String nomeMonstro) {
        Mons novoMonstro = cliente.getJogador().getMonstroPorNome(nomeMonstro);
        if (novoMonstro != null && novoMonstro.estaVivo()) {
            cliente.setMonstroAtual(novoMonstro);
            cliente.enviarMensagem("MONSTRO_TROCADO " + novoMonstro.getNome() + " " +
                    novoMonstro.getVida() + " " + novoMonstro.getVida());
            cliente.getOponente().enviarMensagem("OPONENTE_MONSTRO " + novoMonstro.getNome() + " " +
                    novoMonstro.getVida() + " " + novoMonstro.getVida());
        } else {
            cliente.enviarMensagem("ERRO Monstro inválido ou derrotado.");
        }
    }

    public void enviarListaMonstros(ClientHandler cliente) {
        StringBuilder lista = new StringBuilder("LISTA_MONSTROS");
        for (Mons monstro : cliente.getJogador().getTime()) {
            lista.append(", ").append(monstro.getNome());
        }
        cliente.enviarMensagem(lista.toString());
    }

    private void encerrarBatalha() {
        System.out.println("Batalha encerrada.");
        for (ClientHandler client : clients) {
            client.desconectar();
        }
        clients.clear();
        batalhaIniciada = false;
    }

    public synchronized void removerCliente(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Cliente desconectado.");

        // Encerrar batalha se um jogador desconectar
        if (batalhaIniciada) {
            encerrarBatalha();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private MultiplayerServer server;
    private PrintWriter out;
    private BufferedReader in;
    private Jogador jogador;
    private ClientHandler oponente;
    private Mons monstroAtual;
    private boolean conectado = true;

    class ClientHandler implements Runnable {
    private Socket socket;
    private MultiplayerServer server;
    private PrintWriter out;
    private BufferedReader in;
    private Jogador jogador;
    private ClientHandler oponente;
    private Mons monstroAtual;
    private boolean conectado = true;

    public ClientHandler(Socket socket, MultiplayerServer server) {
        this.socket = socket;
        this.server = server;
        this.jogador = new Jogador("Jogador"); // Cria um novo jogador com nome padrão
        carregarSave(); // Carrega o save local do jogador
        this.monstroAtual = jogador.getTime().get(0); // Define o primeiro monstro como inicial
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensagem;
            while ((mensagem = in.readLine()) != null) {
                System.out.println("Recebido: " + mensagem);
                processarComando(mensagem);
            }
        } catch (IOException e) {
            System.out.println("Erro no cliente: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void processarComando(String comando) {
        if (comando.startsWith("CONECTAR")) {
            String nomeJogador = comando.split(" ")[1];
            jogador.setNome(nomeJogador);
            System.out.println("Jogador conectado: " + nomeJogador);
        } else if (comando.startsWith("ATAQUE")) {
            String nomeAtaque = comando.split(" ")[1];
            server.processarAtaque(this, nomeAtaque);
        } else if (comando.startsWith("TROCAR_MONSTRO")) {
            String nomeMonstro = comando.split(" ")[1];
            server.processarTrocaMonstro(this, nomeMonstro);
        }
    }

    public void enviarMensagem(String mensagem) {
        if (conectado) {
            out.println(mensagem);
        }
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Mons getMonstroAtual() {
        return monstroAtual;
    }

    public void setMonstroAtual(Mons monstroAtual) {
        this.monstroAtual = monstroAtual;
    }

    public void setOponente(ClientHandler oponente) {
        this.oponente = oponente;
    }

    public ClientHandler getOponente() {
        return oponente;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void desconectar() {
        if (conectado) {
            conectado = false;
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar socket: " + e.getMessage());
            }
            server.removerCliente(this);

            // Notificar o oponente
            if (oponente != null && oponente.isConectado()) {
                oponente.enviarMensagem("FIM_BATALHA VITORIA");
            }
        }
    }

    private void carregarSave() {
        // Simula o carregamento de um save local
        jogador.adicionarMonstro(new Mons("Charmander", 100, 1, Tipo.FOGO, Arrays.asList(
                new Ataques("Golpe Feroz", 50, 10, Tipo.NORMAL),
                new Ataques("Chama", 60, 5, Tipo.FOGO)
        ), 10, 10, 10));
        jogador.adicionarMonstro(new Mons("Squirtle", 100, 1, Tipo.AGUA, Arrays.asList(
                new Ataques("Jato d'Água", 50, 10, Tipo.AGUA),
                new Ataques("Bolhas", 40, 15, Tipo.AGUA)
        ), 10, 10, 10));

        // Adiciona itens ao inventário
        jogador.getInventario().adicionarItem("Garrafa Mágica", 3);
        jogador.getInventario().adicionarItem("Poção de Cura", 2);
    }
}
    private void processarComando(String comando) {
        if (comando.startsWith("CONECTAR")) {
            String nomeJogador = comando.split(" ")[1];
            jogador.setNome(nomeJogador);
            System.out.println("Jogador conectado: " + nomeJogador);
        } else if (comando.startsWith("ATAQUE")) {
            String nomeAtaque = comando.split(" ")[1];
            server.processarAtaque(this, nomeAtaque);
        } else if (comando.startsWith("TROCAR_MONSTRO")) {
            String nomeMonstro = comando.split(" ")[1];
            server.processarTrocaMonstro(this, nomeMonstro);
        }
    }

    public void enviarMensagem(String mensagem) {
        if (conectado) {
            out.println(mensagem);
        }
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Mons getMonstroAtual() {
        return monstroAtual;
    }

    public void setMonstroAtual(Mons monstroAtual) {
        this.monstroAtual = monstroAtual;
    }

    public void setOponente(ClientHandler oponente) {
        this.oponente = oponente;
    }

    public ClientHandler getOponente() {
        return oponente;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void desconectar() {
        if (conectado) {
            conectado = false;
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar socket: " + e.getMessage());
            }
            server.removerCliente(this);

            // Notificar o oponente
            if (oponente != null && oponente.isConectado()) {
                oponente.enviarMensagem("FIM_BATALHA VITORIA");
            }
        }
    }

    private void carregarSave() {
        // Simula o carregamento de um save local
        jogador.getTime().add(new Mons("Charmander", 100, 1, Tipo.FOGO, Arrays.asList(
                new Ataques("Golpe Feroz", 50, 10, Tipo.NORMAL),
                new Ataques("Chama", 60, 5, Tipo.FOGO)
        ), 10, 10, 10));
        jogador.getTime().add(new Mons("Squirtle", 100, 1, Tipo.AGUA, Arrays.asList(
                new Ataques("Jato d'Água", 50, 10, Tipo.AGUA),
                new Ataques("Bolhas", 40, 15, Tipo.AGUA)
        ), 10, 10, 10));
    }
}