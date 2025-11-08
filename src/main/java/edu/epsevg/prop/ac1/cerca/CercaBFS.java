package edu.epsevg.prop.ac1.cerca;

import edu.epsevg.prop.ac1.model.*;
import edu.epsevg.prop.ac1.resultat.ResultatCerca;

import java.util.*;

public class CercaBFS extends Cerca {
    public CercaBFS(boolean usarLNT) { super(usarLNT); }

    
    private List<Moviment> reconstruirCami(Node nodeFinal) {
        List<Moviment> cami = new LinkedList<>();
        Node actual = nodeFinal;
        while (actual != null && actual.accio != null) {
            cami.add(0, actual.accio); // Afegim al principi per obtenir l'ordre correcte
            actual = actual.pare;
        }
        return cami;
    }
    @Override
    public void ferCerca(Mapa inicial, ResultatCerca rc) {
       // LNT: Map<Mapa, Integer> on Integer és la profunditat mínima trobada (si usarLNT=true)
        Map<Mapa, Integer> nodesTancats = usarLNT ? new HashMap<>() : null;
        
        // LNO: Llista de Nodes Oberts (Queue per a BFS)
        Queue<Node> nodesOberts = new LinkedList<>();

        Node nodeInicial = new Node(inicial, null, null, 0, 0);
        nodesOberts.add(nodeInicial);
        
        // Registre inicial de l'estat si s'usa LNT
        if (usarLNT) {
            nodesTancats.put(inicial, 0);
        }
        
        // Bucle principal de cerca
        while (!nodesOberts.isEmpty()) {
            // CRIDA CORREGIDA: incNodesExplorats()
            rc.incNodesExplorats(); 
            Node actual = nodesOberts.poll();

            // 1. Comprovació d'objectiu
            if (actual.estat.esMeta()) {
                // CRIDA CORREGIDA: setCami(List<Moviment>)
                rc.setCami(reconstruirCami(actual)); 
                return;
            }

            // 2. Generació de successors
            List<Moviment> accions = actual.estat.getAccionsPossibles();
            
            for (Moviment accio : accions) {
                try {
                    Mapa estatSuccesor = actual.estat.mou(accio);
                    int novaProfunditat = actual.depth + 1;
                    int nouCost = actual.g + 1;

                    // Creem el node
                    Node nodeSuccesor = new Node(estatSuccesor, actual, accio, novaProfunditat, nouCost);
                    
                    // 3. Control de cicles

                    if (usarLNT) {
                        // Control de cicles per LNT (usarLNT=true)
                        Integer profunditatAnterior = nodesTancats.get(estatSuccesor);
                        
                        // Si ja s'ha visitat a menor o igual profunditat, es descarta (pruning)
                        if (profunditatAnterior != null && profunditatAnterior <= novaProfunditat) {
                            // CRIDA CORREGIDA: incNodesTallats()
                            rc.incNodesTallats(); 
                            continue;
                        }
                        
                        // Si s'ha visitat a major profunditat o no s'ha visitat, s'afegeix
                        nodesTancats.put(estatSuccesor, novaProfunditat);
                    } 
                    else {
                        // Control de cicles dins de la branca actual (usarLNT=false)
                        Node nodePunter = actual;
                        boolean cicleTrobat = false;
                        while(nodePunter != null) {
                            if (nodePunter.estat.equals(estatSuccesor)) {
                                cicleTrobat = true;
                                break;
                            }
                            nodePunter = nodePunter.pare;
                        }
                        if (cicleTrobat) {
                            // CRIDA CORREGIDA: incNodesTallats()
                            rc.incNodesTallats();
                            continue;
                        }
                    }

                    // 4. Afegir a la LNO
                    nodesOberts.add(nodeSuccesor);

                } catch (IllegalArgumentException e) {
                    // Ignorem moviments no vàlids (col·lisions, murs, portes tancades).
                }
            }
            // CRIDA CORREGIDA: updateMemoria()
            int memoriaActual = nodesOberts.size() + (usarLNT ? nodesTancats.size() : 0);
            rc.updateMemoria(memoriaActual);
        }

        // Si la cua es buida i no hem trobat la meta
        // CRIDA CORREGIDA: setCami(null)
        rc.setCami(null);
        

    }
   
}