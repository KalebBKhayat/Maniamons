import java.util.ArrayList;

public class Mons {
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setExpAtual(int expAtual) {
        this.expAtual = expAtual;
    }

    public void setExpParaProximoNivel(int expParaProximoNivel) {
        this.expParaProximoNivel = expParaProximoNivel;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public void setAtaques(ArrayList<Ataques> ataques) {
        this.ataques = ataques;
    }

    public void setAtaqueBase(int ataqueBase) {
        this.ataqueBase = ataqueBase;
    }

    public void setDefesaBase(int defesaBase) {
        this.defesaBase = defesaBase;
    }

    public void setVelocidadeBase(int velocidadeBase) {
        this.velocidadeBase = velocidadeBase;
    }

    private String nome;
    private int vida;
    private int vidaMaxima;
    private int nivel;
    private int expAtual;
    private int expParaProximoNivel;
    private Tipo tipo;
    private ArrayList<Ataques> ataques;
    private int ataqueBase;
    private int defesaBase;
    private int velocidadeBase;

    public Mons(String nome, int vidaMaxima, int nivelInicial, Tipo tipo, ArrayList<Ataques> ataques,
                int ataqueBase, int defesaBase, int velocidadeBase) {
        this.nome = nome;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.nivel = nivelInicial;
        this.expAtual = 0;
        this.expParaProximoNivel = 10; 
        this.tipo = tipo;
        this.ataques = ataques != null ? ataques : new ArrayList<>();
        this.ataqueBase = ataqueBase;
        this.defesaBase = defesaBase;
        this.velocidadeBase = velocidadeBase;
    }

    public void receberExp(int expGanha) {
        System.out.println(this.nome + " ganhou " + expGanha + " pontos de experiência!");
        this.expAtual += expGanha;

        while (this.expAtual >= this.expParaProximoNivel) {
            this.expAtual -= this.expParaProximoNivel;
            this.nivel++;
            this.vidaMaxima += 10; 
            this.vida = this.vidaMaxima; 
            this.ataqueBase += 5; 
            this.defesaBase += 3; 
            this.velocidadeBase += 2; 
            this.expParaProximoNivel = (int) (this.expParaProximoNivel * 1.5); 
            System.out.println(this.nome + " subiu para o nível " + this.nivel + "!");
            System.out.println("Novos Status:");
            System.out.println("- Vida Máxima: " + this.vidaMaxima);
            System.out.println("- Ataque: " + this.ataqueBase);
            System.out.println("- Defesa: " + this.defesaBase);
            System.out.println("- Velocidade: " + this.velocidadeBase);

            
            if (this.nivel == 5) {
                evoluir();
            }
        }
    }

    private void evoluir() {
        String nomeAntigo = this.nome;
        switch (nomeAntigo) {
            case "Mc":
                this.nome = "Mc Lara";
                this.vidaMaxima += 50;
                this.vida = this.vidaMaxima;
                this.ataqueBase += 10;
                this.defesaBase += 5;
                this.velocidadeBase += 3;
                System.out.println(nomeAntigo + " evoluiu para " + this.nome + "!");
                break;
            case "Libs":
                this.nome = "McLaren";
                this.vidaMaxima += 60;
                this.vida = this.vidaMaxima;
                this.ataqueBase += 15;
                this.defesaBase += 8;
                this.velocidadeBase += 5;
                System.out.println(nomeAntigo + " evoluiu para " + this.nome + "!");
                break;
            case "Pedro":
                this.nome = "P.S.C.";
                this.vidaMaxima += 70;
                this.vida = this.vidaMaxima;
                this.ataqueBase += 20;
                this.defesaBase += 10;
                this.velocidadeBase += 7;
                System.out.println(nomeAntigo + " evoluiu para " + this.nome + "!");
                break;
            default:
                System.out.println("Este monstro não pode evoluir.");
                break;
        }
        System.out.println("Novos Status após a evolução:");
        System.out.println("- Vida Máxima: " + this.vidaMaxima);
        System.out.println("- Ataque: " + this.ataqueBase);
        System.out.println("- Defesa: " + this.defesaBase);
        System.out.println("- Velocidade: " + this.velocidadeBase);
    }

    public void curar() {
        this.vida = this.vidaMaxima;
        System.out.println(this.nome + " foi curado! Vida total restaurada.");
    }

    public void receberDano(int dano) {
        this.vida -= dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
        System.out.println(this.nome + " recebeu " + dano + " de dano!");
    }

    public void buffStatus() {
        this.ataqueBase += 5;
        this.defesaBase += 3;
        this.velocidadeBase += 2;
        System.out.println(this.nome + " teve seus status aumentados!");
    }
    

    public void cura(int quantidade) {
        this.vida += quantidade;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
        System.out.println(this.nome + " recuperou " + quantidade + " de vida!");
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }

    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExpAtual() {
        return expAtual;
    }

    public int getExpParaProximoNivel() {
        return expParaProximoNivel;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public ArrayList<Ataques> getAtaques() {
        return ataques;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getDefesaBase() {
        return defesaBase;
    }

    public int getVelocidadeBase() {
        return velocidadeBase;
    }

    public void listarAtaques() {
        if (ataques.isEmpty()) {
            System.out.println(this.nome + " não possui ataques!");
        } else {
            System.out.println("Ataques de " + this.nome + ":");
            for (int i = 0; i < ataques.size(); i++) {
                Ataques ataque = ataques.get(i);
                System.out.println((i + 1) + " - " + ataque.getNome() + " (Poder: " + ataque.getPoder() + ", PP: " + ataque.getPp() + ")");
            }
        }
    }

    public Ataques escolherAtaque(int indice) {
        if (indice >= 0 && indice < ataques.size()) {
            return ataques.get(indice);
        }
        System.out.println("Ataque inválido!");
        return null;
    }
}

