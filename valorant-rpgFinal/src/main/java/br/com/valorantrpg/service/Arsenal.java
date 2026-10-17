package br.com.valorantrpg.service;

import br.com.valorantrpg.model.Arma;

import java.util.ArrayList;

public class Arsenal {
    private ArrayList<Arma> armas = new ArrayList<>();

    public Arsenal() {
        adicionarArma(new Arma("Classic", 0, 8, 22, 50, 0.3, 0.85));
        adicionarArma(new Arma("Ghost", 500, 8, 25, 50, 0.38, 0.80));
        adicionarArma(new Arma("Spectre", 1600, 5, 27, 90, 0.3, 0.75));
        adicionarArma(new Arma("Vandal", 2900, 4, 40, 160, 0.2, 0.70));
        adicionarArma(new Arma("Phantom", 2900, 5, 35, 100, 0.3, 0.75));
        adicionarArma(new Arma("Operator", 4500, 3, 150, 255, 0.5, 0.60));
    }

    private void adicionarArma(Arma a) {
        armas.add(a);
    }

    public ArrayList<Arma> getArmas() {
        return armas;
    }

    public String listar() {
        String texto = "";
        for (int i = 0; i < armas.size(); i++) {
            texto = texto + (i + 1) + " - " + armas.get(i).toString() + "\n";
        }
        return texto;
    }

    public Arma buscarPorNome(String nome) {
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i).getNome().equalsIgnoreCase(nome)) {
                return armas.get(i);
            }
        }
        return null;
    }

    public ArrayList<Arma> buscarPorPrecoMaximo(int limite) {
        ArrayList<Arma> achadas = new ArrayList<>();
        for (int i = 0; i < armas.size(); i++) {
            if (armas.get(i).getPreco() <= limite) {
                achadas.add(armas.get(i));
            }
        }
        return achadas;
    }

    public boolean cadastrarArma(String nome, int preco, int pente, double dano, double danoCabeca,
            double chanceCabeca, double chanceAcerto) {
        if (nome.trim().equals("") || buscarPorNome(nome.trim()) != null) {
            return false;
        }
        if (preco < 0 || pente < 1 || dano <= 0 || danoCabeca <= 0) {
            return false;
        }
        if (chanceCabeca < 0 || chanceCabeca > 1 || chanceAcerto <= 0 || chanceAcerto > 1) {
            return false;
        }
        adicionarArma(new Arma(nome.trim(), preco, pente, dano, danoCabeca, chanceCabeca, chanceAcerto));
        return true;
    }

    public boolean editarPreco(String nome, int novoPreco) {
        Arma a = buscarPorNome(nome);
        if (a == null || a.getNome().equalsIgnoreCase("Classic") || novoPreco < 0) {
            return false;
        }
        a.setPreco(novoPreco);
        return true;
    }

    public boolean removerArma(String nome) {
        Arma a = buscarPorNome(nome);
        if (a == null || a.getNome().equalsIgnoreCase("Classic")) {
            return false;
        }
        armas.remove(a);
        return true;
    }

    public String listarOrdenado(String criterio) {
        ArrayList<Arma> copia = new ArrayList<>();
        for (int i = 0; i < armas.size(); i++) {
            copia.add(armas.get(i));
        }
        for (int i = 0; i < copia.size() - 1; i++) {
            for (int k = 0; k < copia.size() - 1 - i; k++) {
                boolean trocar;
                if (criterio.equals("PRECO")) {
                    trocar = copia.get(k).getPreco() > copia.get(k + 1).getPreco();
                } else {
                    trocar = copia.get(k).getDano() > copia.get(k + 1).getDano();
                }
                if (trocar) {
                    Arma troca = copia.get(k);
                    copia.set(k, copia.get(k + 1));
                    copia.set(k + 1, troca);
                }
            }
        }
        String texto = "";
        for (int i = 0; i < copia.size(); i++) {
            texto = texto + (i + 1) + " - " + copia.get(i).toString() + "\n";
        }
        return texto;
    }
}