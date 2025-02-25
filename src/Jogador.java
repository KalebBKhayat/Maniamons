import java.io.Serializable;
import java.util.ArrayList;


public class Jogador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private ArrayList<Mons> time; 
    private Inventario inventario;
    private int dinheiro;

    public Jogador(String nome) {
        this.nome = nome;
        this.time = new ArrayList<>(); 
        this.inventario = new Inventario();
        this.dinheiro = 100; 
        inventario.adicionarItem("Garrafa Mágica", 3);
        inventario.adicionarItem("Poção de Cura", 2);
    }

    public Mons getMonstroPorNome(String nomeMonstro) {
        for (Mons monstro : time) {
            if (monstro.getNome().equalsIgnoreCase(nomeMonstro)) {
                return monstro;
            }
        }
        return null; // Retorna null se o monstro não for encontrado
    }
    public ArrayList<Mons> getTime() {
        return time;
    }

    public void listarMonstros() {
        if (time.isEmpty()) {
            System.out.println("Seu time está vazio!");
        } else {
            System.out.println("Seus monstros:");
            for (Mons mon : time) {
                System.out.println("- " + mon.getNome() + " (Vida: " + mon.getVida() + ")");
            }
        }
    }

    public void adicionarMonstro(Mons monstro) {
        if (time.size() < 6) {
            time.add(monstro);
            System.out.println(monstro.getNome() + " foi adicionado ao time!");
        } else {
            System.out.println("Seu time já está cheio!");
        }
    }

    public boolean capturarMonstro(Mons monstro) {
        if (inventario.getQuantidade("Garrafa Mágica") > 0) {
            if (time.size() < 6) {
                time.add(monstro);
                inventario.usarItem("Garrafa Mágica");
                System.out.println("Você capturou " + monstro.getNome() + "!");
                return true;
            } else {
                System.out.println("Seu time já está cheio! Libere um monstro antes.");
            }
        } else {
            System.out.println("Você não tem Garrafas Mágicas!");
        }
        return false;
    }

    public void curarTodos() {
        for (Mons mon : time) {
            mon.curar();
        }
        System.out.println("Todos os seus monstros foram curados!");
    }

    public void adicionarDinheiro(int valor) {
        this.dinheiro += valor;
    }

    public int getDinheiro() {
        return dinheiro;
    }

    public boolean gastarDinheiro(int valor) {
        if (this.dinheiro >= valor) {
            this.dinheiro -= valor;
            System.out.println("Você gastou " + valor + " moedas. Saldo restante: " + this.dinheiro);
            return true;
        } else {
            System.out.println("Você não tem dinheiro suficiente! Saldo atual: " + this.dinheiro);
            return false;
        }
    }
    public boolean temMonstrosVivos() {
        for (Mons monstro : time) {
            if (monstro.estaVivo()) {
                return true; 
            }
        }
        return false; 
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTime(ArrayList<Mons> time) {
        this.time = time;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public void setDinheiro(int dinheiro) {
        this.dinheiro = dinheiro;
    }

    public String getNome() {
        return nome;
    }
}
