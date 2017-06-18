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
	List<Integer> origPosition = new ArrayList<Integer>(); // Usado para
															// calcular o cuso
	List<Integer> micePosition = new ArrayList<Integer>(); // Armazena a posição
															// atual de cada
															// rato
	List<Integer> holeCapacity = new ArrayList<Integer>(); // A capacidade de
															// cada buraco dadas
															// as posições dos
															// ratos
	int ithMice = -1; // Comeca pelo -1 (a geracao de sucessores incrementa)
	int custoAcc = 0;
	boolean debug = false;

	/** operacao que gerou o estado */

	/** Cria cenario inicial */
	public MiceAndHoles(int mP[], int hC[]) {
		int maxMicePosition = 0;

		// Cria lista de ratos
		if (debug)
			System.out.print("micePosition(MiceAndHoles): ");
		for (int i = 0; i < mP.length; i++) {
			micePosition.add(0); // Começa todos em zero
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

		// Subtrai a capacidade onde há ratos
		for (int i = 0; i < micePosition.size(); i++)
			holeCapacity.set(micePosition.get(i),
					holeCapacity.get(micePosition.get(i)) - 1);

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
		// Se algum buraco estiver superlotado, não é meta!
		for (int i = 0; i < holeCapacity.size(); i++) {
			if (holeCapacity.get(i) < 0) {
				return false;
			}
		}

		return true;
	}

	public int custo() {
		return 1;
	}

	/** Lista de sucessores */
	public List<Estado> sucessores() {

		// Para o rato selecionado, crie sucessores para as possiveis posicoes
		// que ele pode ocupar
		if (ithMice < micePosition.size() - 1) {
			ithMice++;
			if (debug) {
				System.out
						.print("\nSucessores para mice(" + ithMice + ") de [");
				for (int i = 0; i < micePosition.size(); i++)
					System.out.print(micePosition.get(i) + " ");
				System.out.print("]\n");
			}
		} else {
			// Esta situação é estranha, precisa ser verificado se faz sentido
			// mesmo zerar ithMice;
			ithMice = 0;
			if (debug) {
				System.out
						.print("\nSucessores para mice(" + ithMice + ") de [");
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
			origPosition.add(modelo.origPosition.get(i));
		}
		for (int i = 0; i < modelo.holeCapacity.size(); i++)
			holeCapacity.add(modelo.holeCapacity.get(i));
	}

	public String toString() {
		String r = "\n";

		if (debug) {
			// Forma de impressão do relatório 1
			r += "\t[" + ithMice + "/" + micePosition.size() + "] Mice position: ";
			for (int j = 0; j < micePosition.size(); j++)
				r += micePosition.get(j) + " ";
			r += "Hole Capacity: ";
			for (int j = 0; j < holeCapacity.size(); j++)
				r += holeCapacity.get(j) + " ";
			r += "\n";
		}
		
		if (debug) {
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
		r += "\t[" + ithMice + "/" + micePosition.size() + "] Custo total: " + h() + "\n";
		System.out.print(r);
		return r;
	}

	/**
	 * Verifica se um estado eh igual a outro ja inserido na lista de sucessores
	 * (usado para poda)
	 */
	public boolean equals(Object o) {
		try {
			if (o instanceof MiceAndHoles) {
				MiceAndHoles e = (MiceAndHoles) o;
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
		custoAcc = 0;
		for (int i = 0; i < micePosition.size(); i++) {
			if (micePosition.get(i) != origPosition.get(i))
				custoAcc += Math.abs(micePosition.get(i)
						- origPosition.get(i));
		}

		return custoAcc;
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
		return aleatorio;
	}

	/**
     * Custo acumulado g
     */
    public int custoAcumulado(){
		return custoAcc;
    	
    }
	
}


