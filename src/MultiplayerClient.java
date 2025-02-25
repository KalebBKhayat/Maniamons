import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class MultiplayerClient {
    private static final String SERVER_IP = "0.tcp.sa.ngrok.io"; // Altere para o IP do servidor
    private static final int PORT = 19301;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JFrame frame;
    private JLabel jogadorLabel, oponenteLabel;
    private JProgressBar barraVidaJogador, barraVidaOponente;
    private JButton[] botoesAtaques;
    private DefaultListModel<String> modeloMonstros;
    private JList<String> listaMonstros;

    public static void main(String[] args) {
        new MultiplayerClient().start();
    }

    public void start() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado ao servidor!");

            criarInterfaceGrafica();

            // Receber mensagens do servidor
            new Thread(() -> {
                try {
                    String mensagem;
                    while ((mensagem = in.readLine()) != null) {
                        processarMensagem(mensagem);
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao receber mensagem: " + e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private void criarInterfaceGrafica() {
        frame = new JFrame("Multiplayer Battle");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Painel central: Monstros e barras de vida
        JPanel centroPanel = new JPanel(new GridLayout(2, 1));
        jogadorLabel = new JLabel("Seu Monstro: Carregando...", SwingConstants.CENTER);
        barraVidaJogador = criarBarraDeVida(100, 100);
        JPanel jogadorPanel = new JPanel(new BorderLayout());
        jogadorPanel.add(jogadorLabel, BorderLayout.NORTH);
        jogadorPanel.add(barraVidaJogador, BorderLayout.CENTER);

        oponenteLabel = new JLabel("Monstro Oponente: Carregando...", SwingConstants.CENTER);
        barraVidaOponente = criarBarraDeVida(100, 100);
        JPanel oponentePanel = new JPanel(new BorderLayout());
        oponentePanel.add(oponenteLabel, BorderLayout.NORTH);
        oponentePanel.add(barraVidaOponente, BorderLayout.CENTER);

        centroPanel.add(jogadorPanel);
        centroPanel.add(oponentePanel);

        // Painel lateral: Lista de monstros e botões de ataque
        JPanel lateralPanel = new JPanel(new BorderLayout());
        modeloMonstros = new DefaultListModel<>();
        listaMonstros = new JList<>(modeloMonstros);
        listaMonstros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollMonstros = new JScrollPane(listaMonstros);
        lateralPanel.add(scrollMonstros, BorderLayout.CENTER);

        JButton trocarMonstroButton = new JButton("Trocar Monstro");
        trocarMonstroButton.addActionListener(e -> {
            String monstroSelecionado = listaMonstros.getSelectedValue();
            if (monstroSelecionado != null) {
                enviarComando("TROCAR_MONSTRO " + monstroSelecionado);
            } else {
                JOptionPane.showMessageDialog(frame, "Selecione um monstro para trocar!");
            }
        });
        lateralPanel.add(trocarMonstroButton, BorderLayout.SOUTH);

        // Painel inferior: Botões de ataque
        JPanel ataquesPanel = new JPanel(new GridLayout(2, 2));
        botoesAtaques = new JButton[4];
        for (int i = 0; i < botoesAtaques.length; i++) {
            botoesAtaques[i] = new JButton("Ataque " + (i + 1));
            final int indice = i;
            botoesAtaques[i].addActionListener(e -> {
                enviarComando("ATAQUE " + botoesAtaques[indice].getText());
            });
            ataquesPanel.add(botoesAtaques[i]);
        }

        mainPanel.add(centroPanel, BorderLayout.CENTER);
        mainPanel.add(lateralPanel, BorderLayout.EAST);
        mainPanel.add(ataquesPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JProgressBar criarBarraDeVida(int vidaAtual, int vidaMaxima) {
        JProgressBar barraVida = new JProgressBar(0, vidaMaxima);
        barraVida.setValue(vidaAtual);
        barraVida.setString(vidaAtual + "/" + vidaMaxima);
        barraVida.setStringPainted(true);
        return barraVida;
    }

    private void processarMensagem(String mensagem) {
        if (mensagem.startsWith("OPONENTE_MONSTRO")) {
            String[] dados = mensagem.split(" ");
            String nomeOponente = dados[1];
            int vidaOponente = Integer.parseInt(dados[2]);
            int vidaMaximaOponente = Integer.parseInt(dados[3]);

            oponenteLabel.setText("Monstro Oponente: " + nomeOponente);
            barraVidaOponente.setMaximum(vidaMaximaOponente);
            barraVidaOponente.setValue(vidaOponente);
        } else if (mensagem.startsWith("ATUALIZAR_VIDA")) {
            String[] dados = mensagem.split(" ");
            int vidaJogador = Integer.parseInt(dados[1]);
            int vidaOponente = Integer.parseInt(dados[2]);

            barraVidaJogador.setValue(vidaJogador);
            barraVidaOponente.setValue(vidaOponente);
        } else if (mensagem.startsWith("SEU_TURNO")) {
            habilitarBotoes(true);
        } else if (mensagem.startsWith("AGUARDANDO")) {
            habilitarBotoes(false);
        } else if (mensagem.startsWith("FIM_BATALHA")) {
            String resultado = mensagem.split(" ")[1];
            JOptionPane.showMessageDialog(frame, "Fim da batalha! Resultado: " + resultado);
            frame.dispose();
        } else if (mensagem.startsWith("LISTA_MONSTROS")) {
            String[] monstros = mensagem.substring(14).split(",");
            modeloMonstros.clear();
            for (String monstro : monstros) {
                modeloMonstros.addElement(monstro.trim());
            }
        }
    }

    private void habilitarBotoes(boolean habilitado) {
        for (JButton botao : botoesAtaques) {
            botao.setEnabled(habilitado);
        }
    }

    private void enviarComando(String comando) {
        out.println(comando);
    }
}