package exemplos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import busca.Heuristica;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

/**
 * @author Cleber Jorge Amaral 
 */
public class CopyOfMiceAndHoles implements Estado, Heuristica{

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
	List<Integer> micePosition = new ArrayList<Integer>();
	List<Integer> holeCapacity = new ArrayList<Integer>();
	int ithMice = -1; //Comeca pelo Buraco -1 pois a geracao de sucessores incrementa
	/** operacao que gerou o estado */

	/** Cria cenario inicial*/
	public CopyOfMiceAndHoles(int mP[], int hC[]) {
		int maxMicePosition = 0;
		for (int i = 0; i < mP.length; i++) {
			micePosition.add(mP[i]);
			if (mP[i] > maxMicePosition) maxMicePosition = mP[i]; //Cria holes com capacidade 0 onde houver ratos
		}
		for (int i = 0; i < hC.length; i++)
			holeCapacity.add(hC[i]);
		for (int i = hC.length; i <= maxMicePosition; i++)
			holeCapacity.add(0);
		System.out.print("holeCapacity(MiceAndHoles): ");
		for (int i = 0; i < holeCapacity.size(); i++) 
			System.out.print(holeCapacity.get(i)+" ");
		System.out.print("\n");
	}

	public boolean ehMeta(){
		List<Integer> checkCapacity = new ArrayList<Integer>();
		for (int i = 0; i < holeCapacity.size(); i++) 
			checkCapacity.add(holeCapacity.get(i));
		for (int i = 0; i < micePosition.size(); i++) 
			checkCapacity.set(micePosition.get(i), checkCapacity.get(micePosition.get(i))-1);
		for (int i = 0; i < checkCapacity.size(); i++) 
			System.out.print(checkCapacity.get(i)+" ");

		System.out.print("Testando se é meta...\n");
		for (int i = 0; i < checkCapacity.size(); i++) {
			if (checkCapacity.get(i) < 0) {
				System.out.print("Não é meta, superlotado! i:"+i+" Capacity:"+holeCapacity.get(i)+"\n");
				return false;
			}
		}
		System.out.print("Meta alcançada! ");
		for (int i = 0; i < micePosition.size(); i++)
			System.out.print("Mice("+i+"): "+micePosition.get(i)+" ");
		System.out.print("\n");
		
		return true;
	}

	public int custo() {
		return 1;
	}

	/** Lista de sucessores */
	public List<Estado> sucessores(){
		System.out.print("sucessores...\n");

		//Para o rato selecionado, crie sucessores para as possiveis posicoes que ele pode ocupar
		if (ithMice < micePosition.size()) 
			ithMice++;
		else
			return null;

		List<Estado> suc = new LinkedList<Estado>(); //Lista de sucessores

		for (int i = 0; i < holeCapacity.size();i++){
			CopyOfMiceAndHoles novo = new CopyOfMiceAndHoles(this);
			novo.ithMice = ithMice;
			novo.micePosition.set(ithMice,i);
			if (!novo.poda()){
				novo.holeCapacity.set(novo.micePosition.get(ithMice), novo.holeCapacity.get(i)-1);
				System.out.print("Adicionado nó Mice:"+novo.ithMice+" Hole:"+i+"\n");
				suc.add(novo);
			}else{
				System.out.print("Podado nó Mice:"+novo.ithMice+" Hole:"+i+"\n");
			}
		}

		//Retornar a lista de Sucessores
		return suc;
	}

	/**
	 *  cria um estado inicial a partir de outro (copia)
	 */
	CopyOfMiceAndHoles(CopyOfMiceAndHoles modelo) {
		for (int i = 0; i < modelo.micePosition.size(); i++)
			micePosition.add(modelo.micePosition.get(i));
		for (int i = 0; i < modelo.holeCapacity.size(); i++)
			holeCapacity.add(modelo.holeCapacity.get(i));
	}
	
	/** Lista de antecessores, para busca bidirecional */
	public List<Estado> antecessores(){
		System.out.print("antecessores...\n");

		return sucessores();
	}

	public String toString() {
		StringBuffer r = new StringBuffer("\n");
		r.append("Mices(toString): ");
		for (int i=0;i<micePosition.size();i++) {
			if (holeCapacity.get(micePosition.get(i)) > 0)
				r.append("Mice("+i+"):"+micePosition.get(i)+" ok! ");
			else
				r.append("Mice("+i+"):"+micePosition.get(i)+" NO! ");
		}
		r.append("\n");
		System.out.print(r);
		return r.toString();
	}


	public static void main(String[] a) throws IOException {
		int micePosition[] = {4,0,6,7}; //Originalmente: 5 1 7 8 transformado em indices
		int holeCapacity[] = {0,1,6,7,0};

		System.out.print("Iniciando... \n");
		CopyOfMiceAndHoles inicial = new CopyOfMiceAndHoles(micePosition,holeCapacity);

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
			if (o instanceof CopyOfMiceAndHoles) {
				CopyOfMiceAndHoles e = (CopyOfMiceAndHoles)o;
				for (int i=0;i<micePosition.size();i++) if (this.micePosition.get(i) != e.micePosition.get(i)) return false;
				for (int i=0;i<holeCapacity.size();i++) if (this.holeCapacity.get(i) != e.holeCapacity.get(i)) return false;
				System.out.print("Equals!\n");
				//return true;
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
		//System.out.print("hashCode...\n");
		return toString().hashCode();
	}

	public int h() {
		// TODO Auto-generated method stub
		return 0;
	}

    /**
     * retorna true se o estado deve ser podado
     */
    protected boolean poda() {
    	/*Se algum buraco estiver superlotado, pode podar!*/
   		if (holeCapacity.get(micePosition.get(ithMice))-1 < 0) 
   			return true;
   		else
   			return false;
    }
    
	/**
     * Custo acumulado g
     */
    public int custoAcumulado(){
		return 0;
    }

}