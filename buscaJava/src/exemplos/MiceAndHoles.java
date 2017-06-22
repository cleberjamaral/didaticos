package exemplos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import busca.Aleatorio;
import busca.Heuristica;
import busca.Estado;

/**
 * @author Cleber Jorge Amaral
 */
public class MiceAndHoles implements Estado, Heuristica, Aleatorio {

	public String getDescricao() {
		return "One day Masha came home and noticed n mice in the corridor of her flat.\n"
				+ "Of course, she shouted loudly, so scared mice started to run to the holes\n"
				+ " in the corridor.\n"
				+ "The corridor can be represeted as a numeric axis with n mice and m holes on it.\n"
				+ "ith mouse is at the coordinate xi, and jth hole — at coordinate pj. jth hole\n"
				+ "has enough room for cj mice, so not more than cj mice can enter this hole.\n"
				+ "What is the minimum sum of distances that mice have to go through so that\n"
				+ " they all can hide in the holes? If ith mouse goes to the hole j, then its\n"
				+ "distance is |xi - pj|.";
	}

	/** atributos do estado */
	List<Integer> origPosition = new ArrayList<Integer>(); // Usado em custo
	List<Integer> shorterDistance = new ArrayList<Integer>(); // Usado em heurística
	List<Integer> micePosition = new ArrayList<Integer>(); // Posição atual do rato
	List<Integer> holeCapacity = new ArrayList<Integer>(); // Capacidade atual dos buracos
	int ithMice = -1; // Comeca pelo -1 (a geracao de sucessores incrementa)
	int custoAcumulado = 0;
	static boolean debug = true;
	static boolean showMiceMap = false;
	static int heuristicaAtiva = 2;

	/** operacao que gerou o estado */

	/** Cria cenario inicial */
	public MiceAndHoles(int mP[], int hC[], int heuristica) {
		int maxMicePosition = 0;
		
		//Seta a heurística que deve ser utilizada nos ensaios
		heuristicaAtiva = heuristica;

		// Cria lista de ratos
		if (debug)
			System.out.print("micePosition(MiceAndHoles): ");
		for (int i = 0; i < mP.length; i++) {
			micePosition.add(mP[i]);
			origPosition.add(mP[i]);
			if (mP[i] > maxMicePosition)
				maxMicePosition = mP[i];
			if (debug)
				System.out.print(micePosition.get(i) + " ");
		}

		// Cria lista de buracos, caso haja ratos em buracos inexistentes, cria
		// buracos com capacidade zero
		for (int i = 0; i < hC.length; i++)
			holeCapacity.add(hC[i]);
		for (int i = hC.length; i <= maxMicePosition; i++)
			holeCapacity.add(0);

		// Subtrai a capacidade onde há ratos e atualiza heurística shorterDistance
		if (debug)
			System.out.print("shorterDistance(MiceAndHoles): ");
		for (int i = 0; i < micePosition.size(); i++) {
			holeCapacity.set(micePosition.get(i),
					holeCapacity.get(micePosition.get(i)) - 1);

			//Atualiza heurística marcando o buraco mais próximo ao rato corrente
			int closestDist = Integer.MAX_VALUE;
			for (int j = 0; j < holeCapacity.size(); j++){
				if ((holeCapacity.get(j) > 0) && (Math.abs(origPosition.get(i) - j) < closestDist)){
					closestDist = Math.abs(origPosition.get(i) - j);
				}
			}
			shorterDistance.add(closestDist);
			if (debug)
				System.out.print(shorterDistance.get(i) + " ");
		}

		// Imprime mapa de capacidade atualizado
		if (debug)
			System.out.print("holeCapacity(MiceAndHoles): ");
		for (int i = 0; i < holeCapacity.size(); i++)
			if (debug)
				System.out.print(holeCapacity.get(i) + " ");
		if (debug)
			System.out.print("\n");

	}

	public boolean ehMeta() {

		//Gambiarra isso estar aqui, deveria estar em outro local do código
		calculaCustoAcumuladoG();

		// Se algum buraco estiver superlotado, não é meta!
		for (int i = 0; i < holeCapacity.size(); i++) {
			if (holeCapacity.get(i) < 0) {
				return false;
			}
		}

		return true;
	}
	
    /**
     * Custo para geracao deste estado
     * (nao e o custo acumulado --- g)
     */
	public int custo() {
		//Não calcula se for a posição -1 inicial
		if (ithMice >= 0)
			return Math.abs(micePosition.get(ithMice) - origPosition.get(ithMice));
		else	
			return 0;
	}

	/** Lista de sucessores */
	public List<Estado> sucessores() {

		// Para o rato selecionado, crie sucessores para as possiveis posicoes
		// que ele pode ocupar
		if (ithMice < micePosition.size()-1) {
			ithMice++;
			if (debug) {
				System.out
						.print("\nSucessores para mice(" + ithMice + ") de [");
				for (int i = 0; i < micePosition.size(); i++)
					System.out.print(micePosition.get(i) + " ");
				System.out.print("]\n");
			}
		}
		else {
			// Esta situação é estranha, precisa ser verificado se faz sentido
			// mesmo zerar ithMice;
			//Sem isso o BPI da erro
			ithMice = 0;
			if (debug) {
				System.out
						.print("\nSUCESSORES para mice(" + ithMice + ") de [");
				for (int i = 0; i < micePosition.size(); i++)
					System.out.print(micePosition.get(i) + " ");
				System.out.print("]\n");
			}
		}

		List<Estado> suc = new LinkedList<Estado>(); // Lista de sucessores

		for (int i = 0; i < holeCapacity.size(); i++) {
			MiceAndHoles novo = new MiceAndHoles(this);

			// Desaloca buracos da copia
			for (int j = 0; j < micePosition.size(); j++) {
				novo.holeCapacity.set(novo.micePosition.get(j),
						novo.holeCapacity.get(novo.micePosition.get(j)) + 1);
			}

			// Movimenta rato da copia
			novo.ithMice = ithMice;
			novo.micePosition.set(ithMice, i);

			// Realoca buracos até o rato que está sendo processado
			for (int j = 0; j <= ithMice; j++) {
				novo.holeCapacity.set(novo.micePosition.get(j),
						novo.holeCapacity.get(novo.micePosition.get(j)) - 1);
			}

			if (!novo.poda()) {
				// Desaloca buracos da copia
				for (int j = 0; j <= ithMice; j++) {
					novo.holeCapacity
							.set(novo.micePosition.get(j), novo.holeCapacity
									.get(novo.micePosition.get(j)) + 1);
				}

				// Realoca buracos
				for (int j = 0; j < micePosition.size(); j++) {
					novo.holeCapacity
							.set(novo.micePosition.get(j), novo.holeCapacity
									.get(novo.micePosition.get(j)) - 1);
				}

				suc.add(novo);
				
			}
		}

		// Retornar a lista de Sucessores
		return suc;
	}

	/**
	 * cria um estado inicial a partir de outro (copia)
	 */
	MiceAndHoles(MiceAndHoles modelo) {
		for (int i = 0; i < modelo.micePosition.size(); i++) {
			micePosition.add(modelo.micePosition.get(i));
			shorterDistance.add(modelo.shorterDistance.get(i));
			origPosition.add(modelo.origPosition.get(i));
		}
		for (int i = 0; i < modelo.holeCapacity.size(); i++)
			holeCapacity.add(modelo.holeCapacity.get(i));
	}

	public String toString() {
		String r = "\n";

		if (debug) {
			// Forma de impressão do relatório 1
			r += "\t[" + ithMice + "/" + (micePosition.size()-1) + "] \n";
			r +=  "Orig position: ";
			for (int j = 0; j < origPosition.size(); j++)
				r += origPosition.get(j) + " ";
			r += "\n";
			r +=  "Mice position: ";
			for (int j = 0; j < micePosition.size(); j++)
				r += micePosition.get(j) + " ";
			r += "\n";
			r += "Hole Capacity: ";
			for (int j = 0; j < holeCapacity.size(); j++)
				r += holeCapacity.get(j) + " ";
			r += "\n";
			r += "Individ. costs: ";
			for (int j = 0; j < micePosition.size(); j++)
				r += Math.abs(micePosition.get(j) - origPosition.get(j)) + " ";
			r += "\n";
		}
		
		if (showMiceMap) {
			// Forma de impressão do relatório 2
			for (int j = 0; j < holeCapacity.size(); j++) {
				r += "\tHole(" + j + "), capacity: " + holeCapacity.get(j)
						+ " ";
				for (int i = 0; i < micePosition.size(); i++)
					if (micePosition.get(i) == j)
						r += "Mice(" + i + ") ";
				r += "\n";
			}
		}
		r += "\t[" + ithMice + "/" + (micePosition.size()-1) +
				"] Custo total heurística("+heuristicaAtiva+"): " + custoAcumulado() + "\n";
		System.out.print(r);
		return r;
	}

	private void calculaCustoAcumuladoG() {
		//Calcula custo g até o rato atual.
		int calculaCustoAcc = 0;
		for (int j = 0; j < micePosition.size(); j++)
			calculaCustoAcc += Math.abs(micePosition.get(j) - origPosition.get(j));
		custoAcumulado = calculaCustoAcc;
	}

	/**
	 * Verifica se um estado eh igual a outro ja inserido na lista de sucessores
	 * (usado para poda)
	 */
	public boolean equals(Object o) {
		try {
			if (o instanceof MiceAndHoles) {
				MiceAndHoles e = (MiceAndHoles) o;
				//Se qualquer rato estiver numa posição diferente este estado é diferente
				for (int i = 0; i < micePosition.size(); i++)
					if (this.micePosition.get(i) != e.micePosition.get(i))
						return false;
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * retorna o hashCode desse estado (usado para poda, conjunto de fechados)
	 */

	public int hashCode() {
		return toString().hashCode();
	}

	public int h() {
		//Se a heurística selecionada é a de custo padrão (que não faz muito sentido!)
		int estimativa = 0;
		if (heuristicaAtiva == 1){
			for (int i = 0; i < micePosition.size(); i++) {
				if (micePosition.get(i) != origPosition.get(i))
					estimativa += Math.abs(micePosition.get(i)
							- origPosition.get(i));
			}
		} else {
			//Se a heurística é baseada no custo de cada rato não computado de chegar ao buraco mais próximo
			int i;
			if (ithMice > 0) i = ithMice; else i = 0;
			for (; i < shorterDistance.size(); i++) {
				estimativa += shorterDistance.get(i);
			}
		}
		return estimativa;
	}

	/**
	 * retorna true se o estado deve ser podado
	 */
	protected boolean poda() {
		// Se o buraco que este rato esta tentando usar ou de seus antecessores
		// estiver superlotado, pode podar!
		for (int i = 0; i <= ithMice; i++) {
			if (holeCapacity.get(micePosition.get(i)) < 0) {
				return true;
			}
		}
		return false;
	}

	public Estado geraAleatorio() {
		MiceAndHoles aleatorio = new MiceAndHoles(this);

		// Desaloca buracos da copia
		for (int j = 0; j < aleatorio.micePosition.size(); j++) {
			aleatorio.holeCapacity
					.set(micePosition.get(j), aleatorio.holeCapacity
							.get(aleatorio.micePosition.get(j)) + 1);
		}

		// Reposiciona ratos aleatoriamente
		for (int i = 0; i < aleatorio.micePosition.size(); i++)
			aleatorio.micePosition.set(i,
					Math.round((float) (Math.random() * (aleatorio.holeCapacity
							.size() - 1))));

		if (debug) {
			System.out.print("\nNovo posicionamento aleatório: ");
			for (int i = 0; i < aleatorio.micePosition.size(); i++)
				System.out.print(aleatorio.micePosition.get(i) + " ");
			System.out.print("\n");
		}

		// Realoca buracos
		for (int j = 0; j < aleatorio.micePosition.size(); j++) {
			aleatorio.holeCapacity
					.set(aleatorio.micePosition.get(j), aleatorio.holeCapacity
							.get(aleatorio.micePosition.get(j)) - 1);
		}

		// Atualiza o ithMice, se já está alocado -> incrementa
		int j = 0;
		for (; j < aleatorio.micePosition.size(); j++) 
			if (aleatorio.holeCapacity.get(aleatorio.micePosition.get(j)) < 0) 
				break;
		aleatorio.ithMice = j;
		
		aleatorio.calculaCustoAcumuladoG();
			
		return aleatorio;
	}

	/**
     * Custo acumulado g
     */
    public int custoAcumulado(){
		return custoAcumulado;
    	
    }
	
}


