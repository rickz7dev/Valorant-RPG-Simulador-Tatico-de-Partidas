package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArsenalTest {
    private Arsenal arsenal;

    @BeforeEach
    void preparar() {
        arsenal = new Arsenal();
    }

    @Test
    @DisplayName("Arsenal começa com 6 armas")
    void armasIniciais() {
        assertEquals(6, arsenal.getArmas().size());
    }

    @Test
    @DisplayName("Classic não pode ser removida nem ter o preço editado")
    void classicProtegida() {
        assertFalse(arsenal.removerArma("Classic"));
        assertFalse(arsenal.editarPreco("classic", 100));
        assertEquals(0, arsenal.buscarPorNome("Classic").getPreco());
    }

    @Test
    @DisplayName("Cadastro válido e busca por nome sem diferenciar maiúsculas")
    void cadastraEBusca() {
        assertTrue(arsenal.cadastrarArma("Sheriff", 800, 6, 55, 159, 0.25, 0.7));
        assertNotNull(arsenal.buscarPorNome("sheriff"));
        assertEquals(7, arsenal.getArmas().size());
    }

    @Test
    @DisplayName("Não cadastra nome repetido ou vazio")
    void naoCadastraRepetido() {
        assertFalse(arsenal.cadastrarArma("vandal", 100, 5, 30, 90, 0.2, 0.7));
        assertFalse(arsenal.cadastrarArma("   ", 100, 5, 30, 90, 0.2, 0.7));
    }

    @Test
    @DisplayName("Não cadastra arma com valores inválidos")
    void naoCadastraInvalida() {
        assertFalse(arsenal.cadastrarArma("Ruim1", -1, 5, 30, 90, 0.2, 0.7));
        assertFalse(arsenal.cadastrarArma("Ruim2", 100, 0, 30, 90, 0.2, 0.7));
        assertFalse(arsenal.cadastrarArma("Ruim3", 100, 5, 0, 90, 0.2, 0.7));
        assertFalse(arsenal.cadastrarArma("Ruim4", 100, 5, 30, 90, 1.5, 0.7));
        assertFalse(arsenal.cadastrarArma("Ruim5", 100, 5, 30, 90, 0.2, 0));
        assertEquals(6, arsenal.getArmas().size());
    }

    @Test
    @DisplayName("Editar preço de uma arma")
    void editarPreco() {
        assertTrue(arsenal.editarPreco("Ghost", 600));
        assertEquals(600, arsenal.buscarPorNome("Ghost").getPreco());
        assertFalse(arsenal.editarPreco("Ghost", -10));
        assertFalse(arsenal.editarPreco("Inexistente", 100));
        assertEquals(600, arsenal.buscarPorNome("Ghost").getPreco());
    }

    @Test
    @DisplayName("Remover arma existente")
    void removerArma() {
        assertTrue(arsenal.removerArma("Operator"));
        assertNull(arsenal.buscarPorNome("Operator"));
        assertFalse(arsenal.removerArma("Operator"));
    }

    @Test
    @DisplayName("Pesquisa por preço máximo")
    void buscaPorPreco() {
        assertEquals(2, arsenal.buscarPorPrecoMaximo(1000).size());
        assertEquals(6, arsenal.buscarPorPrecoMaximo(5000).size());
    }

    @Test
    @DisplayName("Ordenação por preço é crescente e não altera a lista original")
    void ordenacaoPorPreco() {
        arsenal.cadastrarArma("Shorty", 300, 2, 36, 72, 0.15, 0.7);
        String[] linhas = arsenal.listarOrdenado("PRECO").split("\n");
        assertTrue(linhas[0].startsWith("1 - Classic"));
        assertTrue(linhas[1].startsWith("2 - Shorty"));
        assertTrue(linhas[6].startsWith("7 - Operator"));
        assertEquals("Shorty", arsenal.getArmas().get(6).getNome());
    }

    @Test
    @DisplayName("Ordenação por dano é crescente")
    void ordenacaoPorDano() {
        String[] linhas = arsenal.listarOrdenado("DANO").split("\n");
        assertTrue(linhas[0].contains("Classic"));
        assertTrue(linhas[5].contains("Operator"));
    }
}
