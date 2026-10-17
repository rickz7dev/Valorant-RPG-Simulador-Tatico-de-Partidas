package br.com.valorantrpg.model;

public class Escudo {
    private String tipo;
    private int preco;
    private double protecao;

    public Escudo(String tipo, int preco, double protecao) {
        this.tipo = tipo;
        this.preco = preco;
        this.protecao = protecao;
    }

    public String getTipo() {
        return tipo;
    }

    public int getPreco() {
        return preco;
    }

    public double getProtecao() {
        return protecao;
    }

    @Override
    public String toString() {
        return String.format("%s | Preço: %d | Proteção: %.0f", tipo, preco, protecao);
    }
    
}
