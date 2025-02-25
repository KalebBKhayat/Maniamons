import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static final String SAVE_FILE = "save.dat";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        

        
        System.out.println("Bem-vindo ao mundo dos ManiaMons!");
        System.out.println("Neste mundo, cada monstro representa um campo de estudo.");
        System.out.println("Você pode escolher entre três monstros iniciais:");
        System.out.println("1 - Mc: Representa a Psicologia. Forte contra Computação.");
        System.out.println("2 - Libs: Representa a Computação. Forte contra Sociologia.");
        System.out.println("3 - Pedro: Representa a Sociologia. Forte contra Psicologia.");
        System.out.println("Cada escolha moldará sua jornada. Escolha sabiamente!");

        
        System.out.print("Digite o número do seu monstro inicial (1, 2 ou 3): ");
        int escolhaInicial = scanner.nextInt();

        
        Jogador jogador = new Jogador("Treinador");
        Mons monstroInicial = null;

        switch (escolhaInicial) {
            case 1:
                monstroInicial = criarMc();
                System.out.println("Você escolheu Mc! Ele é um especialista em Psicologia.");
                break;
            case 2:
                monstroInicial = criarLibs();
                System.out.println("Você escolheu Libs! Ela domina a Computação.");
                break;
            case 3:
                monstroInicial = criarPedro();
                System.out.println("Você escolheu Pedro! Ele é um mestre em Sociologia.");
                break;
            default:
                System.out.println("Escolha inválida! Você receberá Mc como padrão.");
                monstroInicial = criarMc();
                break;
        }

        
        jogador.adicionarMonstro(monstroInicial);
        System.out.println(monstroInicial.getNome() + " foi adicionado ao seu time!");

        
        Menu.exibirMenu(jogador);

        
        scanner.close();
        System.out.println("Obrigado por jogar! Até a próxima!");
    }

    
    public static Mons criarMc() {
        Ataques socio1 = new Ataques("Revolução Social", Tipo.SOCIOLOGIA, 100, 85, 5, "NORMAL");
        Ataques socio2 = new Ataques("Movimento Coletivo", Tipo.SOCIOLOGIA, 75, 90, 10,"NORMAL");
        ArrayList<Ataques> ataquesSociologia = new ArrayList<>();
        ataquesSociologia.add(socio1);
        ataquesSociologia.add(socio2);
        return new Mons("Mc", 900, 1, Tipo.SOCIOLOGIA, ataquesSociologia,100,20,100);
    }

    public static Mons criarLibs() {
        Ataques comp1 = new Ataques("Algoritmo Fatal", Tipo.COMPUTACAO, 90, 95, 10, "NORMAL");
        Ataques comp2 = new Ataques("Erro Crítico", Tipo.COMPUTACAO, 70, 100, 20, "NORMAL");
        ArrayList<Ataques> ataquesComputacao = new ArrayList<>();
        ataquesComputacao.add(comp1);
        ataquesComputacao.add(comp2);
        return new Mons("Libs", 1500, 1, Tipo.COMPUTACAO, ataquesComputacao,40,100,50);
    }

    public static Mons criarPedro() {
        Ataques psico1 = new Ataques("Análise Profunda", Tipo.PSICOLOGIA, 80, 100, 10, "NORMAL");
        Ataques psico2 = new Ataques("Terapia Cognitiva", Tipo.PSICOLOGIA, 60, 90, 15, "CURA");
        ArrayList<Ataques> ataquesPsicologia = new ArrayList<>();
        ataquesPsicologia.add(psico1);
        ataquesPsicologia.add(psico2);
        return new Mons("Pedro", 1200, 1, Tipo.PSICOLOGIA, ataquesPsicologia,75,50,75);
    }

    private static Jogador carregarJogo() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Jogador jogador = (Jogador) ois.readObject();
            System.out.println("Jogo carregado com sucesso!");
            return jogador;
        } catch (FileNotFoundException e) {
            System.out.println("Nenhum save encontrado.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar o jogo: " + e.getMessage());
        }
        return null;
    }

    private static void salvarJogo(Jogador jogador) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(jogador);
            System.out.println("Jogo salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }
}
