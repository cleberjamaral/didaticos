package util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Nodo;
import exemplos.OrganizationalRole;
import exemplos.OrganizationalRole.GoalNode;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OrganizationRoleTest {
	
	static boolean p_open = false;
	static boolean s_open = false;

	static GoalNode rootNode;
	static List<GoalNode> tree = new ArrayList<GoalNode>();

	public static void main(String[] args) throws IOException {
		try {

			File file = new File("tst1.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) printNote(doc.getChildNodes());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
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

	private static void printNote(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				if ((!s_open) && (tempNode.getNodeName().equals("scheme"))) s_open = true; 
				if ((!p_open) && (tempNode.getNodeName().equals("plan"))) p_open = true; 
				System.out.println("\nNode=" + tempNode.getNodeName() + " [OPEN]");
				//System.out.println("Node Value=" + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {
						System.out.println("\t" + nodeMap.item(i).getNodeName() + "=" + nodeMap.item(i).getNodeValue());
						if ((rootNode == null) && (nodeMap.item(i).getNodeName() == "id")){
							rootNode = new GoalNode(null,nodeMap.item(i).getNodeValue());
						} else {
							if ((p_open) && (tempNode.getNodeName() == "goal") && (nodeMap.item(i).getNodeName() == "id")) {
								GoalNode newNode = new GoalNode(rootNode,nodeMap.item(i).getNodeValue());
								tree.add(newNode);
							}
						}
					}
				}

				if (tempNode.hasChildNodes()) printNote(tempNode.getChildNodes());

				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
				if ((s_open) && (tempNode.getNodeName().equals("scheme"))) s_open = false; 
				if ((p_open) && (tempNode.getNodeName().equals("plan"))) p_open = false; 

			}
		}
	}
}
