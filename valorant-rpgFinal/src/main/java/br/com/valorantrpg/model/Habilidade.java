package br.com.valorantrpg.model;

public class Habilidade {
    private String nome;
    private String tipo;
    private String alvo;
    private double valor;
    private int duracao;
    private boolean passiva;
    private int custo;

    public Habilidade(String nome, String tipo, String alvo, double valor, int duracao, boolean passiva, int custo) {
        this.nome = nome;
        this.tipo = tipo;
        this.alvo = alvo;
        this.valor = valor;
        this.duracao = duracao;
        this.passiva = passiva;
        this.custo = custo;
    }

    public boolean aplicar(Jogador usuario, Jogador adversario) {
        Jogador destino = adversario;
        if (alvo.equals("SI_MESMO")) {
            destino = usuario;
        }
        if (tipo.equals("CURA")) {
            destino.curar(valor);
        } else if (tipo.equals("ESCUDO")) {
            destino.ganharEscudo(valor);
        } else if (tipo.equals("DANO")) {
            destino.receberDano(valor);
        } else if (tipo.equals("BUFF") || tipo.equals("DANO_CONTINUO")
                || tipo.equals("CEGUEIRA") || tipo.equals("VULNERABILIDADE")) {
            destino.aplicarEfeito(tipo, valor, duracao);
        } else {
            return false;
        }
        return true;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public String getAlvo() {
        return alvo;
    }

    public double getValor() {
        return valor;
    }

    public int getDuracao() {
        return duracao;
    }

    public boolean isPassiva() {
        return passiva;
    }

    public int getCusto() {
        return custo;
    }

    @Override
    public String toString() {
        return String.format("%s | Tipo: %s | Alvo: %s | Valor: %.0f | Duração: %d | Passiva: %b | Custo: %d", nome,
                tipo, alvo, valor, duracao, passiva, custo);
    }

}
