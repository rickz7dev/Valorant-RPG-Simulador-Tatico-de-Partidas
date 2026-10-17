package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Agente;
import br.com.valorantrpg.model.Habilidade;

import java.util.ArrayList;

public class CatalogoAgentes {
    private ArrayList<Agente> agentes = new ArrayList<>();

    public CatalogoAgentes() {
        Agente sage = new Agente("Sage", 3);
        sage.adicionarHabilidade(new Habilidade("Orbe de Barreira", "ESCUDO", "SI_MESMO", 40, 0, false, 400));
        sage.adicionarHabilidade(new Habilidade("Orbe de Cura", "CURA", "SI_MESMO", 40, 0, false, 200));

        Agente reyna = new Agente("Reyna", 8);
        reyna.adicionarHabilidade(new Habilidade("Poder Devastador", "BUFF", "SI_MESMO", 20, 3, false, 150));
        reyna.adicionarHabilidade(new Habilidade("Devorar", "CURA", "SI_MESMO", 30, 0, true, 0));

        Agente fenix = new Agente("Fenix", 7);
        fenix.adicionarHabilidade(new Habilidade("Queimar", "DANO_CONTINUO", "INIMIGO", 10, 3, false, 200));
        fenix.adicionarHabilidade(new Habilidade("Chama de Luz", "CEGUEIRA", "INIMIGO", 15, 2, false, 250));

        Agente breach = new Agente("Breach", 4);
        breach.adicionarHabilidade(new Habilidade("Clarão", "CEGUEIRA", "INIMIGO", 20, 2, false, 250));
        breach.adicionarHabilidade(new Habilidade("Pós-choque", "DANO", "INIMIGO", 60, 0, false, 200));

        Agente viper = new Agente("Viper", 5);
        viper.adicionarHabilidade(new Habilidade("Frasco de Veneno", "DANO_CONTINUO", "INIMIGO", 8, 3, false, 200));
        viper.adicionarHabilidade(new Habilidade("Mordida de Cobra", "VULNERABILIDADE", "INIMIGO", 25, 2, false, 150));

        adicionarAgente(sage);
        adicionarAgente(reyna);
        adicionarAgente(fenix);
        adicionarAgente(breach);
        adicionarAgente(viper);
    }

    public String listar() {
        String texto = "";
        for (int i = 0; i < agentes.size(); i++) {
            texto = texto + (i + 1) + " - " + agentes.get(i).toString() + "\n";
        }
        return texto;
    }

    public Agente buscarPorNome(String nome) {
        for (int i = 0; i < agentes.size(); i++) {
            if (agentes.get(i).getNome().equalsIgnoreCase(nome)) {
                return agentes.get(i);
            }
        }
        return null;
    }

    private void adicionarAgente(Agente a) {
        agentes.add(a);
    }

    public ArrayList<Agente> getAgentes() {
        return agentes;
    }

}
