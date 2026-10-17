package br.com.valorantrpg.model;

public class Arma {
    private String nome;
    private int preco;
    private int capacidadeMunicao;
    private double dano;
    private double danoCabeca;
    private double chanceCabeca;
    private double chanceAcerto;

    public Arma(String nome, int preco, int capacidadeMunicao, double dano, double danoCabeca, double chanceCabeca,
            double chanceAcerto) {
        this.nome = nome;
        this.preco = preco;
        this.capacidadeMunicao = capacidadeMunicao;
        this.dano = dano;
        this.danoCabeca = danoCabeca;
        this.chanceCabeca = chanceCabeca;
        this.chanceAcerto = chanceAcerto;
    }

    public String getNome() {
        return nome;
    }

    public int getPreco() {
        return preco;
    }

    public double getDano() {
        return dano;
    }

    public double getDanoCabeca() {
        return danoCabeca;
    }

    public double getChanceCabeca() {
        return chanceCabeca;
    }

    public int getCapacidadeMunicao() {
        return capacidadeMunicao;
    }

    public double getChanceAcerto() {
        return chanceAcerto;
    }

    public void setPreco(int preco) {
        if (preco >= 0) {
            this.preco = preco;
        }
    }

    public double calcularDanoDoTiro(double reducaoCabeca) {
        if (Math.random() >= chanceAcerto) {
            return 0;
        }
        double chance = chanceCabeca * (1 - reducaoCabeca / 100);
        if (Math.random() < chance) {
            return danoCabeca;
        }
        return dano;
    }

    public double calcularDanoDoTiro() {
        return calcularDanoDoTiro(0);
    }

    @Override
    public String toString() {
        return String.format("%s | Preço: %d | Dano: %.0f | Pente: %d | Precisão: %.0f%%", nome, preco, dano,
                capacidadeMunicao, chanceAcerto * 100);
    }

}
