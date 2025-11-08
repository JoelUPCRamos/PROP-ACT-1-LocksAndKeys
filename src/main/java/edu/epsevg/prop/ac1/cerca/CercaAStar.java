package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.cerca.heuristica.Heuristica;
import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaAStar extends CercaBase {

    private final Heuristica heur;

    public CercaAStar(boolean usarLNT, Heuristica heur) {
        super(usarLNT);
        this.heur = heur;
    }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        Comparator<Node> cmp = Comparator.comparingInt(n -> n.g + heur.h(n.estat));
        PriorityQueue<Node> open = new PriorityQueue<>(cmp);
        Map<Mapa,Integer> closedDepth = usarLNT ? new HashMap<>() : Collections.emptyMap();

        Node root = new Node(inicial, null, null, 0, 0);
        open.add(root);
        if (usarLNT) closedDepth.put(inicial, 0);

        while (!open.isEmpty()) {
            updateMemoria(rc, open.size(), usarLNT ? closedDepth.size() : 0);

            Node cur = open.poll();
            rc.incNodesExplorats();

            if (cur.estat.esMeta()) {
                rc.setCami(reconstruirCami(cur));
                return;
            }

            for (Node nx : expandir(cur, rc)) {
                if (!usarLNT) {
                    if (estaAlaBranca(cur, nx.estat)) { rc.incNodesTallats(); continue; }
                } else {
                    Integer best = closedDepth.get(nx.estat);
                    if (best != null && best <= nx.depth) { rc.incNodesTallats(); continue; }
                    closedDepth.put(nx.estat, nx.depth);
                }
                open.add(nx);
            }
        }
        // sense solució
    }
}
