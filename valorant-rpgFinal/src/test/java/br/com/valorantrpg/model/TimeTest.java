package br.com.valorantrpg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeTest {
    private final Arma classic = new Arma("Classic", 0, 8, 22, 50, 0.3, 0.85);

    private Jogador jogadorCom(String nomeAgente) {
        return new Jogador("J " + nomeAgente, new Agente(nomeAgente, 5), classic);
    }

    @Test
    @DisplayName("Time aceita no máximo 5 jogadores")
    void maximoCincoJogadores() {
        Time t = new Time("A");
        String[] agentes = { "Sage", "Reyna", "Fenix", "Breach", "Viper", "Jett" };
        for (int i = 0; i < 5; i++) {
            assertTrue(t.adicionarJogador(jogadorCom(agentes[i])));
        }
        assertFalse(t.adicionarJogador(jogadorCom(agentes[5])));
        assertEquals(5, t.getJogadores().size());
    }

    @Test
    @DisplayName("Time não aceita agente repetido")
    void naoRepeteAgente() {
        Time t = new Time("A");
        assertTrue(t.adicionarJogador(jogadorCom("Sage")));
        assertFalse(t.adicionarJogador(jogadorCom("Sage")));
    }

    @Test
    @DisplayName("Conta vivos e identifica quando todos morreram")
    void contaVivos() {
        Time t = new Time("A");
        Jogador a = jogadorCom("Sage");
        Jogador b = jogadorCom("Reyna");
        t.adicionarJogador(a);
        t.adicionarJogador(b);
        a.morrer();
        assertEquals(1, t.contarVivos());
        assertEquals(1, t.getVivos().size());
        assertFalse(t.todosMortos());
        b.morrer();
        assertTrue(t.todosMortos());
    }
}
