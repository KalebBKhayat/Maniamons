public class Ataques {
    private String nome;
    private Tipo tipo;
    private int poder;
    private int precisao;
    private int pp;
    private String efeito; 

    public Ataques(String nome, Tipo tipo, int poder, int precisao, int pp, String efeito) {
        this.nome = nome;
        this.tipo = tipo;
        this.poder = poder;
        this.precisao = precisao;
        this.pp = pp;
        this.efeito = efeito;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getPoder() {
        return poder;
    }

    public int getPrecisao() {
        return precisao;
    }

    public int getPp() {
        return pp;
    }

    public String getEfeito() {
        return efeito;
    }

    public void usarAtaque(Mons usuario, Mons alvo) {
        if (pp > 0) {
            pp--;
            System.out.println(usuario.getNome() + " usou " + nome + "!");
            if ("BUFF".equals(efeito)) {
                usuario.buffStatus();
            } else if ("CURA".equals(efeito)) {
                usuario.cura(poder);
            } else {
              
                double danoBase = poder * (usuario.getAtaqueBase() / 10.0); 
                double efetividade = tipo.getEfetividadeContra(alvo.getTipo());
                double danoFinal = (danoBase * 1.7) / (1 + (double) alvo.getDefesaBase() / 100); 
                danoFinal *= efetividade;

                alvo.receberDano((int) danoFinal);
            }
        } else {
            System.out.println("O ataque " + nome + " não tem mais PP!");
        }
    }
}
