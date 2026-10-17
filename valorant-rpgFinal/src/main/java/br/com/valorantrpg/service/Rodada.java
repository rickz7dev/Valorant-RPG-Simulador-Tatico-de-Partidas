package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.model.Time;

import java.util.ArrayList;

public class Rodada {
    private int numeroDaRodada;
    private Time timeA, timeB;
    private Loja loja;
    private Jogador jogadorP1;
    private int premioVitoria = 3000;
    private int premioDerrota = 1900;
    private int premioAbate = 200;

    public Rodada(int numeroDaRodada, Time timeA, Time timeB, Loja loja, Jogador jogadorP1) {
        this.numeroDaRodada = numeroDaRodada;
        this.timeA = timeA;
        this.timeB = timeB;
        this.loja = loja;
        this.jogadorP1 = jogadorP1;
    }

    public void iniciar() {
        for (int i = 0; i < timeA.getJogadores().size(); i++) {
            Jogador j = timeA.getJogadores().get(i);
            j.resetarVida();
            j.resetarKillsRodada();
            j.limparEfeitos();
        }
        for (int i = 0; i < timeB.getJogadores().size(); i++) {
            Jogador j = timeB.getJogadores().get(i);
            j.resetarVida();
            j.resetarKillsRodada();
            j.limparEfeitos();
        }
    }

    public Time vencedor() {
        if (timeA.todosMortos()) {
            return timeB;
        } else if (timeB.todosMortos()) {
            return timeA;
        }
        return null;
    }

    public void pagar() {
        Time vencedor = vencedor();

        if (vencedor == null) {
            return;
        }
        for (int i = 0; i < timeA.getJogadores().size(); i++) {
            Jogador j = timeA.getJogadores().get(i);
            if (vencedor == timeA) {
                j.ganharCreditos(premioVitoria + (j.getKillsRodada()) * premioAbate);
            } else {
                j.ganharCreditos(premioDerrota + (j.getKillsRodada()) * premioAbate);
            }
        }
        for (int i = 0; i < timeB.getJogadores().size(); i++) {
            Jogador j = timeB.getJogadores().get(i);
            if (vencedor == timeB) {
                j.ganharCreditos(premioVitoria + (j.getKillsRodada()) * premioAbate);
            } else {
                j.ganharCreditos(premioDerrota + (j.getKillsRodada()) * premioAbate);
            }
        }
    }

    public ArrayList<Duelo> montarConfrontos() {
        ArrayList<Jogador> vivosA = timeA.getVivos();
        ArrayList<Jogador> vivosB = timeB.getVivos();
        int pares = vivosA.size();
        if (vivosB.size() < pares) {
            pares = vivosB.size();
        }
        ArrayList<Duelo> duelos = new ArrayList<>();
        for (int i = 0; i < pares; i++) {
            duelos.add(new Duelo(vivosA.get(i), vivosB.get(i)));
        }
        return duelos;
    }

    public void comprasDosBots() {
        for (int i = 0; i < timeA.getJogadores().size(); i++) {
            Jogador j = timeA.getJogadores().get(i);
            if (j != jogadorP1) {
                loja.compraAutomatica(j);
            }
        }
        for (int i = 0; i < timeB.getJogadores().size(); i++) {
            loja.compraAutomatica(timeB.getJogadores().get(i));
        }
    }

    public int getNumeroDaRodada() {
        return numeroDaRodada;
    }
}
