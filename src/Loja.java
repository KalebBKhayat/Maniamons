import java.util.Scanner;

public class Loja {
    public static void abrirLoja(Jogador jogador) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- LOJA ---");
            System.out.println("Dinheiro: " + jogador.getDinheiro());
            System.out.println("1 - Comprar Garrafa Mágica (50 moedas)");
            System.out.println("2 - Comprar Poção de Cura (30 moedas)");
            System.out.println("3 - Sair");

            int escolha = scanner.nextInt();
            if (escolha == 1) {
                if (jogador.gastarDinheiro(50)) {
                    jogador.getInventario().adicionarItem("Garrafa Mágica", 1);
                    System.out.println("Você comprou uma Garrafa Mágica!");
                }
            } else if (escolha == 2) {
                if (jogador.gastarDinheiro(30)) {
                    jogador.getInventario().adicionarItem("Poção de Cura", 1);
                    System.out.println("Você comprou uma Poção de Cura!");
                }
            } else if (escolha == 3) {
                System.out.println("Saindo da loja...");
                break;
            } else {
                System.out.println("Opção inválida!");
            }
        }
    }
}

