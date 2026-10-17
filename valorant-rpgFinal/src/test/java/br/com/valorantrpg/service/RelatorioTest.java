package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.model.Time;

class RelatorioTest {
    private Jogador sage;
    private Jogador reyna;
    private Jogador viper;
    private Jogador fenix;
    private Relatorio relatorio;

    @BeforeEach
    void preparar() {
        CatalogoAgentes catalogo = new CatalogoAgentes();
        Arma classic = new Arsenal().buscarPorNome("Classic");
        sage = new Jogador("Sage", catalogo.buscarPorNome("Sage"), classic);
        reyna = new Jogador("Reyna", catalogo.buscarPorNome("Reyna"), classic);
        viper = new Jogador("Viper", catalogo.buscarPorNome("Viper"), classic);
        fenix = new Jogador("Fenix", catalogo.buscarPorNome("Fenix"), classic);

        Time a = new Time("A");
        a.adicionarJogador(sage);
        a.adicionarJogador(reyna);
        Time b = new Time("B");
        b.adicionarJogador(viper);
        b.adicionarJogador(fenix);

        abates(reyna, 5);
        abates(viper, 2);
        abates(fenix, 1);
        reyna.morrer();
        viper.morrer();
        viper.morrer();

        relatorio = new Relatorio(a, b);
    }

    private void abates(Jogador j, int qtd) {
        for (int i = 0; i < qtd; i++) {
            j.registrarKill();
        }
    }

    @Test
    @DisplayName("Total e média de abates")
    void totalEMedia() {
        assertEquals(8, relatorio.totalDeAbates());
        assertEquals(2.0, relatorio.mediaDeAbates(), 0.001);
    }

    @Test
    @DisplayName("Maior e menor abatedor")
    void maiorEMenor() {
        assertSame(reyna, relatorio.maiorAbatedor());
        assertSame(sage, relatorio.menorAbatedor());
    }

    @Test
    @DisplayName("K/D com e sem mortes")
    void kd() {
        assertEquals(5.0, relatorio.kd(reyna), 0.001);
        assertEquals(1.0, relatorio.kd(viper), 0.001);
        assertEquals(1.0, relatorio.kd(fenix), 0.001);
    }

    @Test
    @DisplayName("Ranking em ordem decrescente de abates")
    void ranking() {
        ArrayList<Jogador> r = relatorio.ranking();
        assertSame(reyna, r.get(0));
        assertSame(viper, r.get(1));
        assertSame(fenix, r.get(2));
        assertSame(sage, r.get(3));
    }

    @Test
    @DisplayName("Texto do relatório traz os indicadores")
    void gerarTexto() {
        String texto = relatorio.gerar(reyna);
        assertTrue(texto.contains("Total de abates: 8"));
        assertTrue(texto.contains("Maior abatedor: Reyna (5)"));
    }
}
