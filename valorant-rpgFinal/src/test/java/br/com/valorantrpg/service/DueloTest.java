package br.com.valorantrpg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.model.Agente;
import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Jogador;

class DueloTest {
    private CatalogoAgentes catalogo;
    private Arma classic;

    @BeforeEach
    void preparar() {
        catalogo = new CatalogoAgentes();
        classic = new Arsenal().buscarPorNome("Classic");
    }

    private Jogador jogador(String agente) {
        return new Jogador(agente, catalogo.buscarPorNome(agente), classic);
    }

    @Test
    @DisplayName("Agente com velocidade 0 nunca age primeiro")
    void velocidadeDefineIniciativa() {
        Jogador lento = new Jogador("Lento", new Agente("Lento", 0), classic);
        Jogador rapido = jogador("Reyna");
        Duelo d = new Duelo(lento, rapido);
        for (int i = 0; i < 100; i++) {
            assertSame(rapido, d.decidirQuemAgePrimeiro());
        }
    }

    @Test
    @DisplayName("Encerrar duelo registra abate para o vencedor e morte para o perdedor")
    void encerrarRegistraAbate() {
        Jogador a = jogador("Sage");
        Jogador b = jogador("Viper");
        b.receberDano(200);
        Duelo d = new Duelo(a, b);
        assertTrue(d.terminou());
        assertSame(a, d.encerrar());
        assertEquals(1, a.getKills());
        assertEquals(1, a.getKillsRodada());
        assertEquals(1, b.getMortes());
    }

    @Test
    @DisplayName("Passiva Devorar da Reyna cura ao abater")
    void passivaDaReyna() {
        Jogador reyna = jogador("Reyna");
        Jogador viper = jogador("Viper");
        reyna.receberDano(50);
        viper.receberDano(200);
        new Duelo(reyna, viper).encerrar();
        assertEquals(80, reyna.getVida(), 0.001);
    }

    @Test
    @DisplayName("Habilidade só pode ser usada se foi comprada e é consumida após o uso")
    void habilidadeCompradaEConsumida() {
        Jogador sage = jogador("Sage");
        Jogador viper = jogador("Viper");
        Duelo d = new Duelo(sage, viper);

        String semComprar = d.executarAcao(sage, viper, "HABILIDADE1");
        assertTrue(semComprar.contains("não tem"));

        sage.adicionarHabilidade(sage.getAgente().getHabilidades().get(0));
        String usou = d.executarAcao(sage, viper, "HABILIDADE1");
        assertTrue(usou.contains("usou"));
        assertEquals(40, sage.getEscudo(), 0.001);
        assertEquals(0, sage.getHabilidades().size());
    }

    @Test
    @DisplayName("Pós-choque da Breach causa dano direto no inimigo")
    void habilidadeDeDano() {
        Jogador breach = jogador("Breach");
        Jogador sage = jogador("Sage");
        breach.adicionarHabilidade(breach.getAgente().getHabilidades().get(1));
        new Duelo(breach, sage).executarAcao(breach, sage, "HABILIDADE2");
        assertEquals(40, sage.getVida(), 0.001);
    }

    @Test
    @DisplayName("Atirar sem munição não causa dano")
    void semMunicao() {
        Jogador a = new Jogador("A", new Agente("A", 5), new Arma("Uma", 0, 1, 30, 90, 0, 1.0));
        Jogador b = jogador("Sage");
        Duelo d = new Duelo(a, b);
        d.executarAcao(a, b, "ATIRAR");
        String texto = d.executarAcao(a, b, "ATIRAR");
        assertTrue(texto.contains("sem munição"));
        assertEquals(70, b.getVida(), 0.001);
    }

    @Test
    @DisplayName("Bot recarrega quando está sem munição")
    void botRecarrega() {
        Jogador a = new Jogador("A", new Agente("A", 5), new Arma("Uma", 0, 1, 30, 90, 0, 1.0));
        Duelo d = new Duelo(a, jogador("Sage"));
        a.atirar();
        assertEquals("RECARREGAR", d.escolherAcaoBot(a));
    }

    @Test
    @DisplayName("Duelo simulado sempre termina com um vencedor")
    void simulacaoTermina() {
        Duelo d = new Duelo(jogador("Fenix"), jogador("Breach"));
        Jogador vencedor = d.simular();
        assertTrue(vencedor.estaVivo());
        assertFalse(d.getOponente(vencedor).estaVivo());
    }
}
