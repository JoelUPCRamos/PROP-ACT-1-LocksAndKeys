package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.Direccio;
import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.model.Posicio;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

/**
 * IDS con:
 *  - LNT por profundidad (usarLNT=true): solo descarta si ya se visitó a menor o igual depth en la iteración.
 *  - Control por rama (usarLNT=false).
 *  - Ordenación de sucesores: primero "recoger llave", luego menor Manhattan a salida.
 */
public class CercaIDS extends CercaBase {
    public CercaIDS(boolean usarLNT) { super(usarLNT); }

    // Opcional: tope de seguridad; si no lo quieres, pon Integer.MAX_VALUE.
    private static final int MAX_LIMIT = 2000;

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        for (int limit = 0; limit <= MAX_LIMIT; limit++) {
            Deque<Node> open = new ArrayDeque<>();
            Map<Mapa,Integer> closedDepth = usarLNT ? new HashMap<>() : Collections.emptyMap();

            boolean huboCorte = false;

            Node root = new Node(inicial, null, null, 0, 0);
            open.push(root);
            if (usarLNT) closedDepth.put(inicial, 0);

            while (!open.isEmpty()) {
                updateMemoria(rc, open.size(), usarLNT ? closedDepth.size() : 0);

                Node cur = open.pop();
                rc.incNodesExplorats();

                if (cur.estat.esMeta()) {
                    rc.setCami(reconstruirCami(cur));
                    return;
                }

                if (cur.depth == limit) {
                    huboCorte = true;
                    rc.incNodesTallats();
                    continue;
                }

                // === Expandimos "a mano" para poder ordenar por heurística y por si recoge llave ===
                List<Succ> succs = new ArrayList<>();
                for (Moviment mv : cur.estat.getAccionsPossibles()) {
                    try {
                        Mapa nxMap = cur.estat.mou(mv);
                        Node nx = new Node(nxMap, cur, mv, cur.depth + 1, cur.g + 1);
                        int hExit = heuristicaAExit(nxMap);
                        succs.add(new Succ(nx, mv.isRecullClau(), hExit));
                    } catch (IllegalArgumentException ex) {
                        rc.incNodesTallats();
                    }
                }

                // Orden: primero los que recogen llave, luego por menor hExit
                succs.sort((a, b) -> {
                    if (a.recullClau != b.recullClau) return a.recullClau ? -1 : 1;
                    return Integer.compare(a.hExit, b.hExit);
                });

                // DFS: push en orden inverso para que el mejor salga antes
                for (int i = succs.size() - 1; i >= 0; i--) {
                    Node nx = succs.get(i).node;

                    if (!usarLNT) {
                        if (estaAlaBranca(cur, nx.estat)) { rc.incNodesTallats(); continue; }
                    } else {
                        Integer best = closedDepth.get(nx.estat);
                        if (best != null && best <= nx.depth) { rc.incNodesTallats(); continue; }
                        closedDepth.put(nx.estat, nx.depth);
                    }
                    open.push(nx);
                }
            }

            if (!huboCorte) {
                // No hay más profundidad útil -> no hay solución
                return;
            }
        }
        // Llegó al tope de seguridad; elimina MAX_LIMIT si no quieres guard-rail.
    }

    // Mínima distancia Manhattan de cualquier agente a la salida.
    private int heuristicaAExit(Mapa m) {
        Posicio goal = m.getSortidaPosicio();
        int best = Integer.MAX_VALUE;
        for (Posicio ag : m.getAgents()) {
            int d = Math.abs(ag.x - goal.x) + Math.abs(ag.y - goal.y);
            if (d < best) best = d;
        }
        return (best == Integer.MAX_VALUE) ? 0 : best;
    }

    private static class Succ {
        final Node node;
        final boolean recullClau;
        final int hExit;
        Succ(Node node, boolean recullClau, int hExit) {
            this.node = node;
            this.recullClau = recullClau;
            this.hExit = hExit;
        }
    }
}
