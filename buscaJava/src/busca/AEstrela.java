package busca;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *   Algoritmos de Busca A*
 *
 *   @author Jomi Fred Hubner
 */
public class AEstrela extends BuscaHeuristica {
    
	int maxF = -1; // max F
    int maxAbertos = -1; // max abertos
	Nodo theBest;

    /** busca sem mostrar status */
    public AEstrela() {
    }
    
    /** busca mostrando status */
    public AEstrela(MostraStatusConsole ms) {
        super(ms);
    }
	
	/** seta o limite para f(), -1 � ilimitado */
	public void setMaxF(int m) {
		maxF = m;
	}

    /** seta o limite para o nro de abertos, -1 � ilimitado */
    public void setMaxAbertos(int m) {
        maxAbertos = m;
    }
	
	public Nodo getTheBest() {
		return theBest;
	}
	
    /**
     *
     * Busca a solu��o por busca em heur�stica.
     *                              ----------
     * (baseado no Russel & Norvig)
     */
    public Nodo busca(Estado inicial) {
        status.inicia();
        initFechados();
        
        //Priority_Queue abertos = new Heap(100, getNodoComparatorF()); // lista ordenada por f()
        Queue<Nodo> abertos = new PriorityQueue<Nodo>(100, getNodoComparatorF()); // lista ordenada por f()
        Nodo nInicial = new Nodo(inicial, null);
        abertos.add(nInicial);
        theBest = nInicial; // o melhor nodo j� gerado
        
        while (!parar && abertos.size() > 0) {
            
            Nodo melhor = abertos.remove();
            status.explorando(melhor, abertos.size());
            if (melhor.estado.ehMeta()) {
                /*if (melhorCustoAcumulado == 0) 
                	melhorCustoAcumulado = melhor.estado.custoAcumulado();
                else if (melhorCustoAcumulado > melhor.estado.custoAcumulado())
                	melhorCustoAcumulado = melhor.estado.custoAcumulado();
				*/
                //Não deveria estar entrando aqui quando encontrasse mais de uma solução (baseado nos nodos ainda abertos)? 
                System.out.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * Meta encontrada!!! * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n");
                
                status.termina(true);
                return melhor;
            }
            
            if (maxF < 0 || melhor.f() < maxF) {
                abertos.addAll( sucessores(melhor) );
            }
            if (maxAbertos > 0 && abertos.size() > maxAbertos) {
                break;
            }
            
            // o "the best" e o codigo que segue so para fins de interface
            if (melhor.f() < theBest.f()) {
                theBest = melhor;
                //print("\nMelhor (em profundidade "+melhor.getProfundidade()+", h="+((Heuristica)theBest.estado).h()+")="+melhor);
            	//Esta busca poderia estar sendo chamada da BPI que faria com que fossem geradas mais soluções?

            }
            
        }
        status.termina(false);
        return null;
    }
    
    public String toString() {
    	return "A* - busca heuristica"; 
    }
    
}
