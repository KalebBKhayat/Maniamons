import java.io.*;
import java.util.ArrayList;

public class SaveLoadCSV {
    private static final String JOGADOR_FILE = "jogador.csv";
    private static final String MONSTROS_FILE = "monstros.csv";
    private static final String INVENTARIO_FILE = "inventario.csv";

    // Salvar Jogador
    public static void salvarJogador(Jogador jogador) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JOGADOR_FILE))) {
            writer.write("Nome,Dinheiro");
            writer.newLine();
            writer.write(jogador.getNome() + "," + jogador.getDinheiro());
        } catch (IOException e) {
            System.out.println("Erro ao salvar jogador: " + e.getMessage());
        }
    }

    // Carregar Jogador
    public static Jogador carregarJogador() {
        Jogador jogador = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(JOGADOR_FILE))) {
            String linha = reader.readLine(); // Ler o cabeçalho
            if (linha == null || !linha.equals("Nome,Dinheiro")) {
                System.out.println("Arquivo de jogador inválido ou vazio!");
                return null;
            }

            if ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length < 2) { // Verifica se há pelo menos dois valores (nome e dinheiro)
                    System.out.println("Dados incompletos no arquivo de jogador!");
                    return null;
                }

                String nome = dados[0];
                int dinheiro = Integer.parseInt(dados[1]);
                jogador = new Jogador(nome);
                jogador.adicionarDinheiro(dinheiro); // Ajuste inicial
            } else {
                System.out.println("Nenhum dado de jogador encontrado!");
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar jogador: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valor numérico no arquivo de jogador: " + e.getMessage());
        }
        return jogador;
    }

    // Salvar Monstros
    public static void salvarMonstros(ArrayList<Mons> monstros) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MONSTROS_FILE))) {
            writer.write("Nome,Vida,VidaMaxima,Nivel,ExpAtual,ExpProximo,Tipo,AtaqueBase,DefesaBase,VelocidadeBase");
            writer.newLine();
            for (Mons monstro : monstros) {
                writer.write(monstro.getNome() + "," +
                        monstro.getVida() + "," +
                        monstro.getVida() + "," +
                        monstro.getNivel() + "," +
                        monstro.getExpAtual() + "," +
                        monstro.getExpParaProximoNivel() + "," +
                        monstro.getTipo().name() + "," +
                        monstro.getAtaqueBase() + "," +
                        monstro.getDefesaBase() + "," +
                        monstro.getVelocidadeBase());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar monstros: " + e.getMessage());
        }
    }

    // Carregar Monstros
    public static ArrayList<Mons> carregarMonstros() {
        ArrayList<Mons> monstros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MONSTROS_FILE))) {
            String linha = reader.readLine(); // Ler o cabeçalho
            if (linha == null || !linha.equals("Nome,Vida,VidaMaxima,Nivel,ExpAtual,ExpProximo,Tipo,AtaqueBase,DefesaBase,VelocidadeBase")) {
                System.out.println("Arquivo de monstros inválido ou vazio!");
                return monstros;
            }

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length < 10) { // Verifica se há todas as colunas necessárias
                    System.out.println("Dados incompletos no arquivo de monstros!");
                    continue;
                }

                String nome = dados[0];
                int vida = Integer.parseInt(dados[1]);
                int vidaMaxima = Integer.parseInt(dados[2]);
                int nivel = Integer.parseInt(dados[3]);
                int expAtual = Integer.parseInt(dados[4]);
                int expProximo = Integer.parseInt(dados[5]);
                Tipo tipo = Tipo.valueOf(dados[6]);
                int ataqueBase = Integer.parseInt(dados[7]);
                int defesaBase = Integer.parseInt(dados[8]);
                int velocidadeBase = Integer.parseInt(dados[9]);

                Mons monstro = new Mons(nome, vidaMaxima, nivel, tipo, new ArrayList<>(), ataqueBase, defesaBase, velocidadeBase);
                monstro.receberDano(vidaMaxima - vida); // Ajustar vida atual
                monstro.setExpAtual(expAtual);
                monstro.setExpParaProximoNivel(expProximo);

                monstros.add(monstro);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar monstros: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valor numérico no arquivo de monstros: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao carregar tipo de monstro: " + e.getMessage());
        }
        return monstros;
    }

    // Salvar Inventário
    public static void salvarInventario(Inventario inventario) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTARIO_FILE))) {
            writer.write("Item,Quantidade");
            writer.newLine();
            for (String item : inventario.listarItens().keySet()) {
                writer.write(item + "," + inventario.getQuantidade(item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar inventário: " + e.getMessage());
        }
    }

    // Carregar Inventário
    public static Inventario carregarInventario() {
        Inventario inventario = new Inventario();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTARIO_FILE))) {
            String linha = reader.readLine(); // Ler o cabeçalho
            if (linha == null || !linha.equals("Item,Quantidade")) {
                System.out.println("Arquivo de inventário inválido ou vazio!");
                return inventario;
            }

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length < 2) { // Verifica se há pelo menos duas colunas (item e quantidade)
                    System.out.println("Dados incompletos no arquivo de inventário!");
                    continue;
                }

                String item = dados[0];
                int quantidade = Integer.parseInt(dados[1]);
                inventario.adicionarItem(item, quantidade);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar inventário: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valor numérico no arquivo de inventário: " + e.getMessage());
        }
        return inventario;
    }
}