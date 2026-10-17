package br.com.valorantrpg.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JogadorTest {
    private Arma classic;
    private Agente sage;
    private Jogador jogador;

    @BeforeEach
    void preparar() {
        classic = new Arma("Classic", 0, 8, 22, 50, 0.3, 0.85);
        sage = new Agente("Sage", 3);
        sage.adicionarHabilidade(new Habilidade("Orbe de Barreira", "ESCUDO", "SI_MESMO", 40, 0, false, 400));
        sage.adicionarHabilidade(new Habilidade("Orbe de Cura", "CURA", "SI_MESMO", 40, 0, false, 200));
        jogador = new Jogador("Teste", sage, classic);
    }

    @Test
    @DisplayName("Jogador começa com 100 de vida, 800 créditos e pente cheio")
    void estadoInicial() {
        assertEquals(100, jogador.getVida(), 0.001);
        assertEquals(800, jogador.getCreditos());
        assertEquals(8, jogador.getMunicaoAtual());
    }

    @Test
    @DisplayName("Escudo absorve o dano antes da vida")
    void escudoAbsorveDanoAntesDaVida() {
        jogador.equiparEscudo(new Escudo("Escudo Leve", 400, 25));
        jogador.receberDano(40);
        assertEquals(0, jogador.getEscudo(), 0.001);
        assertEquals(85, jogador.getVida(), 0.001);
    }

    @Test
    @DisplayName("Dano menor que o escudo não tira vida")
    void danoMenorQueEscudoNaoTiraVida() {
        jogador.equiparEscudo(new Escudo("Escudo Pesado", 1000, 50));
        jogador.receberDano(30);
        assertEquals(20, jogador.getEscudo(), 0.001);
        assertEquals(100, jogador.getVida(), 0.001);
    }

    @Test
    @DisplayName("Vulnerabilidade aumenta o dano recebido")
    void vulnerabilidadeAumentaDano() {
        jogador.aplicarEfeito("VULNERABILIDADE", 25, 2);
        jogador.receberDano(40);
        assertEquals(50, jogador.getVida(), 0.001);
    }

    @Test
    @DisplayName("Vida nunca fica negativa")
    void vidaNaoFicaNegativa() {
        jogador.receberDano(500);
        assertEquals(0, jogador.getVida(), 0.001);
        assertFalse(jogador.estaVivo());
    }

    @Test
    @DisplayName("Não gasta mais créditos do que tem")
    void naoGastaMaisDoQueTem() {
        assertFalse(jogador.gastarCreditos(900));
        assertEquals(800, jogador.getCreditos());
        assertTrue(jogador.gastarCreditos(800));
        assertEquals(0, jogador.getCreditos());
    }

    @Test
    @DisplayName("Cura não ultrapassa 100 de vida e não revive")
    void cura() {
        jogador.receberDano(10);
        jogador.curar(40);
        assertEquals(100, jogador.getVida(), 0.001);
        jogador.receberDano(200);
        jogador.curar(40);
        assertEquals(0, jogador.getVida(), 0.001);
    }

    @Test
    @DisplayName("Escudo ganho por habilidade tem limite de 50")
    void escudoTemLimite() {
        jogador.ganharEscudo(40);
        jogador.ganharEscudo(40);
        assertEquals(50, jogador.getEscudo(), 0.001);
    }

    @Test
    @DisplayName("Ao morrer perde arma comprada, escudo e habilidades")
    void morrerPerdeItens() {
        jogador.equiparArma(new Arma("Vandal", 2900, 4, 40, 160, 0.2, 0.70));
        jogador.equiparEscudo(new Escudo("Escudo Pesado", 1000, 50));
        jogador.adicionarHabilidade(sage.getHabilidades().get(0));

        jogador.morrer();

        assertSame(classic, jogador.getArma());
        assertEquals(0, jogador.getEscudo(), 0.001);
        assertEquals(0, jogador.getHabilidades().size());
        assertEquals(1, jogador.getMortes());
    }

    @Test
    @DisplayName("Tiro na cabeça é identificado")
    void tiroNaCabeca() {
        jogador.equiparArma(new Arma("Sempre Cabeça", 0, 5, 30, 90, 1.0, 1.0));
        assertEquals(90, jogador.atirar(), 0.001);
        assertTrue(jogador.foiTiroNaCabeca());
    }

    @Test
    @DisplayName("Buff aumenta o dano do tiro")
    void buffAumentaDano() {
        jogador.equiparArma(new Arma("Corpo", 0, 5, 30, 90, 0.0, 1.0));
        jogador.aplicarEfeito("BUFF", 20, 3);
        assertEquals(36, jogador.atirar(), 0.001);
    }

    @Test
    @DisplayName("Cegueira total impede tiro na cabeça")
    void cegueiraImpedeCabeca() {
        jogador.equiparArma(new Arma("Sempre Cabeça", 0, 5, 30, 90, 1.0, 1.0));
        jogador.aplicarEfeito("CEGUEIRA", 100, 2);
        assertEquals(30, jogador.atirar(), 0.001);
        assertFalse(jogador.foiTiroNaCabeca());
    }

    @Test
    @DisplayName("Dano contínuo acaba depois da duração")
    void danoContinuoTemDuracao() {
        jogador.aplicarEfeito("DANO_CONTINUO", 10, 3);
        for (int i = 0; i < 5; i++) {
            jogador.processarEfeitos();
        }
        assertEquals(70, jogador.getVida(), 0.001);
    }

    @Test
    @DisplayName("Sem munição o tiro não causa dano e recarregar enche o pente")
    void municaoERecarga() {
        jogador.equiparArma(new Arma("Uma Bala", 0, 1, 30, 90, 0.0, 1.0));
        assertEquals(30, jogador.atirar(), 0.001);
        assertEquals(0, jogador.atirar(), 0.001);
        assertTrue(jogador.recarregar());
        assertEquals(1, jogador.getMunicaoAtual());
        assertFalse(jogador.recarregar());
    }
}
