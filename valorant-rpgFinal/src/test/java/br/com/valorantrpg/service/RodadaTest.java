package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.model.Time;

class RodadaTest {
    private Loja loja;
    private CatalogoAgentes catalogo;
    private Arma classic;
    private Time timeA;
    private Time timeB;
    private Jogador a1;
    private Jogador b1;

    @BeforeEach
    void preparar() {
        loja = new Loja();
        catalogo = new CatalogoAgentes();
        classic = loja.getArsenal().buscarPorNome("Classic");
        timeA = new Time("A");
        timeB = new Time("B");
        a1 = new Jogador("A1", catalogo.buscarPorNome("Sage"), classic);
        b1 = new Jogador("B1", catalogo.buscarPorNome("Viper"), classic);
        timeA.adicionarJogador(a1);
        timeB.adicionarJogador(b1);
    }

    @Test
    @DisplayName("Vencedor recebe 3000 + 200 por abate; perdedor recebe 1900")
    void pagamento() {
        Rodada r = new Rodada(1, timeA, timeB, loja, a1);
        r.iniciar();
        a1.registrarKill();
        b1.morrer();
        assertSame(timeA, r.vencedor());
        r.pagar();
        assertEquals(800 + 3000 + 200, a1.getCreditos());
        assertEquals(800 + 1900, b1.getCreditos());
    }

    @Test
    @DisplayName("Rodada sem vencedor não paga ninguém")
    void semVencedorNaoPaga() {
        Rodada r = new Rodada(1, timeA, timeB, loja, a1);
        r.iniciar();
        assertNull(r.vencedor());
        r.pagar();
        assertEquals(800, a1.getCreditos());
        assertEquals(800, b1.getCreditos());
    }

    @Test
    @DisplayName("Iniciar a rodada restaura a vida e zera os abates da rodada")
    void iniciarRestaura() {
        a1.receberDano(60);
        a1.registrarKill();
        Rodada r = new Rodada(2, timeA, timeB, loja, a1);
        r.iniciar();
        assertEquals(100, a1.getVida(), 0.001);
        assertEquals(0, a1.getKillsRodada());
        assertEquals(1, a1.getKills());
    }

    @Test
    @DisplayName("Confrontos pareiam os vivos dos dois times")
    void montarConfrontos() {
        timeA.adicionarJogador(new Jogador("A2", catalogo.buscarPorNome("Reyna"), classic));
        timeA.adicionarJogador(new Jogador("A3", catalogo.buscarPorNome("Fenix"), classic));
        timeB.adicionarJogador(new Jogador("B2", catalogo.buscarPorNome("Breach"), classic));
        Rodada r = new Rodada(1, timeA, timeB, loja, a1);
        r.iniciar();
        assertEquals(2, r.montarConfrontos().size());
    }

    @Test
    @DisplayName("Compras dos bots não mexem nos créditos do usuário")
    void comprasDosBots() {
        Rodada r = new Rodada(1, timeA, timeB, loja, a1);
        r.comprasDosBots();
        assertEquals(800, a1.getCreditos());
        assertEquals(25, b1.getEscudo(), 0.001);
    }
}
