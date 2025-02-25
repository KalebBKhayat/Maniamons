import java.util.HashMap;

public class Inventario {
   
    private HashMap<String, Integer> itens = new HashMap<>();

    
    public void adicionarItem(String item, int quantidade) {
        
        itens.put(item, itens.getOrDefault(item, 0) + quantidade);
        System.out.println(quantidade + " " + item + "(s) adicionado(s) ao inventário.");
    }

   
    public boolean usarItem(String item) {
        if (itens.getOrDefault(item, 0) > 0) {
            
            itens.put(item, itens.get(item) - 1);
            System.out.println("Você usou 1 " + item + ".");
            return true;
        } else {
            System.out.println("Você não tem " + item + " no inventário!");
            return false;
        }
    }

   
    public int getQuantidade(String item) {
        return itens.getOrDefault(item, 0);
    }

    
    public HashMap<String, Integer> listarItens() {
        return itens;
    }

}