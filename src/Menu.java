import java.util.Scanner;

public class Menu {
    public static void exibirMenu(Jogador jogador) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Procurar Batalha");
            System.out.println("2 - Loja");
            System.out.println("3 - Curar Monstros");
            System.out.println("4 - Mostrar Inventário");
            System.out.println("5 - Sair");
            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    
                    Mons monstroSelvagem = ListaMonstros.encontrarMonstro();
                    if (monstroSelvagem != null) {
                        Batalha.iniciarBatalha(jogador, monstroSelvagem);
                    } else {
                        System.out.println("Nenhum monstro encontrado!");
                    }
                    break;
                case 2:
                    Loja.abrirLoja(jogador);
                    break;
                case 3:
                    jogador.curarTodos();
                    break;
                case 4:
                    jogador.getInventario().listarItens();
                    break;
                case 5:
                    System.out.println("Saindo do jogo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}

