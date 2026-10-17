package br.com.valorantrpg.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.valorantrpg.service.Arsenal;

class ArquivoCsvTest {
    private ArquivoCsv csv;
    private Path pasta;

    @BeforeEach
    void preparar() throws IOException {
        csv = new ArquivoCsv();
        pasta = Files.createTempDirectory("valorant-rpg-teste");
    }

    @Test
    @DisplayName("Exporta cabeçalho e uma linha por arma")
    void exportaUmaLinhaPorArma() throws IOException {
        Path arquivo = pasta.resolve("armas.csv");

        csv.exportarArmas(new Arsenal(), arquivo);

        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        assertEquals(ArquivoCsv.CABECALHO_ARMAS, linhas.get(0));
        assertEquals(7, linhas.size()); // cabeçalho + 6 armas de fábrica
    }

    @Test
    @DisplayName("Usa ponto e vírgula como separador e vírgula como decimal")
    void formatoDaLinha() throws IOException {
        Path arquivo = pasta.resolve("armas.csv");

        csv.exportarArmas(new Arsenal(), arquivo);

        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        assertEquals("Classic;0;8;22,00;50,00;0,30;0,85", linhas.get(1));
    }

    @Test
    @DisplayName("Arma cadastrada aparece no arquivo exportado")
    void armaCadastradaEhExportada() throws IOException {
        Arsenal arsenal = new Arsenal();
        arsenal.cadastrarArma("Sheriff", 800, 6, 55, 159, 0.25, 0.7);
        Path arquivo = pasta.resolve("armas.csv");

        csv.exportarArmas(arsenal, arquivo);

        List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        assertEquals(8, linhas.size());
        assertTrue(linhas.contains("Sheriff;800;6;55,00;159,00;0,25;0,70"));
    }

    @Test
    @DisplayName("Cria a pasta de destino quando ela não existe")
    void criaPastaInexistente() throws IOException {
        Path arquivo = pasta.resolve("nova/pasta/armas.csv");

        csv.exportarArmas(new Arsenal(), arquivo);

        assertTrue(Files.exists(arquivo));
    }
}
