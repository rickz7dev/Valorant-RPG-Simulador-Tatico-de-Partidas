package br.com.valorantrpg.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.service.Arsenal;

/**
 * Exportação de dados em CSV.
 * Usa ";" como separador e vírgula como casa decimal (padrão do Excel em pt-BR).
 */
public class ArquivoCsv {
    public static final String CABECALHO_ARMAS =
            "nome;preco;pente;dano;danoCabeca;chanceCabeca;chanceAcerto";

    public void exportarArmas(Arsenal arsenal, Path caminho) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add(CABECALHO_ARMAS);

        ArrayList<Arma> armas = arsenal.getArmas();
        for (int i = 0; i < armas.size(); i++) {
            Arma a = armas.get(i);
            linhas.add(String.format(Locale.forLanguageTag("pt-BR"),
                    "%s;%d;%d;%.2f;%.2f;%.2f;%.2f",
                    a.getNome(), a.getPreco(), a.getCapacidadeMunicao(),
                    a.getDano(), a.getDanoCabeca(), a.getChanceCabeca(), a.getChanceAcerto()));
        }

        Path pasta = caminho.toAbsolutePath().getParent();
        if (pasta != null) {
            Files.createDirectories(pasta);
        }
        Files.write(caminho, linhas, StandardCharsets.UTF_8);
    }
}
