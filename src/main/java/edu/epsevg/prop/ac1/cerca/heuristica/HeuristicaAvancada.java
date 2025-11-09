package edu.epsevg.prop.ac1.cerca.heuristica;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Posicio;

import java.util.*;

/**
 * Heurística avançada (admisible i consistent, multiagent):
 *
 * h(s) = min_{agent a} L1(a, sortida)
 * on L1(x,y) = |x1 - y1| + |x2 - y2| (distància Manhattan).
 *
 * Propietats:
 *  - Admisible: Manhattan és una cota inferior del nombre de moviments en una graella 4-dir amb cost 1.
 *    Si hi ha portes tancades, el cost real només pot ser >= que Manhattan, mai menor.
 *  - Consistent: satisfa la desigualtat triangular en aquest graf.
 *  - Multiagent: com que la meta s’assoleix quan QUALSSEVOL agent arriba a la sortida,
 *    prenem el mínim entre tots els agents.
 *
 * Cost: O(#agents) per estat. És molt ràpida per A* i millora substancialment sobre heurístiques trivials.
 */
public class HeuristicaAvancada implements Heuristica {

    @Override
    public int h(Mapa estat) {
        if (estat.esMeta()) return 0;

        Posicio goal = estat.getSortidaPosicio();
        int best = Integer.MAX_VALUE;

        for (Posicio p : estat.getAgents()) {
            int d = Math.abs(p.x - goal.x) + Math.abs(p.y - goal.y);
            if (d < best) best = d;
        }

        // Si no hi ha agents (no hauria de passar), retornem 0 per seguretat.
        return (best == Integer.MAX_VALUE) ? 0 : best;
    }
}
