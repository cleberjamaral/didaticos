package exemplos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import busca.Aleatorio;
import busca.Heuristica;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

/**
 * @author Cleber Jorge Amaral 
 */
public class MiceAndHoles implements Estado, Heuristica, Aleatorio{

	public String getDescricao() {
		return
				"One day Masha came home and noticed n mice in the corridor of her flat.\n"+ 
				"Of course, she shouted loudly, so scared mice started to run to the holes\n"+
				" in the corridor.\n"+
				"The corridor can be represeted as a numeric axis with n mice and m holes on it.\n"+
				"ith mouse is at the coordinate xi, and jth hole — at coordinate pj. jth hole\n"+
				"has enough room for cj mice, so not more than cj mice can enter this hole.\n"+
				"What is the minimum sum of distances that mice have to go through so that\n"+
				" they all can hide in the holes? If ith mouse goes to the hole j, then its\n" +
				"distance is |xi - pj|.";
	}

	/** atributos do estado */
	List<Integer> origPosition = new ArrayList<Integer>(); //Usado para calcular o cuso
	List<Integer> micePosition = new ArrayList<Integer>(); //Armazena a posição atual de cada rato
	List<Integer> holeCapacity = new ArrayList<Integer>(); //A capacidade de cada buraco dadas as posições dos ratos
	int ithMice = -1; //Comeca pelo Buraco -1 pois a geracao de sucessores incrementa
	/** operacao que gerou o estado */

	/** Cria cenario inicial*/
	public MiceAndHoles(int mP[], int hC[]) {
		int maxMicePosition = 0;
		
		//Cria lista de ratos
		System.out.print("micePosition(MiceAndHoles): ");
		for (int i = 0; i < mP.length; i++) {
			micePosition.add(mP[i]);
			origPosition.add(mP[i]);
			if (mP[i] > maxMicePosition) maxMicePosition = mP[i];
			System.out.print(micePosition.get(i)+" ");
		}
		
		//Cria lista de buracos, caso haja ratos em buracos inexistentes, cria buracos com capacidade zero
		for (int i = 0; i < hC.length; i++)
			holeCapacity.add(hC[i]);
		for (int i = hC.length; i <= maxMicePosition; i++)
			holeCapacity.add(0);
		
		//Subtrai a capacidade onde há ratos
		for (int i = 0; i < micePosition.size(); i++)
			holeCapacity.set(micePosition.get(i), holeCapacity.get(micePosition.get(i))-1);
		
		//Imprime mapa de capacidade atualizado
		System.out.print("holeCapacity(MiceAndHoles): ");
		for (int i = 0; i < holeCapacity.size(); i++) 
			System.out.print(holeCapacity.get(i)+" ");
		System.out.print("\n");
	}

	public boolean ehMeta(){
		//Se algum buraco estiver superlotado, não é meta!
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
	public List<Estado> sucessores(){

		//Para o rato selecionado, crie sucessores para as possiveis posicoes que ele pode ocupar
		if (ithMice < micePosition.size()-1) {
			ithMice++;
			System.out.print("\nSucessores para mice("+ithMice+") de [");
			for (int i = 0;i < micePosition.size(); i++) System.out.print(micePosition.get(i)+" ");
			System.out.print("]\n");
		}
		else{
			//Esta situação é estranha, precisa ser verificado se faz sentido mesmo zerar ithMice;
			ithMice = 0;
			System.out.print("\nSucessores para mice("+ithMice+") de [");
			for (int i = 0;i < micePosition.size(); i++) System.out.print(micePosition.get(i)+" ");
			System.out.print("]\n");
		}

		List<Estado> suc = new LinkedList<Estado>(); //Lista de sucessores

		for (int i = 0; i < holeCapacity.size();i++){
			MiceAndHoles novo = new MiceAndHoles(this);
			
			//Desaloca buracos da copia
			for (int j = 0; j < micePosition.size(); j++){
				novo.holeCapacity.set(novo.micePosition.get(j), novo.holeCapacity.get(novo.micePosition.get(j))+1);
			}
			
			//Movimenta rato da copia
			novo.ithMice = ithMice;
			novo.micePosition.set(ithMice,i);
			//novo.holeCapacity.set(i, novo.holeCapacity.get(i)-1);
			if (!novo.poda()){
				//Realoca buracos
				for (int j = 0; j < micePosition.size(); j++){
					novo.holeCapacity.set(novo.micePosition.get(j), novo.holeCapacity.get(novo.micePosition.get(j))-1);
				}
				suc.add(novo);
			}
		}

		//Retornar a lista de Sucessores
		return suc;
	}

	/**
	 *  cria um estado inicial a partir de outro (copia)
	 */
	MiceAndHoles(MiceAndHoles modelo) {
		for (int i = 0; i < modelo.micePosition.size(); i++){
			micePosition.add(modelo.micePosition.get(i));
			origPosition.add(modelo.origPosition.get(i));
		}
		for (int i = 0; i < modelo.holeCapacity.size(); i++)
			holeCapacity.add(modelo.holeCapacity.get(i));
	}
	
	public String toString() {
		StringBuffer r = new StringBuffer("\n");
		
		//Forma de impressão do relatório 1
		r.append("\tMice position: ");
		for (int j=0; j < micePosition.size();j++) 
			r.append(micePosition.get(j)+" ");
		r.append("Hole Capacity: ");
		for (int j=0; j < holeCapacity.size();j++) 
			r.append(holeCapacity.get(j)+" ");
		r.append("\n");

		//Forma de impressão do relatório 2
		for (int j=0; j < holeCapacity.size();j++) {
			r.append("\tHole("+j+"), capacity: "+holeCapacity.get(j)+" ");
			for (int i=0; i < micePosition.size();i++) 
				if (micePosition.get(i) == j) r.append("Mice("+i+") ");
			r.append("\n");
		}
		
		r.append("Custo total: "+h()+"\n");
		System.out.print(r);
		return r.toString();
	}


	public static void main(String[] a) throws IOException {
		int micePosition[] = {4,0,6,7}; //Originalmente: 5 1 7 8 transformado em indices
		int holeCapacity[] = {0,1,6,7,0};

		System.out.print("Iniciando... \n");
		MiceAndHoles inicial = new MiceAndHoles(micePosition,holeCapacity);

		String str;
		BufferedReader teclado;
		teclado = new BufferedReader(new InputStreamReader(System.in));

		Nodo n = null;

		str = "0";
		while (!str.equals("S")){
			System.out.print("Digite sua opcao de busca { Digite S para finalizar }\n");
			System.out.print("\t1  -  Largura\n");
			System.out.print("\t2  -  Profundidade\n");
			System.out.print("\t3  -  Pronfundidade Iterativo\n");
			System.out.print("Opcao: ");
			str = teclado.readLine().toUpperCase();

			if (str.equals("1")){
				System.out.println("Busca em Largura");
				n = new BuscaLargura().busca(inicial);
			}else{
				if (str.equals("2")){
					System.out.println("Busca em Profundidade");
					n = new BuscaProfundidade(20).busca(inicial);
				}else{
					if (str.equals("3")){
						System.out.println("Busca em Profundidade Iterativo");
						n = new BuscaIterativo().busca(inicial);
					}
				}
			}

			if (n == null) {
                System.out.println("Sem Solucao!");
            } else {
                System.out.println("Solucao:\n" + n.montaCaminho() + "\n\n");
            }
		}
	}

	/** Verifica se um estado eh igual a outro ja inserido na lista de sucessores (usado para poda) */
	public boolean equals(Object o) {
		try {
			if (o instanceof MiceAndHoles) {
				MiceAndHoles e = (MiceAndHoles)o;
				for (int i=0;i<micePosition.size();i++) if (this.micePosition.get(i) != e.micePosition.get(i)) return false;
				//for (int i=0;i<holeCapacity.size();i++) if (this.holeCapacity.get(i) != e.holeCapacity.get(i)) return false;
				//System.out.print("Equals!\n");
				return true;
			}
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 
	 * retorna o hashCode desse estado
	 * (usado para poda, conjunto de fechados)
	 */

	public int hashCode() {
		return toString().hashCode();
	}

	public int h() {
		int movimentacao = 0;
		for (int i = 0; i < micePosition.size(); i++){
			if (micePosition.get(i) != origPosition.get(i))
				movimentacao += Math.abs(micePosition.get(i) - origPosition.get(i)); 
		}
	
		//System.out.print("h: "+movimentacao);
		return movimentacao;
	}

    /**
     * retorna true se o estado deve ser podado
     */
    protected boolean poda() {
    	/*
    	//Copia capacidades para uma lista temporária para testar inclusive os antecessores
		//System.out.print("holeTest: ");
    	List<Integer> holeTest = new ArrayList<Integer>();
    	for (int i = 0; i < holeCapacity.size(); i++){
    		holeTest.add(holeCapacity.get(i)); 
			//System.out.print(holeTest.get(i)+" ");
    	}
		//System.out.print("\n");

    	//Se o buraco que este rato esta tentando usar ou de seus antecessores estiver superlotado, pode podar!
		//System.out.print("Mices("+ithMice+"): ");
    	for (int i = 0; i <= ithMice; i++) {
    		//System.out.print(micePosition.get(i)+"  ");

    		//System.out.print("HOLETest: ");
        	//for (int j = 0; j < holeCapacity.size(); j++){
    			//System.out.print(holeTest.get(j)+" ");
        	//}
    		//System.out.print("\n");
    		
    		if (holeTest.get(micePosition.get(i)) < 0) {
    			//System.out.print("\nPodar!\n");
    			return true;
    		}
    		//System.out.print("\n");
    	}
   		
    	return false;
    	*/

    	//Se algum buraco estiver superlotado, pode podar!
   		if (holeCapacity.get(micePosition.get(ithMice))-1 < 0) 
   			return true;
   		else
   			return false;
   		 
    }

	public Estado geraAleatorio() {
		MiceAndHoles aleatorio = new MiceAndHoles(this);

		//Desaloca buracos da copia
		for (int j = 0; j < aleatorio.micePosition.size(); j++){
			aleatorio.holeCapacity.set(micePosition.get(j), aleatorio.holeCapacity.get(aleatorio.micePosition.get(j))+1);
		}

		//Reposiciona ratos aleatoriamente
		for (int i = 0;i < aleatorio.micePosition.size();i++)
			aleatorio.micePosition.set(i,Math.round( (float)(Math.random()*(aleatorio.holeCapacity.size()-1))));

		//DEBUG: Novo posicionamento dos ratos
		System.out.print("\nNovo posicionamento aleatório: ");
		for (int i = 0;i < aleatorio.micePosition.size();i++)
			System.out.print(aleatorio.micePosition.get(i)+" ");
		System.out.print("\n");
		
		//Realoca buracos
		for (int j = 0; j < aleatorio.micePosition.size(); j++){
			aleatorio.holeCapacity.set(aleatorio.micePosition.get(j), aleatorio.holeCapacity.get(aleatorio.micePosition.get(j))-1);
		}
		return aleatorio;
	}
}