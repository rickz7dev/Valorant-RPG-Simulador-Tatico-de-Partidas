package br.com.valorantrpg.model;

import java.util.ArrayList;

public class Agente {
    private String nome;
    private double velocidade;
    private ArrayList<Habilidade> listaDeHabilidades = new ArrayList<>();

    public Agente(String nome, double velocidade) {
        this.nome = nome;
        this.velocidade = velocidade;
    }

    public void adicionarHabilidade(Habilidade h) {
        listaDeHabilidades.add(h);
    }
    
    public String getNome() {
        return nome;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public ArrayList<Habilidade> getHabilidades() {
        return listaDeHabilidades;
    }

    @Override
    public String toString() {
        String habilidades = "";
        for (int i = 0; i < listaDeHabilidades.size(); i++) {
            habilidades += listaDeHabilidades.get(i).getNome();
            if (i < listaDeHabilidades.size() - 1) {
                habilidades += ", ";
            }
        }
        return String.format("%s | Velocidade: %.0f | Habilidades: %s", nome, velocidade, habilidades);
    }

}
