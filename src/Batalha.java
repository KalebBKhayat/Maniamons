import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Batalha {
    public static void iniciarBatalha(Jogador jogador, Mons selvagem) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Mons monstroJogador = jogador.getTime().get(0); 

        System.out.println("Um " + selvagem.getNome() + " apareceu!");

        while (jogador.temMonstrosVivos() && selvagem.estaVivo()) {
           
            Ataques ataqueJogador = null;
            Ataques ataqueSelvagem = null;

            
            if (!monstroJogador.estaVivo()) {
                System.out.println(monstroJogador.getNome() + " foi derrotado!");
                jogador.listarMonstros();
                System.out.print("Escolha um novo monstro (1-" + jogador.getTime().size() + "): ");
                int indice = scanner.nextInt() - 1;
                if (indice >= 0 && indice < jogador.getTime().size()) {
                    Mons novoMonstro = jogador.getTime().get(indice);
                    if (novoMonstro.estaVivo()) {
                        monstroJogador = novoMonstro;
                        System.out.println("Você trocou para " + monstroJogador.getNome() + "!");
                    } else {
                        System.out.println("Este monstro não pode lutar!");
                        continue; 
                    }
                } else {
                    System.out.println("Escolha inválida!");
                    continue; 
                }
            }

            
            System.out.println("\n--- Turno ---");
            System.out.println(monstroJogador.getNome() + " (Vida: " + monstroJogador.getVida() + ")");
            System.out.println(selvagem.getNome() + " (Vida: " + selvagem.getVida() + ")");
            System.out.println("1 - Atacar | 2 - Capturar | 3 - Fugir | 4 - Trocar Monstro | 5 - Usar Item");
            int escolha = scanner.nextInt();

            if (escolha == 1) {
                monstroJogador.listarAtaques();
                System.out.println("Escolha um ataque:");
                int indiceAtaque = scanner.nextInt() - 1;
                ataqueJogador = monstroJogador.escolherAtaque(indiceAtaque);
                if (ataqueJogador == null) {
                    System.out.println("Ataque inválido! Escolhendo um ataque aleatório.");
                    ArrayList<Ataques> ataquesDisponiveis = monstroJogador.getAtaques();
                    ataqueJogador = ataquesDisponiveis.get(random.nextInt(ataquesDisponiveis.size()));
                }
            } else if (escolha == 2) {
                if (jogador.capturarMonstro(selvagem)) return;
            } else if (escolha == 3) {
                System.out.println("Você fugiu!");
                return;
            } else if (escolha == 4) {
                jogador.listarMonstros();
                System.out.print("Escolha um novo monstro (1-" + jogador.getTime().size() + "): ");
                int indice = scanner.nextInt() - 1;
                if (indice >= 0 && indice < jogador.getTime().size()) {
                    Mons novoMonstro = jogador.getTime().get(indice);
                    if (novoMonstro.estaVivo()) {
                        monstroJogador = novoMonstro;
                        System.out.println("Você trocou para " + monstroJogador.getNome() + "!");
                    } else {
                        System.out.println("Este monstro não pode lutar!");
                    }
                } else {
                    System.out.println("Escolha inválida!");
                }
            } 

            
            ArrayList<Ataques> ataquesSelvagem = selvagem.getAtaques();
            if (!ataquesSelvagem.isEmpty()) {
                ataqueSelvagem = ataquesSelvagem.get(random.nextInt(ataquesSelvagem.size()));
            }

            
            boolean jogadorAtacaPrimeiro = monstroJogador.getVelocidadeBase() >= selvagem.getVelocidadeBase();

           
            if (jogadorAtacaPrimeiro) {
                executarAcao(monstroJogador, selvagem, ataqueJogador);
                if (!selvagem.estaVivo()) break; 
                executarAcao(selvagem, monstroJogador, ataqueSelvagem);
            } else {
                executarAcao(selvagem, monstroJogador, ataqueSelvagem);
                if (!monstroJogador.estaVivo()) continue; 
                executarAcao(monstroJogador, selvagem, ataqueJogador);
            }
        }

        
        if (!jogador.temMonstrosVivos()) {
            System.out.println("Seu time foi derrotado...");
        } else if (!selvagem.estaVivo()) {
            System.out.println("Você venceu a batalha!");
            jogador.adicionarDinheiro(50);
            System.out.println("Você ganhou 50 moedas!");

            int expGanha = 20;
            for (Mons monstro : jogador.getTime()) {
                if (monstro.estaVivo()) {
                    monstro.receberExp(expGanha);
                }
            }
        }
    }

    private static void executarAcao(Mons atacante, Mons alvo, Ataques ataque) {
        if (ataque != null) {
            ataque.usarAtaque(atacante, alvo);
            System.out.println(atacante.getNome() + " usou " + ataque.getNome() + "!");
        } else {
            int danoBasico = 10 + new Random().nextInt(20); 
            alvo.receberDano(danoBasico);
            System.out.println(atacante.getNome() + " usou um ataque básico!");
        }
    }
}
