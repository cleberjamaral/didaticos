package exemplos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import busca.Antecessor;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

public class OrganizationalRole implements Estado, Antecessor{
    
	GoalNode headGoal;
	private List<String> roleSkills = new ArrayList<String>();
	private List<GoalNode> goalSuccessors = new ArrayList<GoalNode>();
	private List<OrganizationalRole> roleSuccessors = new ArrayList<OrganizationalRole>();

	private OrganizationalRole roleParent;
	
	private List<String> graphLinks = new ArrayList<String>();
	
    public String getDescricao() {
        return "Empty\n";
    }
    
    public OrganizationalRole(GoalNode gn) {
    	headGoal = gn;
    	
    	if (gn.parent == null) {
    		for (GoalNode goal : headGoal.getSuccessors()) goalSuccessors.add(goal);
    	}
    }
    
    public boolean ehMeta(){

    	if (goalSuccessors.size() == 0) {
        	System.out.println("GOAL ACHIEVED!\n");
        	OrganizationalRole rootRole = getRootRole(this);
        	
    		try (FileWriter fw = new FileWriter("graph.gv", false);
    				BufferedWriter bw = new BufferedWriter(fw);
    				PrintWriter out = new PrintWriter(bw)) {
            	out.print("digraph G {\n");
    			for (String s : rootRole.graphLinks) out.print(s+";\n");
            	out.print("}\n");
    		} catch (IOException e) {
    		}
    	}
    	return (goalSuccessors.size() == 0);
    }    	

	public OrganizationalRole getRootRole(OrganizationalRole node) {
		if (node.roleParent == null) 
			return node;
		else
			return getRootRole(node.roleParent);
	}

	/** * Java method to print leaf nodes using recursion * * @param root * */

	public int custo() {
		return 1;
	}

	/** Lista de sucessores */
	public List<Estado> sucessores() {
		List<Estado> suc = new LinkedList<Estado>(); // Lista de sucessores

		System.out.println("PARENT: " + this.toString() + " - Goal: " + goalSuccessors.size() + " - Size: "
				+ headGoal.getSuccessors().size());

		for (GoalNode temp : goalSuccessors) {
			// Add child
			addChild(suc, temp);
			// Add brother
			addBrother(suc, temp);
/*			
			// Join to another
			OrganizationalRole rootRole = getRootRole(this);
			for (OrganizationalRole tempRole : rootRole.roleSuccessors) {
				if (tempRole.roleSkills.retainAll(temp.getSkills())) {
//					OrganizationalRole novo = new OrganizationalRole(temp);
//					for (String skill : temp.getSkills()) novo.roleSkills.add(skill);
//					novo.roleParent = rootRole;
//					graphLinks.add(rootRole.headGoal.goalName + "->" + novo.headGoal.goalName);
					System.out.println("addLink: " + temp.goalName);					
//					System.out.println("addLink: " + novo.toString());
//					suc.add(novo);
//					roleSuccessors.add(novo);
				}
			}
*/
		}
		return suc;
	}

	public void addChild(List<Estado> suc, GoalNode temp) {
		if (headGoal.getChildren().contains(temp)) {
			OrganizationalRole novo = new OrganizationalRole(temp);
			for (String skill : temp.getSkills())
				novo.roleSkills.add(skill);
			novo.roleParent = this;
			graphLinks.add(this.headGoal.goalName + "->" + novo.headGoal.goalName);

			for (GoalNode goal : headGoal.getSuccessors()) {
				if (goal != novo.headGoal)
					novo.goalSuccessors.add(goal);
			}

			System.out.println("addChild: " + novo.toString());
			suc.add(novo);
			roleSuccessors.add(novo);
		}
	}

	public void addBrother(List<Estado> suc, GoalNode temp) {
		if ((headGoal.parent == temp.parent) && (headGoal.parent != null)) {
			OrganizationalRole novo = new OrganizationalRole(temp);
			for (String skill : temp.getSkills())
				novo.roleSkills.add(skill);
			novo.roleParent = this.roleParent;
			graphLinks.add(this.roleParent.headGoal.goalName + "->" + novo.headGoal.goalName);

			for (GoalNode goal : headGoal.getSuccessors()) {
				if (goal != novo.headGoal)
					novo.goalSuccessors.add(goal);
			}

			System.out.println("addBrother: " + novo.toString());
			suc.add(novo);
			roleSuccessors.add(novo);
		}
	}

	/** Lista de antecessores, para busca bidirecional */
	public List<Estado> antecessores() {
		return sucessores();
	}

	public String toString() {
		String r = "";
		r += headGoal.toString() + "[" + this.roleSkills.toString() + "]";

		return r;
	}

	/**
	 * Verifica se um estado � igual a outro j� inserido na lista de sucessores
	 * (usado para poda)
	 */
	public boolean equals(Object o) {
		/*
		try {
			if (o instanceof OrganizationalRole) {
				OrganizationalRole e = (OrganizationalRole) o;
				//Se qualquer rato estiver numa posicao diferente este estado e diferente
				if (e.graphLinks.equals(((OrganizationalRole) o).graphLinks)) {
					System.out.println("equal!!");
					return true;
				} else {
					System.out.println("Not equal!!");
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		return false;
	}

	/**
	 * retorna o hashCode desse estado (usado para poda, conjunto de fechados)
	 */

	public int hashCode() {
		return 0;

	}

	/**
	 * Custo acumulado g
	 */
	public int custoAcumulado() {
		return 0;
	}

	public static void main(String[] a) throws IOException {

		// GoalNode paintHouse = new GoalNode(null,"paintHouse");
		// GoalNode contracting = new GoalNode(paintHouse,"contracting");
		// contracting.addSkill("getBids");
		// contracting.addSkill("show");
		// contracting.addSkill("contract");
		// GoalNode bidIPaint = new GoalNode(contracting,"bidIPaint");
		// bidIPaint.addSkill("bid");
		// bidIPaint.addSkill("paint");
		// GoalNode bidEPaint = new GoalNode(contracting,"bidEPaint");
		// bidEPaint.addSkill("bid");
		// bidEPaint.addSkill("paint");
		GoalNode execute = new GoalNode(null, "execute");
		GoalNode contractWinner = new GoalNode(execute, "contractWinner");
		contractWinner.addSkill("contract");
		GoalNode iPaint = new GoalNode(execute, "iPaint");
		iPaint.addSkill("paint");
		GoalNode ePaint = new GoalNode(execute, "ePaint");
		ePaint.addSkill("paint");

		OrganizationalRole inicial = new OrganizationalRole(execute);

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
		if (!str.equals("S")) {
			if (str.equals("1")) {
				System.out.println("Busca em Largura");
				n = new BuscaLargura().busca(inicial);
			} else {
				if (str.equals("2")) {
					System.out.println("Busca em Profundidade");
					n = new BuscaProfundidade(20).busca(inicial);
				} else {
					if (str.equals("3")) {
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

	static class GoalNode {
		private List<String> skills = new ArrayList<String>();
		private List<GoalNode> children = new ArrayList<GoalNode>();
		private List<GoalNode> successors = new ArrayList<GoalNode>();
		private String goalName;
		private GoalNode parent;
		private int childrenNumber;

		public GoalNode(GoalNode p, String name) {
			goalName = name;
			parent = p;
			if (parent != null) {
				parent.addChild(this);
				parent.incChildrenNumber();
			}
		}

		public GoalNode(GoalNode p) {
			parent = p;
			if (parent != null)
				parent.addChild(this);
		}

		public void incChildrenNumber() {
			childrenNumber++;
			if (parent != null)
				parent.incChildrenNumber();
		}

		public int getChildrenNumber() {
			return childrenNumber;
		}

		public void addSkill(String newSkill) {
			skills.add(newSkill);
		}

		public List<String> getSkills() {
			return skills;
		}

		private void addChild(GoalNode newChild) {
			children.add(newChild);
			successors.add(newChild);
			if (parent != null)
				parent.addSuccessors(newChild);
		}

		private void addSuccessors(GoalNode newChild) {
			successors.add(newChild);
		}

		public List<GoalNode> getChildren() {
			return children;
		}

		public List<GoalNode> getSuccessors() {
			return successors;
		}

		public GoalNode pickChild() {
			GoalNode temp = children.get(0);
			children.remove(0);
			return temp;
		}

		public String toString() {
			return goalName;
		}
	}

}
