package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Habilidade;
import br.com.valorantrpg.model.Jogador;

public class Duelo {
    private Jogador jogadorA;
    private Jogador jogadorB;

    public Duelo(Jogador jogadorA, Jogador jogadorB) {
        this.jogadorA = jogadorA;
        this.jogadorB = jogadorB;
    }

    public Jogador decidirQuemAgePrimeiro() {
        double chance = jogadorA.getAgente().getVelocidade()
                / (jogadorB.getAgente().getVelocidade() + jogadorA.getAgente().getVelocidade());
        if (Math.random() < chance) {
            return jogadorA;
        } else
            return jogadorB;
    }

    public String executarAcao(Jogador atacante, Jogador alvo, String acao) {
        if (acao.equals("ATIRAR")) {
            if (atacante.getMunicaoAtual() == 0) {
                return atacante.getAgente().getNome() + " está sem munição";
            }
            double dano = atacante.atirar();
            if (dano > 0) {
                alvo.receberDano(dano);
                String texto = String.format("%s acertou %s e causou %.0f de dano", atacante.getAgente().getNome(),
                        alvo.getAgente().getNome(), dano);
                if (atacante.foiTiroNaCabeca()) {
                    texto = texto + " (TIRO NA CABEÇA!)";
                }
                return texto;
            }
            return atacante.getAgente().getNome() + " errou o tiro";
        } else if (acao.equals("RECARREGAR")) {
            if (atacante.recarregar()) {
                return atacante.getAgente().getNome() + " recarregou a sua arma";
            }
            return atacante.getAgente().getNome() + " já está com o pente cheio";
        } else if (acao.equals("HABILIDADE1") || acao.equals("HABILIDADE2")) {
            return usarHabilidade(atacante, alvo, acao);
        }
        return atacante.getAgente().getNome() + " não fez nada";
    }

    private String usarHabilidade(Jogador atacante, Jogador alvo, String acao) {
        String nome = atacante.getAgente().getNome();

        int indice = 0;
        if (acao.equals("HABILIDADE2")) {
            indice = 1;
        }
        Habilidade h = atacante.getAgente().getHabilidades().get(indice);

        Habilidade comprada = null;
        for (int i = 0; i < atacante.getHabilidades().size(); i++) {
            if (atacante.getHabilidades().get(i).getNome().equals(h.getNome())) {
                comprada = atacante.getHabilidades().get(i);
            }
        }
        if (comprada == null) {
            return nome + " não tem a habilidade " + h.getNome();
        }

        if (!h.aplicar(atacante, alvo)) {
            return nome + " ainda não pode usar " + h.getNome();
        }

        atacante.consumirHabilidade(comprada);
        return nome + " usou " + h.getNome();
    }

    public String executarTurno(String acaoA, String acaoB) {
        Jogador primeiro = decidirQuemAgePrimeiro();
        Jogador segundo;
        String acaoPrimeiro;
        String acaoSegundo;

        if (primeiro == jogadorA) {
            segundo = jogadorB;
            acaoPrimeiro = acaoA;
            acaoSegundo = acaoB;
        } else {
            segundo = jogadorA;
            acaoPrimeiro = acaoB;
            acaoSegundo = acaoA;
        }

        String texto = executarAcao(primeiro, segundo, acaoPrimeiro);

        if (segundo.estaVivo()) {
            texto = texto + "\n" + executarAcao(segundo, primeiro, acaoSegundo);
        } else {
            texto = texto + "\n" + segundo.getAgente().getNome() + " foi derrubado e não conseguiu agir";
        }

        texto = texto + efeitosDe(jogadorA);
        if (jogadorA.estaVivo()) {
            texto = texto + efeitosDe(jogadorB);
        }
        return texto;
    }

    public boolean terminou() {
        return !jogadorA.estaVivo() || !jogadorB.estaVivo();
    }

    public String escolherAcaoBot(Jogador j) {
        if (j.getMunicaoAtual() == 0) {
            return "RECARREGAR";
        }
        if (j.getHabilidades().size() > 0 && Math.random() < 0.4) {
            Habilidade h = j.getHabilidades().get(0);
            boolean util = true;
            if (h.getTipo().equals("CURA") && j.getVida() >= 70) {
                util = false;
            }
            if (h.getTipo().equals("ESCUDO") && j.getEscudo() >= 50) {
                util = false;
            }
            if (util) {
                for (int i = 0; i < j.getAgente().getHabilidades().size(); i++) {
                    if (j.getAgente().getHabilidades().get(i).getNome().equals(h.getNome())) {
                        return "HABILIDADE" + (i + 1);
                    }
                }
            }
        }
        return "ATIRAR";
    }

    public Jogador simular() {
        while (!terminou()) {
            executarTurno(escolherAcaoBot(jogadorA), escolherAcaoBot(jogadorB));
        }
        return encerrar();
    }

    private String efeitosDe(Jogador j) {
        if (!j.estaVivo()) {
            return "";
        }
        String texto = j.processarEfeitos();
        if (texto.equals("")) {
            return "";
        }
        return "\n" + texto;
    }

    public Jogador encerrar() {
        Jogador vencedor;
        Jogador perdedor;
        if (jogadorA.estaVivo()) {
            vencedor = jogadorA;
            perdedor = jogadorB;
        } else {
            vencedor = jogadorB;
            perdedor = jogadorA;
        }

        vencedor.registrarKill();

        for (int i = 0; i < vencedor.getAgente().getHabilidades().size(); i++) {
            Habilidade h = vencedor.getAgente().getHabilidades().get(i);
            if (h.isPassiva()) {
                h.aplicar(vencedor, perdedor);
            }
        }

        perdedor.morrer();
        return vencedor;

    }

    public boolean envolve(Jogador j) {
        return jogadorA == j || jogadorB == j;
    }

    public Jogador getOponente(Jogador j) {
        if (jogadorA == j) {
            return jogadorB;
        }
        return jogadorA;
    }
}
