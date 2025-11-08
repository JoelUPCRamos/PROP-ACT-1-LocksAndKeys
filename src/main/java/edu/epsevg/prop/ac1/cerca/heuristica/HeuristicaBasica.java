package edu.epsevg.prop.ac1.cerca.heuristica;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

import java.util.List;

/** 
 * Distància de Manhattan a la clau més propera 
 * (si queden per recollir) o a la sortida.
 */
public class HeuristicaBasica implements Heuristica {
    @Override
    public int h(Mapa estat) {
        // si algun agent ja és a la meta, heurística 0
        if (estat.esMeta()) return 0;

        // Claus pendents? -> distància mínima agent -> clau
        List<Posicio> clausPendents = estat.getClausPendents(); // requereix els helpers que comentamos
        if (!clausPendents.isEmpty()) {
            int best = Integer.MAX_VALUE;
            for (Posicio ag : estat.getAgents()) {
                for (Posicio k : clausPendents) {
                    int d = Math.abs(ag.x - k.x) + Math.abs(ag.y - k.y);
                    if (d < best) best = d;
                }
            }
            return (best == Integer.MAX_VALUE) ? 0 : best;
        }

        // Si no hi ha claus pendents -> distància mínima agent -> sortida
        Posicio goal = estat.getSortidaPosicio();
        int best = Integer.MAX_VALUE;
        for (Posicio ag : estat.getAgents()) {
            int d = Math.abs(ag.x - goal.x) + Math.abs(ag.y - goal.y);
            if (d < best) best = d;
        }
        return (best == Integer.MAX_VALUE) ? 0 : best;
    }
}
