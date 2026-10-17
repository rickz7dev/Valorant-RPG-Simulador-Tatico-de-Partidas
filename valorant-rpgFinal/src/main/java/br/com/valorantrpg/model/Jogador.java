package br.com.valorantrpg.model;

import java.util.ArrayList;

public class Jogador {
    private String nome;
    private Agente agente;
    private Arma arma;
    private ArrayList<Habilidade> habilidades = new ArrayList<>();
    private double vida;
    private double escudo;
    private int creditos;
    private int municaoAtual;
    private int kills;
    private int mortes;
    private Arma armaPadrao;
    private int killsRodada;
    private double bonusDano;
    private int turnosBonusDano;
    private double danoContinuo;
    private int turnosDanoContinuo;
    private double reducaoPrecisao;
    private int turnosReducaoPrecisao;
    private double vulnerabilidade;
    private int turnosVulnerabilidade;
    private boolean ultimoTiroCabeca;

    public Jogador(String nome, Agente agente, Arma arma) {
        this.nome = nome;
        this.agente = agente;
        this.arma = arma;
        vida = 100;
        creditos = 800;
        municaoAtual = arma.getCapacidadeMunicao();
        this.armaPadrao = arma;
    }

    public double atirar() {
        ultimoTiroCabeca = false;
        if (municaoAtual > 0) {
            municaoAtual = municaoAtual - 1;
            double reducao = 0;
            if (turnosReducaoPrecisao > 0) {
                reducao = reducaoPrecisao;
            }
            double dano = arma.calcularDanoDoTiro(reducao);
            ultimoTiroCabeca = dano == arma.getDanoCabeca();
            if (turnosBonusDano > 0) {
                dano = dano * (1 + bonusDano / 100);
            }
            return dano;
        }
        return 0;
    }

    public boolean foiTiroNaCabeca() {
        return ultimoTiroCabeca;
    }

    public boolean recarregar() {
        if (municaoAtual < arma.getCapacidadeMunicao()) {
            municaoAtual = arma.getCapacidadeMunicao();
            return true;
        }
        return false;
    }

    public boolean estaVivo() {
        if (vida > 0) {
            return true;
        } else
            return false;
    }

    public void resetarVida() {
        vida = 100;
    }

    public void morrer() {
        vida = 0;
        escudo = 0;
        habilidades.clear();
        arma = armaPadrao;
        municaoAtual = arma.getCapacidadeMunicao();
        limparEfeitos();
        mortes++;
    }

    public void receberDano(double dano) {
        if (turnosVulnerabilidade > 0) {
            dano = dano * (1 + vulnerabilidade / 100);
        }
        if (dano <= escudo) {
            escudo = escudo - dano;
        } else {
            vida = vida - (dano - escudo);
            escudo = 0;
        }
        if (vida < 0) {
            vida = 0;
        }

    }

    public void registrarKill() {
        kills++;
        killsRodada++;
    }

    public void resetarKillsRodada() {
        killsRodada = 0;
    }

    public void curar(double valor) {
        if (!estaVivo()) {
            return;
        }
        vida = vida + valor;
        if (vida > 100) {
            vida = 100;
        }
    }

    public void ganharEscudo(double valor) {
        escudo = escudo + valor;
        if (escudo > 50) {
            escudo = 50;
        }
    }

    public void consumirHabilidade(Habilidade h) {
        habilidades.remove(h);
    }

    public boolean gastarCreditos(int valor) {
        if (creditos >= valor) {
            creditos = creditos - valor;
            return true;
        } else
            return false;
    }

    public void ganharCreditos(int valor) {
        creditos = creditos + valor;
    }

    public void equiparArma(Arma novaArma) {
        arma = novaArma;
        municaoAtual = arma.getCapacidadeMunicao();
    }

    public void equiparEscudo(Escudo novoEscudo) {
        escudo = novoEscudo.getProtecao();
    }

    public void adicionarHabilidade(Habilidade h) {
        habilidades.add(h);
    }

    public void aplicarEfeito(String tipo, double valor, int duracao) {
        if (tipo.equals("BUFF")) {
            bonusDano = valor;
            turnosBonusDano = duracao;
        } else if (tipo.equals("DANO_CONTINUO")) {
            danoContinuo = valor;
            turnosDanoContinuo = duracao;
        } else if (tipo.equals("CEGUEIRA")) {
            reducaoPrecisao = valor;
            turnosReducaoPrecisao = duracao;
        } else if (tipo.equals("VULNERABILIDADE")) {
            vulnerabilidade = valor;
            turnosVulnerabilidade = duracao;
        }
    }

    public String processarEfeitos() {
        String texto = "";
        if (turnosDanoContinuo > 0) {
            receberDano(danoContinuo);
            texto = agente.getNome() + " sofreu " + String.format("%.0f", danoContinuo) + " de dano contínuo";
            turnosDanoContinuo--;
            if (turnosDanoContinuo == 0) {
                danoContinuo = 0;
            }
        }
        if (turnosBonusDano > 0) {
            turnosBonusDano--;
            if (turnosBonusDano == 0) {
                bonusDano = 0;
            }
        }
        if (turnosReducaoPrecisao > 0) {
            turnosReducaoPrecisao--;
            if (turnosReducaoPrecisao == 0) {
                reducaoPrecisao = 0;
            }
        }
        if (turnosVulnerabilidade > 0) {
            turnosVulnerabilidade--;
            if (turnosVulnerabilidade == 0) {
                vulnerabilidade = 0;
            }
        }
        return texto;
    }

    public void limparEfeitos() {
        bonusDano = 0;
        turnosBonusDano = 0;
        danoContinuo = 0;
        turnosDanoContinuo = 0;
        reducaoPrecisao = 0;
        turnosReducaoPrecisao = 0;
        vulnerabilidade = 0;
        turnosVulnerabilidade = 0;
    }

    public String getNome() {
        return nome;
    }

    public Agente getAgente() {
        return agente;
    }

    public Arma getArma() {
        return arma;
    }

    public ArrayList<Habilidade> getHabilidades() {
        return habilidades;
    }

    public double getVida() {
        return vida;
    }

    public double getEscudo() {
        return escudo;
    }

    public int getCreditos() {
        return creditos;
    }

    public int getMunicaoAtual() {
        return municaoAtual;
    }

    public int getKills() {
        return kills;
    }

    public int getMortes() {
        return mortes;
    }

    public Arma getArmaPadrao() {
        return armaPadrao;
    }

    public int getKillsRodada() {
        return killsRodada;
    }

}
