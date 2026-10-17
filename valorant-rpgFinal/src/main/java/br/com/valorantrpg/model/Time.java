package br.com.valorantrpg.model;

import java.util.ArrayList;

public class Time {
    private String nome;
    private ArrayList<Jogador> jogadores = new ArrayList<>();

    public Time(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    public boolean adicionarJogador(Jogador j) {
        if (jogadores.size() >= 5) {
            return false;
        }
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getAgente().getNome().equals(j.getAgente().getNome())) {
                return false;
            }
        }
        jogadores.add(j);
        return true;
    }

    public ArrayList<Jogador> getVivos() {
        ArrayList<Jogador> vivos = new ArrayList<>();
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).estaVivo()) {
                vivos.add(jogadores.get(i));
            }
        }
        return vivos;
    }

    public int contarVivos() {
        int vivos = 0;
        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).estaVivo()) {
                vivos++;
            }
        }
        return vivos;
    }

    public boolean todosMortos() {
        return contarVivos() == 0;
    }

}
