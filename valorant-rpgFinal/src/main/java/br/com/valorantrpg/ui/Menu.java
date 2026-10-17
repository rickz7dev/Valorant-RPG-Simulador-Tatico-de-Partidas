package br.com.valorantrpg.ui;

import br.com.valorantrpg.io.ArquivoCsv;
import br.com.valorantrpg.model.Agente;
import br.com.valorantrpg.model.Arma;
import br.com.valorantrpg.model.Habilidade;
import br.com.valorantrpg.model.Jogador;
import br.com.valorantrpg.service.Arsenal;
import br.com.valorantrpg.service.CatalogoAgentes;
import br.com.valorantrpg.service.Duelo;
import br.com.valorantrpg.service.Loja;
import br.com.valorantrpg.service.Partida;
import br.com.valorantrpg.service.Relatorio;
import br.com.valorantrpg.service.Rodada;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private Scanner sc = new Scanner(System.in);
    private CatalogoAgentes catalogo = new CatalogoAgentes();
    private Loja loja = new Loja();
    private Partida ultimaPartida;

    public void iniciar() {
        System.out.println("=== VALORANT RPG ===");
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1 - Jogar uma rodada");
            System.out.println("2 - Gerenciar armas");
            System.out.println("3 - Consultar agentes");
            System.out.println("4 - Relatório da última partida");
            System.out.println("0 - Sair");
            opcao = lerInt();

            if (opcao == 1) {
                jogarPartida();
            } else if (opcao == 2) {
                menuArmas();
            } else if (opcao == 3) {
                menuAgentes();
            } else if (opcao == 4) {
                mostrarRelatorio();
            }
        }
        System.out.println("Até a próxima!");
    }

    private void jogarPartida() {
        System.out.print("Seu Nickname: ");
        String nickname = sc.nextLine();

        Agente escolhido = escolherAgente();
        Partida partida = new Partida(nickname, catalogo, escolhido, loja);
        ultimaPartida = partida;
        Jogador usuario = partida.getUsuario();

        while (!partida.terminou()) {
            Rodada rodada = partida.novaRodada();
            rodada.iniciar();
            System.out.println("\n===== RODADA " + rodada.getNumeroDaRodada() + " =====");
            System.out.println(partida.getPlacar());
            rodada.comprasDosBots();
            menuLoja(partida);

            while (rodada.vencedor() == null) {
                ArrayList<Duelo> duelos = rodada.montarConfrontos();
                for (int i = 0; i < duelos.size(); i++) {
                    Duelo d = duelos.get(i);
                    if (d.envolve(usuario) && usuario.estaVivo()) {
                        jogarDueloUsuario(d, usuario, partida);
                    } else {
                        Jogador v = d.simular();
                        System.out.println(v.getNome() + " abateu " + d.getOponente(v).getNome()
                                + "  [" + vivos(partida) + "]");
                    }
                }
            }

            rodada.pagar();
            partida.registrarResultado(rodada);
            System.out.println("\n" + rodada.vencedor().getNome() + " venceu a rodada!");
            System.out.println(partida.getPlacar());
            System.out.println(
                    "Seus créditos: " + usuario.getCreditos() + " | Abates na rodada: " + usuario.getKillsRodada());
        }

        System.out.println("\n=== FIM DA PARTIDA ===");
        System.out.println("Vencedor: " + partida.vencedorDaPartida().getNome());
        System.out.println(partida.getPlacar());
        mostrarRelatorio();
    }

    private int lerInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lerDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Agente escolherAgente() {
        int n = 0;
        while (n < 1 || n > catalogo.getAgentes().size()) {
            System.out.println("Escolha seu agente:");
            System.out.println(catalogo.listar());
            n = lerInt();
        }
        return catalogo.getAgentes().get(n - 1);
    }

    private void mostrarRelatorio() {
        if (ultimaPartida == null) {
            System.out.println("Nenhuma partida foi jogada ainda.");
            return;
        }
        Relatorio relatorio = new Relatorio(ultimaPartida.getTimeUsuario(), ultimaPartida.getTimeAdversario());
        System.out.println(relatorio.gerar(ultimaPartida.getUsuario()));
    }

    private void menuAgentes() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- AGENTES ---");
            System.out.println("1 - Listar agentes");
            System.out.println("2 - Consultar agente por nome");
            System.out.println("0 - Voltar");
            opcao = lerInt();

            if (opcao == 1) {
                System.out.println(catalogo.listar());
            } else if (opcao == 2) {
                System.out.print("Nome do agente: ");
                Agente a = catalogo.buscarPorNome(sc.nextLine());
                if (a == null) {
                    System.out.println("Agente não encontrado.");
                } else {
                    System.out.println(a);
                    for (int i = 0; i < a.getHabilidades().size(); i++) {
                        System.out.println("   " + a.getHabilidades().get(i));
                    }
                }
            }
        }
    }

    private void menuArmas() {
        Arsenal arsenal = loja.getArsenal();
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- GERENCIAR ARMAS ---");
            System.out.println("1 - Listar armas");
            System.out.println("2 - Pesquisar arma por nome");
            System.out.println("3 - Pesquisar armas por preço máximo");
            System.out.println("4 - Listar armas ordenadas");
            System.out.println("5 - Cadastrar nova arma");
            System.out.println("6 - Editar preço de uma arma");
            System.out.println("7 - Remover arma");
            System.out.println("8 - Exportar armas (CSV)");
            System.out.println("0 - Voltar");
            opcao = lerInt();

            if (opcao == 1) {
                System.out.println(arsenal.listar());
            } else if (opcao == 2) {
                System.out.print("Nome da arma: ");
                Arma a = arsenal.buscarPorNome(sc.nextLine());
                if (a == null) {
                    System.out.println("Arma não encontrada.");
                } else {
                    System.out.println(a);
                }
            } else if (opcao == 3) {
                System.out.print("Preço máximo: ");
                ArrayList<Arma> achadas = arsenal.buscarPorPrecoMaximo(lerInt());
                if (achadas.size() == 0) {
                    System.out.println("Nenhuma arma encontrada.");
                }
                for (int i = 0; i < achadas.size(); i++) {
                    System.out.println(achadas.get(i));
                }
            } else if (opcao == 4) {
                System.out.println("Ordenar por: 1 - Preço | 2 - Dano");
                int criterio = lerInt();
                if (criterio == 1) {
                    System.out.println(arsenal.listarOrdenado("PRECO"));
                } else if (criterio == 2) {
                    System.out.println(arsenal.listarOrdenado("DANO"));
                }
            } else if (opcao == 5) {
                System.out.print("Nome: ");
                String nome = sc.nextLine();
                System.out.print("Preço: ");
                int preco = lerInt();
                System.out.print("Tamanho do pente: ");
                int pente = lerInt();
                System.out.print("Dano no corpo: ");
                double dano = lerDouble();
                System.out.print("Dano na cabeça: ");
                double danoCabeca = lerDouble();
                System.out.print("Chance de tiro na cabeça (0 a 1): ");
                double chanceCabeca = lerDouble();
                System.out.print("Chance de acerto (0 a 1): ");
                double chanceAcerto = lerDouble();
                if (arsenal.cadastrarArma(nome, preco, pente, dano, danoCabeca, chanceCabeca, chanceAcerto)) {
                    System.out.println("Arma cadastrada!");
                } else {
                    System.out.println("Não foi possível cadastrar (nome repetido ou valores inválidos).");
                }
            } else if (opcao == 6) {
                System.out.print("Nome da arma: ");
                String nome = sc.nextLine();
                System.out.print("Novo preço: ");
                int novoPreco = lerInt();
                if (arsenal.editarPreco(nome, novoPreco)) {
                    System.out.println("Preço atualizado!");
                } else {
                    System.out.println("Não foi possível editar (arma inexistente, Classic ou preço inválido).");
                }
            } else if (opcao == 7) {
                System.out.print("Nome da arma: ");
                if (arsenal.removerArma(sc.nextLine())) {
                    System.out.println("Arma removida!");
                } else {
                    System.out.println("Não foi possível remover (arma inexistente ou Classic).");
                }
            } else if (opcao == 8) {
                try {
                    new ArquivoCsv().exportarArmas(arsenal, Path.of("dados/armas.csv"));
                    System.out.println("Armas exportadas para dados/armas.csv");
                } catch (IOException e) {
                    System.out.println("Erro ao exportar: " + e.getMessage());
                }
            }
        }
    }

    private void resultadoCompra(boolean ok) {
        if (ok) {
            System.out.println("Compra realizada!");
        } else {
            System.out.println("Compra recusada (sem saldo, item repetido ou indisponível).");
        }
    }

    private void menuLoja(Partida partida) {
        Loja lojaDaPartida = partida.getLoja();
        Jogador u = partida.getUsuario();
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("\n--- LOJA --- Créditos: " + u.getCreditos() + " | Arma: " + u.getArma().getNome()
                    + " | Escudo: " + (int) u.getEscudo());
            System.out.println("1 - Comprar arma");
            System.out.println("2 - Comprar escudo");
            System.out.println("3 - Comprar habilidade");
            System.out.println("4 - Começar a rodada\n");
            opcao = lerInt();

            if (opcao == 1) {
                System.out.println(lojaDaPartida.getArsenal().listar());
                int n = lerInt();
                if (n >= 1 && n <= lojaDaPartida.getArsenal().getArmas().size()) {
                    resultadoCompra(lojaDaPartida.comprarArma(u, lojaDaPartida.getArsenal().getArmas().get(n - 1)));
                }
            } else if (opcao == 2) {
                System.out.println(lojaDaPartida.listarEscudos());
                int n = lerInt();
                if (n >= 1 && n <= lojaDaPartida.getEscudos().size()) {
                    resultadoCompra(lojaDaPartida.comprarEscudo(u, lojaDaPartida.getEscudos().get(n - 1)));
                }
            } else if (opcao == 3) {
                ArrayList<Habilidade> habs = u.getAgente().getHabilidades();
                for (int i = 0; i < habs.size(); i++) {
                    System.out.println((i + 1) + " - " + habs.get(i));
                }
                int n = lerInt();
                if (n >= 1 && n <= habs.size()) {
                    resultadoCompra(lojaDaPartida.comprarHabilidade(u, habs.get(n - 1)));
                }
            }
        }
    }

    private String escolherAcaoUsuario(Jogador u) {
        ArrayList<String> codigos = new ArrayList<>();
        ArrayList<String> textos = new ArrayList<>();
        codigos.add("ATIRAR");
        textos.add("Atirar (" + u.getMunicaoAtual() + " balas)");
        codigos.add("RECARREGAR");
        textos.add("Recarregar");

        ArrayList<Habilidade> habsAgente = u.getAgente().getHabilidades();
        for (int i = 0; i < habsAgente.size(); i++) {
            for (int k = 0; k < u.getHabilidades().size(); k++) {
                if (u.getHabilidades().get(k).getNome().equals(habsAgente.get(i).getNome())) {
                    codigos.add("HABILIDADE" + (i + 1));
                    textos.add("Usar " + habsAgente.get(i).getNome());
                }
            }
        }

        int opcao = 0;
        while (opcao < 1 || opcao > codigos.size()) {
            System.out.println("Escolha sua ação:");
            for (int i = 0; i < textos.size(); i++) {
                System.out.println((i + 1) + " - " + textos.get(i));
            }
            opcao = lerInt();
        }
        return codigos.get(opcao - 1);
    }

    private void jogarDueloUsuario(Duelo d, Jogador usuario, Partida partida) {
        Jogador bot = d.getOponente(usuario);
        System.out.println("\n>>> DUELO: " + usuario.getAgente().getNome() + " x " + bot.getAgente().getNome());
        while (!d.terminou()) {
            System.out.println("Você: vida " + (int) usuario.getVida() + " | escudo " + (int) usuario.getEscudo()
                    + " | balas " + usuario.getMunicaoAtual() + "    Inimigo: vida " + (int) bot.getVida()
                    + " | escudo " + (int) bot.getEscudo());
            String acao = escolherAcaoUsuario(usuario);
            System.out.println("\n" + d.executarTurno(acao, d.escolherAcaoBot(bot)) + "\n");
        }
        Jogador vencedor = d.encerrar();
        if (vencedor == usuario) {
            System.out.println("Você venceu o duelo!");
        } else {
            System.out.println("Você foi derrubado.");
        }
        System.out.println(vivos(partida));
    }

    private String vivos(Partida partida) {
        return "Aliados vivos: " + partida.getTimeUsuario().contarVivos()
                + " | Inimigos vivos: " + partida.getTimeAdversario().contarVivos();
    }
}
