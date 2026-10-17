package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.model.Time;

import java.util.ArrayList;

public class Relatorio {
    private ArrayList<Jogador> jogadores = new ArrayList<>();

    public Relatorio(Time timeA, Time timeB) {
        for (int i = 0; i < timeA.getJogadores().size(); i++) {
            jogadores.add(timeA.getJogadores().get(i));
        }
        for (int i = 0; i < timeB.getJogadores().size(); i++) {
            jogadores.add(timeB.getJogadores().get(i));
        }
    }

    public int totalDeAbates() {
        int total = 0;
        for (int i = 0; i < jogadores.size(); i++) {
            total = total + jogadores.get(i).getKills();
        }
        return total;
    }

    public double mediaDeAbates() {
        return (double) totalDeAbates() / jogadores.size();
    }

    public Jogador maiorAbatedor() {
        Jogador melhor = jogadores.get(0);
        for (int i = 1; i < jogadores.size(); i++) {
            if (jogadores.get(i).getKills() > melhor.getKills()) {
                melhor = jogadores.get(i);
            }
        }
        return melhor;
    }

    public Jogador menorAbatedor() {
        Jogador pior = jogadores.get(0);
        for (int i = 1; i < jogadores.size(); i++) {
            if (jogadores.get(i).getKills() < pior.getKills()) {
                pior = jogadores.get(i);
            }
        }
        return pior;
    }

    public double kd(Jogador j) {
        if (j.getMortes() == 0) {
            return j.getKills();
        }
        return (double) j.getKills() / j.getMortes();
    }

    public ArrayList<Jogador> ranking() {
        ArrayList<Jogador> r = new ArrayList<>();
        for (int i = 0; i < jogadores.size(); i++) {
            r.add(jogadores.get(i));
        }
        for (int i = 0; i < r.size() - 1; i++) {
            for (int k = 0; k < r.size() - 1 - i; k++) {
                if (r.get(k).getKills() < r.get(k + 1).getKills()) {
                    Jogador troca = r.get(k);
                    r.set(k, r.get(k + 1));
                    r.set(k + 1, troca);
                }
            }
        }
        return r;
    }

    public String gerar(Jogador usuario) {
        String texto = "\n===== RELATÓRIO DA PARTIDA =====\n";
        texto = texto + "Total de abates: " + totalDeAbates() + "\n";
        texto = texto + String.format("Média de abates por jogador: %.2f\n", mediaDeAbates());
        texto = texto + "Maior abatedor: " + maiorAbatedor().getNome() + " (" + maiorAbatedor().getKills() + ")\n";
        texto = texto + "Menor abatedor: " + menorAbatedor().getNome() + " (" + menorAbatedor().getKills() + ")\n";
        texto = texto + String.format("Seu K/D: %.2f\n", kd(usuario));
        texto = texto + "\nRanking por abates:\n";
        ArrayList<Jogador> r = ranking();
        for (int i = 0; i < r.size(); i++) {
            Jogador j = r.get(i);
            texto = texto + String.format("%d - %s | Abates: %d | Mortes: %d | K/D: %.2f\n", i + 1, j.getNome(),
                    j.getKills(), j.getMortes(), kd(j));
        }
        return texto;
    }
}