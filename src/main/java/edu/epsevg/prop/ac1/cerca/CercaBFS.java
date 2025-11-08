package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaBFS extends CercaBase {
    public CercaBFS(boolean usarLNT) { super(usarLNT); }

    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
        Deque<Node> open = new ArrayDeque<>();
        // LNT: mapa -> millor profunditat vista
        Map<Mapa,Integer> closedDepth = usarLNT ? new HashMap<>() : Collections.emptyMap();

        Node root = new Node(inicial, null, null, 0, 0);
        open.addLast(root);
        if (usarLNT) closedDepth.put(inicial, 0);

        while (!open.isEmpty()) {
            updateMemoria(rc, open.size(), usarLNT ? closedDepth.size() : 0);

            Node cur = open.removeFirst();
            rc.incNodesExplorats();

            if (cur.estat.esMeta()) {
                rc.setCami(reconstruirCami(cur));
                return;
            }

            for (Node nx : expandir(cur, rc)) {
                // Control cicles
                if (!usarLNT) {
                    if (estaAlaBranca(cur, nx.estat)) { rc.incNodesTallats(); continue; }
                } else {
                    Integer best = closedDepth.get(nx.estat);
                    if (best != null && best <= nx.depth) { rc.incNodesTallats(); continue; }
                    closedDepth.put(nx.estat, nx.depth);
                }
                open.addLast(nx);
            }
        }
        // Sense soluciÃ³ (rc.getCami()==null)
    }
}