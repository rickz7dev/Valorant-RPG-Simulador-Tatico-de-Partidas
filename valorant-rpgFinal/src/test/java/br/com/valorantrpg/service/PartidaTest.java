package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.model.Jogador;

class PartidaTest {
    private Partida partida;

    @BeforeEach
    void preparar() {
        CatalogoAgentes catalogo = new CatalogoAgentes();
        partida = new Partida("Isac", catalogo, catalogo.buscarPorNome("Reyna"), new Loja());
    }

    private void matarTodos(ArrayList<Jogador> jogadores) {
        for (int i = 0; i < jogadores.size(); i++) {
            jogadores.get(i).morrer();
        }
    }

    @Test
    @DisplayName("Partida monta dois times de 5 com o usuário no time dele")
    void montaTimes() {
        assertEquals(5, partida.getTimeUsuario().getJogadores().size());
        assertEquals(5, partida.getTimeAdversario().getJogadores().size());
        assertTrue(partida.getTimeUsuario().getJogadores().contains(partida.getUsuario()));
    }

    @Test
    @DisplayName("Vence a partida quem fizer 5 rodadas primeiro")
    void venceComCincoRodadas() {
        for (int i = 0; i < 5; i++) {
            assertFalse(partida.terminou());
            Rodada r = partida.novaRodada();
            r.iniciar();
            matarTodos(partida.getTimeAdversario().getJogadores());
            r.pagar();
            partida.registrarResultado(r);
        }
        assertTrue(partida.terminou());
        assertSame(partida.getTimeUsuario(), partida.vencedorDaPartida());
        assertEquals("Seu Time 5 x 0 Time Adversário", partida.getPlacar());
    }

    @Test
    @DisplayName("Fluxo completo simulado: compras -> duelos -> pagamento -> placar")
    void fluxoCompletoSimulado() {
        int rodadas = 0;
        while (!partida.terminou() && rodadas < 50) {
            Rodada r = partida.novaRodada();
            r.iniciar();
            r.comprasDosBots();
            while (r.vencedor() == null) {
                ArrayList<Duelo> duelos = r.montarConfrontos();
                for (int i = 0; i < duelos.size(); i++) {
                    duelos.get(i).simular();
                }
            }
            r.pagar();
            partida.registrarResultado(r);
            rodadas++;
        }
        assertTrue(partida.terminou());
        assertNotNull(partida.vencedorDaPartida());
    }
}
