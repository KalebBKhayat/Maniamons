import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class GameGUI {
    private JFrame frame;
    private Jogador jogador;

    public GameGUI() {
        jogador = null;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("ManiaMons");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tela Inicial
        JPanel telaInicial = new JPanel();
        telaInicial.setLayout(new GridLayout(4, 1)); // Adicionamos mais um botão: Carregar Jogo
        telaInicial.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JLabel titulo = new JLabel("Bem-vindo ao Mundo dos ManiaMons!", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JButton iniciarJogoButton = new JButton("Iniciar Jogo");
        JButton carregarJogoButton = new JButton("Carregar Jogo");
        JButton sairButton = new JButton("Sair");

        telaInicial.add(titulo);
        telaInicial.add(iniciarJogoButton);
        telaInicial.add(carregarJogoButton);
        telaInicial.add(sairButton);

        frame.add(telaInicial, BorderLayout.CENTER);

        // Ação do botão "Iniciar Jogo"
        iniciarJogoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJogo();
            }
        });

        // Ação do botão "Carregar Jogo"
        carregarJogoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarJogoInicial();
            }
        });

        // Ação do botão "Sair"
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void iniciarJogo() {
        // Solicitar o nome do jogador
        String nomeJogador = JOptionPane.showInputDialog(frame, "Digite seu nome:", "Nome do Jogador", JOptionPane.PLAIN_MESSAGE);
        if (nomeJogador == null || nomeJogador.trim().isEmpty()) {
            nomeJogador = "Treinador"; // Nome padrão se o jogador não inserir nada
        }

        // Escolher Monstro Inicial
        String[] opcoesMonstros = {"Mc (Psicologia)", "Libs (Computação)", "Pedro (Sociologia)"};
        String escolha = (String) JOptionPane.showInputDialog(
                frame,
                "Escolha seu monstro inicial:",
                "Monstro Inicial",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesMonstros,
                opcoesMonstros[0]
        );

        Mons monstroInicial = null;
        if (escolha != null) {
            switch (escolha) {
                case "Mc (Psicologia)":
                    monstroInicial = Main.criarMc();
                    break;
                case "Libs (Computação)":
                    monstroInicial = Main.criarLibs();
                    break;
                case "Pedro (Sociologia)":
                    monstroInicial = Main.criarPedro();
                    break;
            }
        }

        if (monstroInicial == null) {
            JOptionPane.showMessageDialog(frame, "Escolha inválida! Você receberá Mc como padrão.");
            monstroInicial = Main.criarMc();
        }

        // Criar o jogador com o nome fornecido
        jogador = new Jogador(nomeJogador);
        jogador.adicionarMonstro(monstroInicial);
        JOptionPane.showMessageDialog(frame, "Você escolheu " + monstroInicial.getNome() + "!");

        // Mostrar Menu Principal
        mostrarMenuPrincipal();
    }

    private void carregarJogoInicial() {
        jogador = SaveLoadCSV.carregarJogador();
        if (jogador != null) {
            jogador.setTime(SaveLoadCSV.carregarMonstros());
            jogador.setInventario(SaveLoadCSV.carregarInventario());
            JOptionPane.showMessageDialog(frame, "Jogo carregado com sucesso!");
            mostrarMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(frame, "Nenhum save encontrado!");
        }
    }

    private void mostrarMenuPrincipal() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(7, 1)); // Adicionamos mais duas opções: Salvar e Carregar
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel nomeJogadorLabel = new JLabel("Jogador: " + jogador.getNome(), SwingConstants.CENTER);
        menuPanel.add(nomeJogadorLabel, 0);

        JButton procurarBatalhaButton = new JButton("Procurar Batalha na Maramabaia");
        JButton lojaButton = new JButton("Ir à Loja do Marcio Almeida");
        JButton curarButton = new JButton("Curar Monstros na casa do Breno");
        JButton inventarioButton = new JButton("Mostrar Inventário");
        JButton boxButton = new JButton("Mostrar mons na box");
        JButton salvarButton = new JButton("Salvar Jogo");
        JButton carregarButton = new JButton("Carregar Jogo");
        JButton conectarServidorButton = new JButton("Conectar ao Servidor");
        JButton sairButton = new JButton("Sair");

        menuPanel.add(procurarBatalhaButton);
        menuPanel.add(lojaButton);
        menuPanel.add(curarButton);
        menuPanel.add(inventarioButton);
        menuPanel.add(boxButton);
        menuPanel.add(salvarButton);
        menuPanel.add(carregarButton);
        menuPanel.add(conectarServidorButton);
        menuPanel.add(sairButton);

        frame.add(menuPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        // Ações dos botões


        boxButton.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e){
                mostrarMons();
            }
        });
        conectarServidorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conectarAoServidor();
            }
        });

        procurarBatalhaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mons selvagem = ListaMonstros.encontrarMonstro();
                if (selvagem != null) {
                    iniciarBatalha(selvagem);
                } else {
                    JOptionPane.showMessageDialog(frame, "Nenhum monstro encontrado!");
                }
            }
        });

        lojaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirLojaGUI();
            }
        });

        curarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogador.curarTodos();
                JOptionPane.showMessageDialog(frame, "Todos os seus monstros foram curados!");
            }
        });

        inventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jogador.getInventario().listarItens();
            }
        });

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarJogo();
            }
        });

        carregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarJogo();
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    private void conectarAoServidor() {
        // Carregar o save local do jogador
        Jogador jogadorSalvo = SaveLoadCSV.carregarJogador();
        if (jogadorSalvo == null) {
            JOptionPane.showMessageDialog(frame, "Nenhum save encontrado! Crie um novo jogo primeiro.");
            return;
        }
    
        // Atualizar o jogador com os dados salvos
        jogador = jogadorSalvo;
        jogador.setTime(SaveLoadCSV.carregarMonstros());
        jogador.setInventario(SaveLoadCSV.carregarInventario());
    
        // Conectar ao servidor
        String serverIp = JOptionPane.showInputDialog(frame, "Digite o IP do servidor:");
        if (serverIp == null || serverIp.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "IP do servidor inválido!");
            return;
        }
    
        try {
            Socket socket = new Socket(serverIp, 14481); // Conecta ao servidor
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            JOptionPane.showMessageDialog(frame, "Conectado ao servidor!");
    
            // Enviar os dados do jogador para o servidor
            out.println("CONECTAR " + jogador.getNome());
    
            // Aguardar início da batalha
            String mensagem = in.readLine();
            if (mensagem.equals("INICIAR_BATALHA")) {
                iniciarBatalhaMultiplayer(socket, out, in); // Inicia batalha multiplayer
            }
    
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private void iniciarBatalhaMultiplayer(Socket socket, PrintWriter out, BufferedReader in) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
    
        JPanel batalhaPanel = new JPanel();
        batalhaPanel.setLayout(new BorderLayout());
        batalhaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Exibir Monstros e Status
        JPanel monstrosPanel = new JPanel();
        monstrosPanel.setLayout(new GridLayout(2, 1));
    
        Mons monstroJogador = jogador.getTime().get(0);
    
        // Usar um array para armazenar monstroOponente
        Mons[] monstroOponente = new Mons[1]; // Array de tamanho 1
    
        JLabel jogadorLabel = new JLabel("Seu Monstro: " + monstroJogador.getNome(), SwingConstants.CENTER);
        JProgressBar barraVidaJogador = criarBarraDeVida(monstroJogador.getVida(), monstroJogador.getVida());
    
        JLabel oponenteLabel = new JLabel("Monstro Oponente: Carregando...", SwingConstants.CENTER);
        JProgressBar barraVidaOponente = criarBarraDeVida(100, 100); // Placeholder
    
        JPanel jogadorPanel = new JPanel(new BorderLayout());
        jogadorPanel.add(jogadorLabel, BorderLayout.NORTH);
        jogadorPanel.add(barraVidaJogador, BorderLayout.CENTER);
    
        JPanel oponentePanel = new JPanel(new BorderLayout());
        oponentePanel.add(oponenteLabel, BorderLayout.NORTH);
        oponentePanel.add(barraVidaOponente, BorderLayout.CENTER);
    
        monstrosPanel.add(jogadorPanel);
        monstrosPanel.add(oponentePanel);
    
        // Menu de Ações
        JPanel acoesPanel = new JPanel();
        acoesPanel.setLayout(new GridLayout(4, 1));
    
        JButton atacarButton = new JButton("Atacar");
        JButton capturarButton = new JButton("Capturar");
        JButton fugirButton = new JButton("Fugir");
        JButton usarItemButton = new JButton("Usar Item");
    
        acoesPanel.add(atacarButton);
        acoesPanel.add(capturarButton);
        acoesPanel.add(fugirButton);
        acoesPanel.add(usarItemButton);
    
        batalhaPanel.add(monstrosPanel, BorderLayout.CENTER);
        batalhaPanel.add(acoesPanel, BorderLayout.SOUTH);
    
        frame.add(batalhaPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    
        // Receber informações do oponente
        try {
            String mensagem = in.readLine();
            if (mensagem.startsWith("OPONENTE_MONSTRO")) {
                String[] dados = mensagem.split(" ");
                String nomeOponente = dados[1];
                int vidaOponente = Integer.parseInt(dados[2]);
                int vidaMaximaOponente = Integer.parseInt(dados[3]);
    
                monstroOponente[0] = new Mons(nomeOponente, vidaMaximaOponente, 1, Tipo.FISIOTERAPIA, new ArrayList<>(), 10, 10, 10);
                monstroOponente[0].receberDano(vidaMaximaOponente - vidaOponente);
    
                oponenteLabel.setText("Monstro Oponente: " + nomeOponente);
                barraVidaOponente.setMaximum(vidaMaximaOponente);
                barraVidaOponente.setValue(vidaOponente);
                barraVidaOponente.setString(vidaOponente + "/" + vidaMaximaOponente);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Erro ao receber dados do oponente: " + e.getMessage());
            return;
        }
    
        // Lógica da batalha
        atacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Ataques> ataques = monstroJogador.getAtaques();
    
                if (ataques.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Seu monstro não possui ataques!");
                    return;
                }
    
                String[] opcoesAtaques = new String[ataques.size()];
                for (int i = 0; i < ataques.size(); i++) {
                    Ataques ataque = ataques.get(i);
                    opcoesAtaques[i] = ataque.getNome() + " (PP: " + ataque.getPp() + ")";
                }
    
                String escolha = (String) JOptionPane.showInputDialog(
                        frame,
                        "Escolha um ataque:",
                        "Ataques",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoesAtaques,
                        opcoesAtaques[0]
                );
    
                if (escolha != null) {
                    int indice = -1;
                    for (int i = 0; i < opcoesAtaques.length; i++) {
                        if (opcoesAtaques[i].equals(escolha)) {
                            indice = i;
                            break;
                        }
                    }
    
                    if (indice != -1) {
                        Ataques ataqueEscolhido = ataques.get(indice);
                        if (ataqueEscolhido.getPp() > 0) {
                            // Enviar o ataque ao servidor
                            out.println("ATAQUE " + ataqueEscolhido.getNome());
    
                            // Aguardar resposta do servidor
                            try {
                                String mensagem = in.readLine();
                                if (mensagem.startsWith("ATUALIZAR_VIDA")) {
                                    String[] dados = mensagem.split(" ");
                                    int vidaJogador = Integer.parseInt(dados[1]);
                                    int vidaOponente = Integer.parseInt(dados[2]);
    
                                    barraVidaJogador.setValue(vidaJogador);
                                    barraVidaOponente.setValue(vidaOponente);
    
                                    if (!monstroJogador.estaVivo()) {
                                        JOptionPane.showMessageDialog(frame, "Você foi derrotado!");
                                        mostrarMenuPrincipal();
                                        return;
                                    }
    
                                    if (monstroOponente[0] != null && !monstroOponente[0].estaVivo()) {
                                        JOptionPane.showMessageDialog(frame, "Você venceu a batalha!");
                                        mostrarMenuPrincipal();
                                        return;
                                    }
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(frame, "Erro durante a batalha: " + ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Este ataque não tem PP suficiente!");
                        }
                    }
                }
            }
        });
    
        // Desativar botões desnecessários no multiplayer
        capturarButton.setEnabled(false);
        fugirButton.setEnabled(false);
        usarItemButton.setEnabled(false);
    }



    private void mostrarMons(){
        JPanel boxJPanel = new JPanel();
        boxJPanel.setLayout(new GridLayout(6,2));
        boxJPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        JLabel titulo = new JLabel();

        boxJPanel.add(titulo);
        

    }


    private void abrirLojaGUI() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel lojaPanel = new JPanel();
        lojaPanel.setLayout(new GridLayout(4, 1));
        lojaPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titulo = new JLabel("Loja", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JButton garrafaMagicaButton = new JButton("Comprar Garrafa Mágica (50 moedas)");
        JButton pocaoCuraButton = new JButton("Comprar Poção de Cura (30 moedas)");
        JButton sairButton = new JButton("Sair");

        lojaPanel.add(titulo);
        lojaPanel.add(garrafaMagicaButton);
        lojaPanel.add(pocaoCuraButton);
        lojaPanel.add(sairButton);

        frame.add(lojaPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        // Ação do botão "Garrafa Mágica"
        garrafaMagicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jogador.gastarDinheiro(50)) {
                    jogador.getInventario().adicionarItem("Garrafa Mágica", 1);
                    JOptionPane.showMessageDialog(frame, "Você comprou uma Garrafa Mágica!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Você não tem dinheiro suficiente!");
                }
            }
        });

        // Ação do botão "Poção de Cura"
        pocaoCuraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jogador.gastarDinheiro(30)) {
                    jogador.getInventario().adicionarItem("Poção de Cura", 1);
                    JOptionPane.showMessageDialog(frame, "Você comprou uma Poção de Cura!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Você não tem dinheiro suficiente!");
                }
            }
        });

        // Ação do botão "Sair"
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarMenuPrincipal();
            }
        });
    }

    private JProgressBar criarBarraDeVida(int vidaAtual, int vidaMaxima) {
        JProgressBar barraDeVida = new JProgressBar(0, vidaMaxima);
        barraDeVida.setValue(vidaAtual);
        barraDeVida.setStringPainted(true); // Mostrar o valor numérico
        barraDeVida.setString(vidaAtual + "/" + vidaMaxima);
        barraDeVida.setForeground(Color.GREEN); // Cor da barra
        return barraDeVida;
    }

    private void iniciarBatalha(Mons selvagem) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel batalhaPanel = new JPanel();
        batalhaPanel.setLayout(new BorderLayout());
        batalhaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Exibir Monstros e Status
        JPanel monstrosPanel = new JPanel();
        monstrosPanel.setLayout(new GridLayout(2, 1));

        Mons monstroJogador = jogador.getTime().get(0);

        JLabel jogadorLabel = new JLabel("Seu Monstro: " + monstroJogador.getNome(), SwingConstants.CENTER);
        JProgressBar barraVidaJogador = criarBarraDeVida(monstroJogador.getVida(), monstroJogador.getVida());

        JLabel selvagemLabel = new JLabel("Monstro Selvagem: " + selvagem.getNome(), SwingConstants.CENTER);
        JProgressBar barraVidaSelvagem = criarBarraDeVida(selvagem.getVida(), selvagem.getVida());

        JPanel jogadorPanel = new JPanel(new BorderLayout());
        jogadorPanel.add(jogadorLabel, BorderLayout.NORTH);
        jogadorPanel.add(barraVidaJogador, BorderLayout.CENTER);

        JPanel selvagemPanel = new JPanel(new BorderLayout());
        selvagemPanel.add(selvagemLabel, BorderLayout.NORTH);
        selvagemPanel.add(barraVidaSelvagem, BorderLayout.CENTER);

        monstrosPanel.add(jogadorPanel);
        monstrosPanel.add(selvagemPanel);

        // Menu de Ações
        JPanel acoesPanel = new JPanel();
        acoesPanel.setLayout(new GridLayout(4, 1));

        JButton atacarButton = new JButton("Atacar");
        JButton capturarButton = new JButton("Capturar");
        JButton fugirButton = new JButton("Fugir");
        JButton usarItemButton = new JButton("Usar Item");

        acoesPanel.add(atacarButton);
        acoesPanel.add(capturarButton);
        acoesPanel.add(fugirButton);
        acoesPanel.add(usarItemButton);

        batalhaPanel.add(monstrosPanel, BorderLayout.CENTER);
        batalhaPanel.add(acoesPanel, BorderLayout.SOUTH);

        frame.add(batalhaPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();

        // Ação do botão "Atacar"
        atacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Ataques> ataques = monstroJogador.getAtaques();

                if (ataques.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Seu monstro não possui ataques!");
                    return;
                }

                String[] opcoesAtaques = new String[ataques.size()];
                for (int i = 0; i < ataques.size(); i++) {
                    Ataques ataque = ataques.get(i);
                    opcoesAtaques[i] = ataque.getNome() + " (PP: " + ataque.getPp() + ")";
                }

                String escolha = (String) JOptionPane.showInputDialog(
                        frame,
                        "Escolha um ataque:",
                        "Ataques",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoesAtaques,
                        opcoesAtaques[0]
                );

                if (escolha != null) {
                    int indice = -1;
                    for (int i = 0; i < opcoesAtaques.length; i++) {
                        if (opcoesAtaques[i].equals(escolha)) {
                            indice = i;
                            break;
                        }
                    }

                    if (indice != -1) {
                        Ataques ataqueEscolhido = ataques.get(indice);
                        if (ataqueEscolhido.getPp() > 0) {
                            double efetividade = ataqueEscolhido.getTipo().getEfetividadeContra(selvagem.getTipo());
                            ataqueEscolhido.usarAtaque(monstroJogador, selvagem);

                            String mensagemEfetividade = "";
                            if (efetividade > 1.5) {
                                mensagemEfetividade = "Super efetivo!";
                            } else if (efetividade < 0.8) {
                                mensagemEfetividade = "Ineficaz...";
                            } else {
                                mensagemEfetividade = "Efeito normal.";
                            }

                            JOptionPane.showMessageDialog(frame, monstroJogador.getNome() + " usou " + ataqueEscolhido.getNome() + "!\n" + mensagemEfetividade);

                            // Atualizar barras de vida
                            barraVidaSelvagem.setValue(selvagem.getVida());
                            barraVidaSelvagem.setString(selvagem.getVida() + "/" + selvagem.getVida());

                            if (!selvagem.estaVivo()) {
                                JOptionPane.showMessageDialog(frame, "Você derrotou " + selvagem.getNome() + "!");
                                mostrarMenuPrincipal();
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Este ataque não tem PP suficiente!");
                        }
                    }
                }
            }
        });

        // Ação do botão "Capturar"
        capturarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean capturado = jogador.capturarMonstro(selvagem);
                if (capturado) {
                    JOptionPane.showMessageDialog(frame, "Você capturou " + selvagem.getNome() + "!");
                    mostrarMenuPrincipal();
                } else {
                    JOptionPane.showMessageDialog(frame, "Falha ao capturar!");
                }
            }
        });

        // Ação do botão "Fugir"
        fugirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Você fugiu!");
                mostrarMenuPrincipal();
            }
        });

        // Ação do botão "Usar Item"
        
    }

    private void salvarJogo() {
        SaveLoadCSV.salvarJogador(jogador);
        SaveLoadCSV.salvarMonstros(jogador.getTime());
        SaveLoadCSV.salvarInventario(jogador.getInventario());
        JOptionPane.showMessageDialog(frame, "Jogo salvo com sucesso!");
    }

    private void carregarJogo() {
        jogador = SaveLoadCSV.carregarJogador();
        if (jogador != null) {
            jogador.setTime(SaveLoadCSV.carregarMonstros());
            jogador.setInventario(SaveLoadCSV.carregarInventario());
            JOptionPane.showMessageDialog(frame, "Jogo carregado com sucesso!");
            mostrarMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(frame, "Nenhum save encontrado!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameGUI();
            }
        });
    }
}