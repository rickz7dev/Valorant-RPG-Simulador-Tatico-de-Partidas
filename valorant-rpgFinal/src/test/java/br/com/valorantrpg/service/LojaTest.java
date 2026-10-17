package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.model.Escudo;
import br.com.valorantrpg.model.Habilidade;
import br.com.valorantrpg.model.Jogador;

class LojaTest {
    private Loja loja;
    private CatalogoAgentes catalogo;
    private Jogador sage;

    @BeforeEach
    void preparar() {
        loja = new Loja();
        catalogo = new CatalogoAgentes();
        sage = new Jogador("Sage", catalogo.buscarPorNome("Sage"), loja.getArsenal().buscarPorNome("Classic"));
    }

    @Test
    @DisplayName("Compra de arma sem créditos é recusada")
    void armaSemCreditos() {
        assertFalse(loja.comprarArma(sage, loja.getArsenal().buscarPorNome("Vandal")));
        assertEquals(800, sage.getCreditos());
        assertEquals("Classic", sage.getArma().getNome());
    }

    @Test
    @DisplayName("Compra de arma desconta créditos e equipa")
    void compraArma() {
        assertTrue(loja.comprarArma(sage, loja.getArsenal().buscarPorNome("Ghost")));
        assertEquals(300, sage.getCreditos());
        assertEquals("Ghost", sage.getArma().getNome());
    }

    @Test
    @DisplayName("Não compra a arma que já está na mão")
    void naoCompraMesmaArma() {
        assertFalse(loja.comprarArma(sage, loja.getArsenal().buscarPorNome("Classic")));
    }

    @Test
    @DisplayName("Não compra escudo igual ou mais fraco que o atual")
    void escudoNaoRegride() {
        Escudo leve = loja.getEscudos().get(1);
        assertTrue(loja.comprarEscudo(sage, leve));
        assertFalse(loja.comprarEscudo(sage, leve));
        assertEquals(400, sage.getCreditos());
    }

    @Test
    @DisplayName("Habilidade passiva não pode ser comprada")
    void passivaNaoVende() {
        Jogador reyna = new Jogador("Reyna", catalogo.buscarPorNome("Reyna"),
                loja.getArsenal().buscarPorNome("Classic"));
        Habilidade devorar = reyna.getAgente().getHabilidades().get(1);
        assertTrue(devorar.isPassiva());
        assertFalse(loja.comprarHabilidade(reyna, devorar));
    }

    @Test
    @DisplayName("Não compra habilidade de outro agente")
    void habilidadeDeOutroAgente() {
        Habilidade queimar = catalogo.buscarPorNome("Fenix").getHabilidades().get(0);
        assertFalse(loja.comprarHabilidade(sage, queimar));
        assertEquals(800, sage.getCreditos());
    }

    @Test
    @DisplayName("Não compra a mesma habilidade duas vezes")
    void habilidadeRepetida() {
        Habilidade cura = sage.getAgente().getHabilidades().get(1);
        assertTrue(loja.comprarHabilidade(sage, cura));
        assertFalse(loja.comprarHabilidade(sage, cura));
        assertEquals(600, sage.getCreditos());
    }

    @Test
    @DisplayName("Compra automática do bot prioriza escudo e respeita o saldo")
    void compraAutomatica() {
        String resultado = loja.compraAutomatica(sage);
        assertEquals(25, sage.getEscudo(), 0.001);
        assertTrue(sage.getCreditos() >= 0);
        assertTrue(resultado.contains("Escudo Leve"));
    }
}
