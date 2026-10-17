package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Agente;
import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.model.Time;

public class Partida {
    private Jogador usuario;
    private Time timeUsuario;
    private Time timeAdversario;
    private Loja loja;
    private int rodadaAtual = 0;
    private int placarUsuario = 0;
    private int placarAdversario = 0;
    private int rodadasParaVencer = 5;

    public Partida(String nomeUsuario, CatalogoAgentes catalogo, Agente agenteEscolhido, Loja loja) {
        this.loja = loja;
        Arma classic = loja.getArsenal().buscarPorNome("Classic");
        timeUsuario = new Time("Seu Time");
        timeAdversario = new Time("Time Adversário");

        usuario = new Jogador(nomeUsuario, agenteEscolhido, classic);
        timeUsuario.adicionarJogador(usuario);

        for (int i = 0; i < catalogo.getAgentes().size(); i++) {
            Agente a = catalogo.getAgentes().get(i);
            if (!a.getNome().equals(agenteEscolhido.getNome())) {
                timeUsuario.adicionarJogador(new Jogador("Aliado " + a.getNome(), a, classic));
            }
            timeAdversario.adicionarJogador(new Jogador("Inimigo " + a.getNome(), a, classic));
        }
    }

    public Rodada novaRodada() {
        rodadaAtual++;
        return new Rodada(rodadaAtual, timeUsuario, timeAdversario, loja, usuario);
    }

    public void registrarResultado(Rodada r) {
        Time vencedor = r.vencedor();
        if (vencedor == null) {
            return;
        }
        if (vencedor == timeUsuario) {
            placarUsuario++;
        } else {
            placarAdversario++;
        }
    }

    public boolean terminou() {
        return placarUsuario >= rodadasParaVencer || placarAdversario >= rodadasParaVencer;
    }

    public Time vencedorDaPartida() {
        if (placarUsuario >= rodadasParaVencer) {
            return timeUsuario;
        }
        if (placarAdversario >= rodadasParaVencer) {
            return timeAdversario;
        }
        return null;
    }

    public String getPlacar() {
        return timeUsuario.getNome() + " " + placarUsuario + " x " + placarAdversario + " " + timeAdversario.getNome();
    }

    public Jogador getUsuario() {
        return usuario;
    }

    public Loja getLoja() {
        return loja;
    }

    public Time getTimeUsuario() {
        return timeUsuario;
    }

    public Time getTimeAdversario() {
        return timeAdversario;
    }
}