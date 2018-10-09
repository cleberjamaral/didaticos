package exemplos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import busca.Antecessor;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

/**
 * Solucao para o problema dos misssionarios e canibais.
 *
 * @author (Malcus Otavio Quinoto Imhof & Daniel Dalcastagne - 5. Semestre Matutino - BCC)
 * @version (02/04/2004)
 */
public class OrgDesigner1 implements Estado, Antecessor{
    
    public String getDescricao() {
        return "Empty\n";
    }
    
    public OrgDesigner1(GoalNode gn) {

    }
    
    public boolean ehMeta(){
    	return false;
    }
    
    public int custo() {
        return 1;
    }
    
    /** Lista de sucessores */
    public List<Estado> sucessores(){
        List<Estado> suc = new LinkedList<Estado>(); //Lista de sucessores
        
        //Retornar a lista de Sucessores
        return suc;
    }
    
    /** Lista de antecessores, para busca bidirecional */
    public List<Estado> antecessores(){
        return sucessores();
    }
    
    /** Verifica se o sucessor gerado e valido */
    public boolean ehValido(){
        return true;
    }
    
    
    public String toString() {
		return null;

    }

    
    public static void main(String[] a) throws IOException {
    	
    	GoalNode paintHouse = new GoalNode(null);
    		GoalNode contracting = new GoalNode(paintHouse);
    			GoalNode createArtifact = new GoalNode(contracting);
    				createArtifact.addSkill("createArtifact");
   		    	GoalNode getBids = new GoalNode(contracting);
    		    	GoalNode bidIPaint = new GoalNode(getBids);
    		    		bidIPaint.addSkill("bidPaint");
    		    		bidIPaint.addSkill("paint");
    		    	GoalNode bidEPaint = new GoalNode(getBids);
    		    		bidEPaint.addSkill("bidPaint");
    		    		bidEPaint.addSkill("paint");
    			GoalNode showWinner = new GoalNode(contracting);
    				showWinner.addSkill("show");
    				showWinner.addSkill("contract");
    	    GoalNode execute = new GoalNode(paintHouse);
        		GoalNode contractWinner = new GoalNode(execute);
        			contractWinner.addSkill("show");
        			contractWinner.addSkill("contract");
       		   	GoalNode iPaint = new GoalNode(execute);
       		   		iPaint.addSkill("bidPaint");
       		   		iPaint.addSkill("paint");
       		   	GoalNode ePaint = new GoalNode(execute);
       		   		ePaint.addSkill("bidPaint");
       		   		ePaint.addSkill("paint");
   
    	
        OrgDesigner1 inicial = new OrgDesigner1(paintHouse);
        
        String str;
        BufferedReader teclado;
        teclado = new BufferedReader(new InputStreamReader(System.in));
        
        Nodo n = null;
        
        System.out.print("Digite sua opcao de busca { Digite S para finalizar }\n");
        System.out.print("\t1  -  Largura\n");
        System.out.print("\t2  -  Profundidade\n");
        System.out.print("\t3  -  Pronfundidade Iterativo\n");
        System.out.print("Opcao: ");
        str = teclado.readLine().toUpperCase();
        while (!str.equals("S")){
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
            if (str.equals("1") || str.equals("2") || str.equals("3")) {
                if (n == null) {
                    System.out.println("Sem Solucao!");
                } else {
                    System.out.println("Solucao:\n" + n.montaCaminho() + "\n\n");
                }
            }
        }
    }
    
    
    /** Verifica se um estado � igual a outro j� inserido na lista de sucessores (usado para poda) */
    public boolean equals(Object o) {
        return false;
    }
    
    /** 
     * retorna o hashCode desse estado
     * (usado para poda, conjunto de fechados)
     */
    
    public int hashCode() {
		return 0;

    }
    
	/**
     * Custo acumulado g
     */
    public int custoAcumulado(){
		return 0;
    }
    
    static class GoalNode {
    	private List<String> skills;
    	private List<GoalNode> children;
    	private GoalNode parent;
 
    	public GoalNode(GoalNode p) {
    		parent = p;
    		if (parent != null)
    			parent.addChild(this);
    	}
    	
    	public void addSkill(String newSkill) {
    		skills.add(newSkill);
    	}

    	private void addChild(GoalNode newChild) {
    		children.add(newChild);
    	}
    }
}

