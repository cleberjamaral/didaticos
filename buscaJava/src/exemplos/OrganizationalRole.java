package exemplos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import busca.Antecessor;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Nodo;

public class OrganizationalRole implements Estado, Antecessor{
    
	private static List<OrganizationalRole> isGoalList = new ArrayList<OrganizationalRole>();
	private static List<OrganizationalRole> rolesList = new ArrayList<OrganizationalRole>();
	
	GoalNode headGoal;
	private Set<String> roleSkills = new HashSet<String>();
	private List<GoalNode> goalSuccessors = new ArrayList<GoalNode>();

	private OrganizationalRole roleParent;
	
	private Set<String> graphLinks = new HashSet<String>();
	
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

    	if (goalSuccessors.size() <= 0) {
           	if (!isGoalList.contains(this))
        	{
        		isGoalList.add(this);
            	System.out.println("* * * GOAL ACHIEVED! Solution: #" + isGoalList.size() + " * * *");
            	
        		try (FileWriter fw = new FileWriter("graph_"+isGoalList.size()+".gv", false);
        				BufferedWriter bw = new BufferedWriter(fw);
        				PrintWriter out = new PrintWriter(bw)) {
                	out.print("digraph G {\n");
                	if (this.roleParent != null) 
                		out.print("\t" + this.roleParent.headGoal.goalName + ";\n");
                	else
                		out.print("\troot;\n");
                		
        			for (String s : this.graphLinks) out.print("\t" + s + ";\n");
                	out.print("}\n");
        		} catch (IOException e) {
        		}
        	} else {
        		//This should not happen again, it has occurred because searching process (deepth) was calling ehMeta 2 times
            	System.out.println("* * * Goal achieved but duplicated * * *");
        	}
    	}
    	return false;
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

		if (!goalSuccessors.isEmpty())
			System.out.println("\nPARENT: " + this.toString() + " - OpenGoals: [" + goalSuccessors.toString()
					+ "] - Size: " + headGoal.getSuccessors().size());

		for (GoalNode goalToBeAssociated : goalSuccessors) {
			// Add child
			addChild(suc, goalToBeAssociated);
			// Add brother
			addBrother(suc, goalToBeAssociated);
			// Join to another
			joinAnother(suc, goalToBeAssociated);
		}
		
		//for (OrganizationalRole or : rolesList) 
		//	System.out.println("ExistingRole: " + or.headGoal.goalName);
		
		return suc;
	}

	public void addChild(List<Estado> suc, GoalNode goalToBeAssociatedToRole) {
		if (headGoal.getChildren().contains(goalToBeAssociatedToRole)) {
			OrganizationalRole newRole = new OrganizationalRole(goalToBeAssociatedToRole);
			
			//Copy all skills of the goal to this new role
			for (String skill : goalToBeAssociatedToRole.getSkills()) newRole.roleSkills.add(skill);
			
			//The new role is a child of the current state (current role)
			newRole.roleParent = this;
			for (String s : this.graphLinks) newRole.graphLinks.add(s);
			newRole.graphLinks.add(this.headGoal.goalName + "->" + newRole.headGoal.goalName);

			for (GoalNode goal : this.goalSuccessors) {
				if (goal != newRole.headGoal)
					newRole.goalSuccessors.add(goal);
			}
			
			System.out.print("\taddChild: " + newRole.toString() + ", Links: [ ");
			for (String s : newRole.graphLinks) System.out.print(s + " ");
			System.out.println("], nSucc: " + newRole.goalSuccessors.size());

			suc.add(newRole);
			rolesList.add(newRole);
		}
	}

	public void addBrother(List<Estado> suc, GoalNode goalToBeAssociatedToRole) {
		if ((headGoal.parent == goalToBeAssociatedToRole.parent) && (headGoal.parent != null)) {
			OrganizationalRole newRole = new OrganizationalRole(goalToBeAssociatedToRole);
			for (String skill : goalToBeAssociatedToRole.getSkills())
				newRole.roleSkills.add(skill);
			newRole.roleParent = this.roleParent;
			for (String s : this.graphLinks) newRole.graphLinks.add(s);
			newRole.graphLinks.add(this.roleParent.headGoal.goalName + "->" + newRole.headGoal.goalName);

			for (GoalNode goal : this.goalSuccessors) {
				if (goal != newRole.headGoal)
					newRole.goalSuccessors.add(goal);
			}

			System.out.print("\taddBrother: " + newRole.toString() + ", Links: [ ");
			for (String s : newRole.graphLinks) System.out.print(s + " ");
			System.out.println("], nSucc: " + newRole.goalSuccessors.size());

			suc.add(newRole);
			rolesList.add(newRole);
		}
	}

	public void joinAnother(List<Estado> suc, GoalNode goalToBeAssociatedToRole) {
		//if the goal has skills (not null) and this node has all skills, so we can join the goals in an unique role 
		if ((this.roleSkills.containsAll(goalToBeAssociatedToRole.getSkills())) && (!goalToBeAssociatedToRole.getSkills().isEmpty())) {
			//Creates a new state which is the same role but with another equal link (just to make it different)
			OrganizationalRole newRole = new OrganizationalRole(goalToBeAssociatedToRole);
			for (String skill : goalToBeAssociatedToRole.getSkills())
				newRole.roleSkills.add(skill);
			newRole.roleParent = this.roleParent;
			for (String s : this.graphLinks) newRole.graphLinks.add(s);
			newRole.graphLinks.add(newRole.headGoal.goalName + " [shape=plaintext,comment=joined]");

			for (GoalNode goal : this.goalSuccessors) {
				if (goal != newRole.headGoal)
					newRole.goalSuccessors.add(goal);
			}

			System.out.print("\tjoinAnother: " + newRole.toString() + ", Links: [ ");
			for (String s : newRole.graphLinks) System.out.print(s + " ");
			System.out.println("], nSucc: " + newRole.goalSuccessors.size());

			suc.add(newRole);
			rolesList.add(newRole);
		}
	}
	
	/** Lista de antecessores, para busca bidirecional */
	public List<Estado> antecessores() {
		return sucessores();
	}

	public String toString() {
		String r = "";
		r += headGoal.toString();
		if (!this.roleSkills.isEmpty()) r += "[" + this.roleSkills.toString() + "]";

		return r;
	}

	/**
	 * Verifica se um estado � igual a outro j� inserido na lista de sucessores
	 * (usado para poda)
	 */
	public boolean equals(Object o) {
		try {
			if (o instanceof OrganizationalRole) {
				return this.graphLinks.equals(((OrganizationalRole) o).graphLinks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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

		GoalNode paintHouse = new GoalNode(null,"paintHouse");
		GoalNode contracting = new GoalNode(paintHouse,"contracting");
		//contracting.addSkill("getBids");
		//contracting.addSkill("show");
		//contracting.addSkill("contract");
		GoalNode bidIPaint = new GoalNode(contracting,"bidIPaint");
		//bidIPaint.addSkill("bid");
		//bidIPaint.addSkill("paint");
		//GoalNode bidEPaint = new GoalNode(contracting,"bidEPaint");
		//bidEPaint.addSkill("bid");
		//bidEPaint.addSkill("paint");
		GoalNode execute = new GoalNode(paintHouse, "execute");
		//GoalNode contractWinner = new GoalNode(execute, "contractWinner");
		//contractWinner.addSkill("contract");
		//GoalNode iPaint = new GoalNode(execute, "iPaint");
		//iPaint.addSkill("paint");
		//GoalNode ePaint = new GoalNode(execute, "ePaint");
		//ePaint.addSkill("paint");

/*
Digite sua opcao de busca { Digite S para finalizar }
	1  -  Largura
	2  -  Profundidade
	3  -  Pronfundidade Iterativo
Opcao: 2
Busca em Profundidade

PARENT: paintHouse - OpenGoals: [[contracting, execute]] - Size: 2
	addChild: contracting, Links: [ paintHouse->contracting ], nSucc: 1
	addChild: execute, Links: [ paintHouse->execute ], nSucc: 1

PARENT: contracting - OpenGoals: [[execute]] - Size: 0
	addBrother: execute, Links: [ paintHouse->execute paintHouse->contracting ], nSucc: 0
* * * GOAL ACHIEVED! Solution: #1 * * *

PARENT: execute - OpenGoals: [[contracting]] - Size: 0
	addBrother: contracting, Links: [ paintHouse->execute paintHouse->contracting ], nSucc: 0
Sem Solucao!
*/		
		
		OrganizationalRole inicial = new OrganizationalRole(paintHouse);

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
					n = new BuscaProfundidade(100).busca(inicial);
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

		private void addSuccessors(GoalNode newSuccessor) {
			successors.add(newSuccessor);
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
