package busca;

import java.util.LinkedList;
import java.util.List;


/**
 *   Algoritmos de Busca (geral, qquer problema)
 *   Busca a solu��o por busca em profundidade.
 *
 *   @author Jomi Fred H�bner
 */
public class BuscaProfundidade extends Busca {

    protected int profMax = 1000;

    /** busca sem mostrar status */
    public BuscaProfundidade() {
    }

    public BuscaProfundidade(int m) {
        profMax = m;
    }
    
    /** busca mostrando status */
    public BuscaProfundidade(MostraStatusConsole ms) {
        super(ms);
    }
    public BuscaProfundidade(int m, MostraStatusConsole ms) {
        super(ms);
        profMax = m;
    }
    
	
	public void setProfMax(int m) {
		profMax = m;
	}
	
    public Nodo busca(Estado inicial) {
        status.inicia();
        initFechados();
        
        List<Nodo> abertos = new LinkedList<Nodo>();
        
        abertos.add(new Nodo(inicial, null));
        
        while (!parar && abertos.size() > 0) {
            
            Nodo n = abertos.remove(0);
            status.explorando(n,abertos.size());
            if (n.estado.ehMeta()) {
            	/*
            	//Esta busca poderia estar sendo chamada da BPI que faria com que fossem geradas mais soluções?
            	if ((n.estado.custoAcumulado() > 0) && (melhorCustoAcumulado == 0)) 
           			melhorCustoAcumulado = n.estado.custoAcumulado();
            	else if ((n.estado.custoAcumulado() > 0) && (n.estado.custoAcumulado() < melhorCustoAcumulado)) 
           			melhorCustoAcumulado = n.estado.custoAcumulado();
            	*/	
                status.termina(true);
                return n;
            }
        
            if (n.getProfundidade() < profMax) {
                abertos.addAll( 0, sucessores(n) );
            } else {
            	System.out.print("ERRO! Profundidade máxima alcançada!");
            	status.termina(false);
            	return null;
            }
        }
        status.termina(false);
        return null;
    }        
    
    public String toString() {
    	return "BP - Busca em Profundidade";
    }
}
