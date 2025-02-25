import java.util.ArrayList;
import java.util.Random;

public class ListaMonstros {
    private static ArrayList<Mons> listaDeMonstros = new ArrayList<>();

    static {
        
        Ataques psico1 = new Ataques("Análise Profunda", Tipo.PSICOLOGIA, 80, 100, 10, "NORMAL");
        Ataques psico2 = new Ataques("Terapia Cognitiva", Tipo.PSICOLOGIA, 60, 90, 15, "CURA");

        
        Ataques comp1 = new Ataques("Algoritmo Fatal", Tipo.COMPUTACAO, 90, 95, 10, "NORMAL");
        Ataques comp2 = new Ataques("Erro Crítico", Tipo.COMPUTACAO, 70, 100, 20, "BUFF");

        
        Ataques socio1 = new Ataques("Revolução Social", Tipo.SOCIOLOGIA, 100, 85, 5, "NORMAL");
        Ataques socio2 = new Ataques("Movimento Coletivo", Tipo.SOCIOLOGIA, 75, 90, 10, "BUFF");

        
        Ataques fisio1 = new Ataques("Massagem Terapêutica", Tipo.FISIOTERAPIA, 120, 90, 5, "CURA");
        Ataques fisio2 = new Ataques("Alongamento Extremo", Tipo.FISIOTERAPIA, 150, 85, 3, "NORMAL");

        
        Ataques psic1 = new Ataques("Delírio Destrutivo", Tipo.PSICOTICO, 180, 80, 3, "NORMAL");
        Ataques psic2 = new Ataques("Ilusão Curativa", Tipo.PSICOTICO, 0, 100, 5, "CURA");

        
        Ataques eng1 = new Ataques("Cálculo Estrutural", Tipo.ENGENHARIA, 100, 90, 5, "NORMAL");
        Ataques eng2 = new Ataques("Reforço Metálico", Tipo.ENGENHARIA, 0, 100, 10, "BUFF");

        
        Ataques arte1 = new Ataques("Pincelada Mágica", Tipo.ARTE, 90, 85, 5, "NORMAL");
        Ataques arte2 = new Ataques("Inspiração Criativa", Tipo.ARTE, 0, 100, 10, "BUFF");

        
        Ataques vag1 = new Ataques("Vida na Rua", Tipo.VAGABUNDO, 150, 80, 3, "NORMAL");
        Ataques vag2 = new Ataques("Curativo Improvisado", Tipo.VAGABUNDO, 0, 100, 5, "CURA");

        
        Ataques saude1 = new Ataques("Primeiros Socorros", Tipo.SAUDE, 120, 90, 5, "NORMAL");
        Ataques saude2 = new Ataques("Auto-Cura", Tipo.SAUDE, 0, 100, 5, "CURA");

        
        listaDeMonstros.add(new Mons("Pedro", 800, 1, Tipo.PSICOLOGIA, criarLista(psico1, psico2), 70, 30, 40));
        listaDeMonstros.add(new Mons("Libs", 1000, 1, Tipo.COMPUTACAO, criarLista(comp1, comp2), 80, 35, 50));
        listaDeMonstros.add(new Mons("Mc", 900, 1, Tipo.SOCIOLOGIA, criarLista(socio1, socio2), 75, 30, 45));

        
        listaDeMonstros.add(new Mons("Voshio", 2000, 50, Tipo.FISIOTERAPIA, criarLista(fisio1, fisio2), 120, 80, 100));
        listaDeMonstros.add(new Mons("Mi Make", 2500, 50, Tipo.PSICOTICO, criarLista(psic1, psic2), 150, 100, 120));
        listaDeMonstros.add(new Mons("BrenKoh", 3000, 50, Tipo.VAGABUNDO, criarLista(vag1, saude1), 180, 120, 150));

        
        listaDeMonstros.add(new Mons("Engenius", 1200, 1, Tipo.ENGENHARIA, criarLista(eng1, eng2), 90, 70, 80));
        listaDeMonstros.add(new Mons("Artista", 1000, 1, Tipo.ARTE, criarLista(arte1, arte2), 80, 60, 100));
        listaDeMonstros.add(new Mons("Cientista Maluco", 1100, 1, Tipo.COMPUTACAO, criarLista(comp1, comp2), 85, 60, 90));
        listaDeMonstros.add(new Mons("Revolucionário", 1300, 1, Tipo.SOCIOLOGIA, criarLista(socio1, socio2), 90, 70, 80));
        listaDeMonstros.add(new Mons("Terapeuta Zen", 1000, 1, Tipo.PSICOLOGIA, criarLista(psico1, psico2), 75, 60, 100));
        listaDeMonstros.add(new Mons("FisioMaster", 1500, 1, Tipo.FISIOTERAPIA, criarLista(fisio1, fisio2), 100, 80, 70));
        listaDeMonstros.add(new Mons("Engenheiro Quântico", 1400, 1, Tipo.ENGENHARIA, criarLista(eng1, eng2), 110, 90, 60));
        listaDeMonstros.add(new Mons("Pintor Surrealista", 900, 1, Tipo.ARTE, criarLista(arte1, arte2), 80, 50, 120));
        listaDeMonstros.add(new Mons("Vagabundo Errante", 1000, 1, Tipo.VAGABUNDO, criarLista(vag1, vag2), 70, 40, 130));
        listaDeMonstros.add(new Mons("Enfermeiro Heroico", 1600, 1, Tipo.SAUDE, criarLista(saude1, saude2), 90, 100, 50));
        listaDeMonstros.add(new Mons("Programador Nocturno", 1200, 1, Tipo.COMPUTACAO, criarLista(comp1, comp2), 80, 50, 110));
        listaDeMonstros.add(new Mons("Artista de Rua", 900, 1, Tipo.VAGABUNDO, criarLista(vag1, vag2), 75, 45, 140));
    }

    
    private static ArrayList<Ataques> criarLista(Ataques... ataques) {
        ArrayList<Ataques> lista = new ArrayList<>();
        for (Ataques ataque : ataques) {
            lista.add(ataque);
        }
        return lista;
    }

    public static Mons encontrarMonstro() {
        Random random = new Random();
        if (listaDeMonstros.isEmpty()) {
            System.out.println("Não há monstros disponíveis!");
            return null;
        }
        return listaDeMonstros.get(random.nextInt(listaDeMonstros.size()));
    }
}
