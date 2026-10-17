package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Escudo;
import br.com.valorantrpg.model.Habilidade;
import br.com.valorantrpg.model.Jogador;

import java.util.ArrayList;

public class Loja {
    private Arsenal arsenal = new Arsenal();
    private ArrayList<Escudo> escudos = new ArrayList<>();

    public Loja() {
        escudos.add(new Escudo("Escudo Pesado", 1000, 50));
        escudos.add(new Escudo("Escudo Leve", 400, 25));
    }

    public boolean comprarArma(Jogador j, Arma a) {
        if (j.getArma().getNome().equals(a.getNome())) {
            return false;
        } else if (j.gastarCreditos(a.getPreco())) {
            j.equiparArma(a);
            return true;
        } else
            return false;
    }

    public boolean comprarHabilidade(Jogador j, Habilidade h) {
        if (h.isPassiva()) {
            return false;
        }

        boolean pertence = false;
        for (int i = 0; i < j.getAgente().getHabilidades().size(); i++) {
            if (j.getAgente().getHabilidades().get(i).getNome().equals(h.getNome())) {
                pertence = true;
            }
        }
        if (!pertence) {
            return false;
        }

        for (int i = 0; i < j.getHabilidades().size(); i++) {
            if (j.getHabilidades().get(i).getNome().equals(h.getNome())) {
                return false;
            }
        }

        if (j.gastarCreditos(h.getCusto())) {
            j.adicionarHabilidade(h);
            return true;
        }
        return false;
    }

    public boolean comprarEscudo(Jogador j, Escudo e) {
        if (j.getEscudo() >= e.getProtecao()) {
            return false;
        } else if (j.gastarCreditos(e.getPreco())) {
            j.equiparEscudo(e);
            return true;
        } else
            return false;
    }

    public String compraAutomatica(Jogador bot) {
        String itensComprados = "";

        for (int i = 0; i < escudos.size(); i++) {
            if (comprarEscudo(bot, escudos.get(i))) {
                itensComprados = escudos.get(i).getTipo();
                break;
            }
        }

        ArrayList<Arma> armas = arsenal.getArmas();
        Arma melhor = bot.getArma();

        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i).getPreco() <= bot.getCreditos() && armas.get(i).getPreco() > melhor.getPreco()) {
                melhor = armas.get(i);
            }
        }

        if (melhor != bot.getArma() && comprarArma(bot, melhor)) {
            if (!itensComprados.equals("")) {
                itensComprados = itensComprados + " e ";
            }
            itensComprados = itensComprados + melhor.getNome();
        }

        for (int i = 0; i < bot.getAgente().getHabilidades().size(); i++) {
            Habilidade h = bot.getAgente().getHabilidades().get(i);
            if (comprarHabilidade(bot, h)) {
                if (!itensComprados.equals("")) {
                    itensComprados = itensComprados + ", ";
                }
                itensComprados = itensComprados + h.getNome();
            }
        }
        
        if (itensComprados.equals("")) {
            return bot.getNome() + " não comprou nada";
        }

        return bot.getNome() + " comprou " + itensComprados;
    }

    public Arsenal getArsenal() {
        return arsenal;
    }

    public ArrayList<Escudo> getEscudos() {
        return escudos;
    }

    public String listarEscudos() {
        String texto = "";
        for (int i = 0; i < escudos.size(); i++) {
            texto = texto + (i + 1) + " - " + escudos.get(i).toString() + "\n";
        }
        return texto;
    }

}
