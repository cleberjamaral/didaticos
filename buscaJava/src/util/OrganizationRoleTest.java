package util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Nodo;
import exemplos.OrganizationalRole;
import exemplos.OrganizationalRole.GoalNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OrganizationRoleTest {
	
	static List<GoalNode> tree = new ArrayList<GoalNode>();
	static Stack<GoalNode> stack = new Stack<GoalNode>();
	static GoalNode rootNode = null;
	static boolean pushGoalNode = false;
	static GoalNode referenceGoalNode = null;

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File("tst1.xml"));

		if (!document.getDocumentElement().getNodeName().equals("organisational-specification"))
			throw new IllegalArgumentException("Error! It is expected an 'organisational-specification' XML structure");

		document.getDocumentElement().normalize();
		//Visit all possible schemes from Moise 'functional-specification'
		NodeList nList = document.getElementsByTagName("scheme");

		visitNodes(nList);		
		
		plotOrganizationalGoalTree();
		
		OrganizationalRole inicial = new OrganizationalRole(rootNode);

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

	private static void visitNodes(NodeList nList) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node node = nList.item(temp);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) node;
	            //Create new Employee Object
				if (node.getNodeName().equals("goal")) {
					System.out.println("Node id = " + eElement.getAttribute("id"));

					if (rootNode == null) {
						rootNode = new GoalNode(null,eElement.getAttribute("id"));
						referenceGoalNode = rootNode;
					} else {
						GoalNode gn = new GoalNode(stack.peek(),eElement.getAttribute("id"));
						tree.add(gn);
						referenceGoalNode = gn;
					}
					
				} else if (node.getNodeName().equals("plan")) {
					stack.push(referenceGoalNode);
					System.out.println("Push = " + referenceGoalNode.toString());
				} else if (node.getNodeName().equals("skill")) {
					referenceGoalNode.addSkill(eElement.getAttribute("id"));
					System.out.println("Skill = " + referenceGoalNode.toString() + " : " + referenceGoalNode.getSkills());
				}
				if (node.hasChildNodes()) {
					// We got more childs; Let's visit them as well
					visitNodes(node.getChildNodes());
					if (node.getNodeName().equals("plan")) 
						System.out.println("Poping = " + stack.pop().toString());
				}
			}
		}
	}
	
	private static void plotOrganizationalGoalTree() {
		try (FileWriter fw = new FileWriter("orgTree.gv", false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
        	out.println("digraph G {");
    		for (GoalNode or : tree) {
    			//out.println(or.headGoal.goalName + ";");
				out.print("\t\"" + or.getGoalName()
						+ "\" [ style = \"filled\" fillcolor = \"white\" "
						+ "shape = \"ellipse\" label = <<table border=\"0\" cellborder=\"0\" bgcolor=\"white\">"
						+ "<tr><td bgcolor=\"black\" align=\"center\"><font color=\"white\">"
						+ or.getGoalName() + "</font></td></tr>");
				for (String s : or.getSkills())
					out.print("<tr><td align=\"left\">" + s + "</td></tr>");
				out.println("</table>> ];");
				if (or.getParent() != null)
					out.println("\t" + or.getParent().getGoalName() + "->" + or.getGoalName() + ";");
    		}
        		
        	out.println("}");
		} catch (IOException e) {
		}
	}
}
