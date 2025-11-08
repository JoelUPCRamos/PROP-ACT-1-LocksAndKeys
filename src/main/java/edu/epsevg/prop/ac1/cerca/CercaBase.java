package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.Mapa;
import edu.epsevg.prop.ac1.model.Moviment;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** Utilidades comunes y contratos para búsquedas. */
public abstract class CercaBase extends Cerca {

    public CercaBase(boolean usarLNT) { super(usarLNT); }

    protected List<Moviment> reconstruirCami(Node goal) {
        LinkedList<Moviment> cami = new LinkedList<>();
        for (Node n = goal; n != null && n.pare != null; n = n.pare) cami.addFirst(n.accio);
        return cami;
    }

    protected List<Node> expandir(Node actual, ResultatCerca rc) {
        List<Node> fills = new ArrayList<>();
        for (Moviment mv : actual.estat.getAccionsPossibles()) {
            try {
                Mapa next = actual.estat.mou(mv);
                fills.add(new Node(next, actual, mv, actual.depth + 1, actual.g + 1));
            } catch (IllegalArgumentException ex) {
                rc.incNodesTallats(); // moviment invàlid
            }
        }
        return fills;
    }

    /** Control de cicles per branca (usarLNT=false). */
    protected boolean estaAlaBranca(Node node, Mapa estat) {
        for (Node p = node; p != null; p = p.pare) {
            if (p.estat.equals(estat)) return true;
        }
        return false;
    }

    /** Actualiza memoria pico en rc usando open + closed. */
    protected void updateMemoria(ResultatCerca rc, int openSize, int closedSize) {
        rc.updateMemoria(openSize + closedSize);
    }

    @Override
    public abstract void ferCerca(Mapa inicial, ResultatCerca rc);
}
