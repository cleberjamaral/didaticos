package util; 

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import busca.AEstrela;
import busca.Aleatorio;
import busca.Antecessor;
import busca.Busca;
import busca.BuscaBidirecional;
import busca.BuscaIterativo;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.Heuristica;
import busca.MostraStatusConsole;
import busca.Nodo;
import busca.SubidaMontanha;
import exemplos.Estado8Puzzle;
import exemplos.EstadoJarros;
import exemplos.EstadoMapa;
import exemplos.EstadoRainhas;
import exemplos.HLAC;
import exemplos.MiceAndHoles;
import exemplos.MissionarioCanibal;
import exemplos.QuadradoMagico;
import exemplos.QuadradoMagicoB;


/**
 * Applet para demonstrar os algoritmos de Busca
 *
 * @author Jomi Fred Hubner
 */

public class AppletDemoBusca extends JApplet {
    boolean isStandalone = false;
    
    JTextArea text = new JTextArea();
    JComboBox cAlgoritmo;
    JComboBox cProblema;
    JButton para;
    JButton executa;
    JTextField visitados = new JTextField(30);
    JTextField tPars = new JTextField(10);
    JCheckBox comFechados;
    JCheckBox comPoda;
    
    Problema problema = null;
    Busca  algBusca = null;
    

    /** inicia a interface */
    public void init() {
        try {
            getContentPane().setLayout(new BorderLayout());
            executa = new JButton("Executa");
            executa.setEnabled(false);
            executa.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    executa();
                }
            });
            
            para = new JButton("Parar");
            para.setEnabled(false);
            para.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                	if (algBusca != null) {
                		algBusca.para();
                	}
                }
            });
            
            cAlgoritmo = new JComboBox();
            cProblema = new JComboBox();
            cProblema.addItem("<sem selecao>");
            cProblema.addItem(new Jarros());
            cProblema.addItem(new P_HLAC()); 
            cProblema.addItem(new P_MisCa()); 
            cProblema.addItem(new P_Mapa()); 
            cProblema.addItem(new P_Puzzle1()); 
            cProblema.addItem(new P_Puzzle2()); 
            cProblema.addItem(new P_Puzzle3()); 
            cProblema.addItem(new P_Rainhas()); 
            cProblema.addItem(new P_Quadrado1()); 
            cProblema.addItem(new P_Quadrado2());
            cProblema.addItem(new P_MiceAndHoles());

            cProblema.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    executa.setEnabled(false);
                    tPars.setEditable(false);
                    tPars.setText("");
                    tPars.setToolTipText("sem parametros para este problema");
                    int sel = cProblema.getSelectedIndex();
                    if (sel > 0) {
                    	problema = (Problema)cProblema.getSelectedItem();
                    	if (problema.getToolTip() != null) {
                    	    tPars.setEditable(true);
                    	    tPars.setToolTipText(problema.getToolTip());
                    	}

                    	Estado inicial = problema.getInicial();
                    	Estado meta = problema.getMeta();
	                    text.setText(inicial.getDescricao());
	                    text.append("\nEstado inicial="+inicial);
	                    if (meta != null) {
	                        text.append("\nEstado meta="+EstadoMapa.nomes[EstadoMapa.getMeta()]);                        
	                    }
	                    executa.setEnabled(true);
	                    
	                    cAlgoritmo.removeAllItems();
	                    cAlgoritmo.addItem(new BuscaLargura());
	                    cAlgoritmo.addItem(new BuscaProfundidade());
	                    cAlgoritmo.addItem(new BuscaIterativo());
	                    try {
	                        Heuristica h = (Heuristica)inicial; // ve se implementa a interface Heuristica
	                        cAlgoritmo.addItem(new AEstrela());
	                    } catch (Exception e2) {}
	                    try {
	                        Aleatorio a = (Aleatorio)inicial; // ve se implementa a interface Aleatorio
	                        cAlgoritmo.addItem(new SubidaMontanha());
	                    } catch (Exception e2) {}
	                    try {
	                        Antecessor a = (Antecessor)inicial; // ve se implementa a interface Aleatorio
	                        cAlgoritmo.addItem(new BuscaBidirecional());
	                    } catch (Exception e2) {}
                    }
                }
            });
            
            JPanel panelN = new JPanel();
            panelN.setLayout(new GridLayout(0, 1));
            
            tPars.setEditable(false);
            tPars.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                    	problema.setParametros(tPars.getText());
                    } catch (Exception e) {
                        text.append("Erro: "+e);
                    }
                }
            });
            
            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(new JLabel("Problema:"));
            p.add(cProblema);
            p.add(new JLabel("   Parametros:"));
            p.add(tPars);
            panelN.add(p);
            
            p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(new JLabel("Algoritmo:"));
            p.add(cAlgoritmo);
            p.add(executa);
            p.add(para);
            panelN.add(p);
            
            comFechados = new JCheckBox("usar 'fechados'", true);
            comFechados.setToolTipText("Se ativado, um nodo nao gera sucessor igual a um outro da lista de fechados (j� visitados), sen�o considera s� a ascendencia direta do nodo.");

            comPoda = new JCheckBox("podar", true);
            comPoda.setToolTipText("Se ativado, um nodo nao gera sucessor igual a um outro ja criado.");
            comPoda.addItemListener(new  ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.DESELECTED) {
                        comFechados.setSelected(false);
                    }
                }});

                
            p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT));
            p.add(new JLabel("Nodos visitados:"));
            p.add(visitados);
            p.add(comPoda);
            p.add(comFechados);
            visitados.setEditable(false);
            
            getContentPane().add(panelN, BorderLayout.NORTH);
            getContentPane().add(new JScrollPane(text), BorderLayout.CENTER);
            getContentPane().add(p, BorderLayout.SOUTH);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    void executa() {
        para.setEnabled(true);
        executa.setEnabled(false);

        final Estado inicial = problema.getInicial();
    	final Estado meta = problema.getMeta();

        try {
        	algBusca = (Busca)cAlgoritmo.getSelectedItem();
        } catch (Exception e) {
        	System.err.println("Nao foi selecionado um algoritmo");
        	return;
        }
        
        visitados.setText("");
        text.setText(algBusca+"\n");
        text.append("Estado inicial="+inicial);
        if (meta != null) {
        	text.append("Estado meta="+meta);
        }

        
        algBusca.setPodar(comPoda.isSelected());
        algBusca.usarFechados(comFechados.isSelected());
        
        // tenta setar a profundidade (para o caso do algoritmo ser BP ou descendente)
        try {
        	BuscaProfundidade bp = (BuscaProfundidade)algBusca;
        	bp.setProfMax(problema.getProfundidade());
        } catch (Exception e) {}
        
        algBusca.setMostra(new MostraStatusConsole() {
            protected void mostraFim() {
                visitados.setText(getStatus().getVisitados()+" em "+getStatus().getTempoDecorrido()+" ms, profundidade="+getStatus().getProfundidade());        
            }
            protected void mostra() {
                mostraFim();
            }
            protected void println(String s) {
                text.append(s+"\n");
            }
        });            

        
        Thread busca = new Thread() {
            public void run() {
                try {
                    Nodo   n = null;
                    algBusca.setParar(false);
                    algBusca.novoStatus();
                    
                    try { 
                    	// testa se � o bi-direcional
                    	BuscaBidirecional bbd = (BuscaBidirecional)algBusca;
                    	n = bbd.busca(inicial, meta);
                    } catch (Exception e) {
                    	n = algBusca.busca(inicial); // � uma algoritmo "normal" do busca
                    }
                    
                    if (n != null) {
                        text.append("\n\nsolucao ("+n.getProfundidade()+" operacoes, custo="+n.g()+"):\n" + n.montaCaminho() + "\n\n");
                    } else {
                    	text.append("\n\nsem solucao!");
                    }
                } catch (Throwable e) {
                    text.append("Erro: "+e);
                } finally {
                    para.setEnabled(false);
                    executa.setEnabled(true);
                    text.setCaretPosition(text.getText().length());
                    algBusca.para();
                }                
            }
        };
        busca.start();
    }
    
    /** classes que mapeiam um problema para a interface do applet */
    
    abstract class Problema {
    	String id;
    	Problema(String s) { id = s; }
    	public String toString() {	return id; }
    	abstract Estado getInicial();
    	Estado getMeta() { return null; }
    	int getProfundidade() { return 20; }
    	String getToolTip() { return null; }
    	void setParametros(String p) { }
    }
    
    class Jarros extends Problema {
    	Jarros() { super("Jarros"); }
    	Estado getInicial() { return new EstadoJarros(0,0,"inicial"); }
    	Estado getMeta() { return new EstadoJarros(2,2,"meta"); }    	
    }

    class P_HLAC extends Problema {
    	P_HLAC() { super("Homem, Lobo, Carneiro e o Alface"); }
    	Estado getInicial() { return new HLAC('e', 'e', 'e', 'e', "inicial"); }
    	Estado getMeta() { return new HLAC('d', 'd', 'd', 'd', "meta"); }    	
    }

    class P_MisCa extends Problema {
    	P_MisCa() { super("Missionarios e Canibais"); }
    	Estado getInicial() { return new MissionarioCanibal(3,3,'e',"inicial"); }
    	Estado getMeta() { return new MissionarioCanibal(0,0,'d',"meta"); }    	
    }

    class P_Mapa extends Problema {
    	int inicial = 8;
    	P_Mapa() { super("Caminho em mapa"); EstadoMapa.setMeta(15); }
    	Estado getInicial() { return new EstadoMapa(inicial); }
    	Estado getMeta() { return new EstadoMapa(EstadoMapa.getMeta()); }
    	String getToolTip() { return "Informe a cidade inicial"; }
    	void setParametros(String p) { 
	        // procura o indice
	        for (int i=0; i<EstadoMapa.nomes.length; i++) {
	            if (EstadoMapa.nomes[i] == tPars.getText().charAt(0)) {
	                inicial = i;
	                break;
	            }
	        }
	        text.append("\nNovo estado inicial="+getInicial());
    	}
    }

    class P_Puzzle1 extends Problema {
    	P_Puzzle1() { super("8-Puzzle (estado inicial aleatorio)"); }
    	Estado getInicial() { return new Estado8Puzzle(); }
    	Estado getMeta() { return Estado8Puzzle.getEstadoMeta(); }    	
    	int getProfundidade() { return 30; }
    }
    class P_Puzzle2 extends Problema {
    	P_Puzzle2() { super("8-Puzzle (estado inicial facil)"); }
    	Estado getInicial() { return Estado8Puzzle.getEstadoFacil(); }
    	Estado getMeta() { return Estado8Puzzle.getEstadoMeta(); }    	
    }
    class P_Puzzle3 extends Problema {
    	P_Puzzle3() { super("8-Puzzle (estado inicial dificil)"); }
    	Estado getInicial() { return Estado8Puzzle.getEstadoDificil(); }
    	Estado getMeta() { return Estado8Puzzle.getEstadoMeta(); }    	
    	int getProfundidade() { return 80; }
    }
    
    class P_Rainhas extends Problema {
    	P_Rainhas() { super("8 Rainhas"); }
    	Estado getInicial() { return new EstadoRainhas(); }
    	String getToolTip() { return "Informe o numero de rainhas"; }
    	void setParametros(String p) { 
    		EstadoRainhas.setTamanho( Integer.parseInt(p));
    	}
    }
    
    class P_Quadrado1 extends Problema {
    	P_Quadrado1() { super("Quadrado magico (versao a)"); }
    	Estado getInicial() { return new QuadradoMagico(); }
    	String getToolTip() { return "Informe a dimensao do quadrado"; }
    	void setParametros(String p) { 
    		QuadradoMagico.setTamanho( Integer.parseInt(p));
    	}
    }
    class P_Quadrado2 extends Problema {
    	P_Quadrado2() { super("Quadrado magico (versao b)"); }
    	Estado getInicial() { return new QuadradoMagicoB(); }
    	String getToolTip() { return "Informe a dimensao do quadrado"; }
    	void setParametros(String p) { 
    		QuadradoMagico.setTamanho( Integer.parseInt(p));
    	}
    }
    class P_MiceAndHoles extends Problema {
    	P_MiceAndHoles() { super("Mice and holes"); }
    
//1000    	int micePosition[] = {1806,1589,1005,1991,478,1622,919,128,1596,101,278,426,1698,1505,1609,695,1211,709,822,44,582,835,1464,317,1569,1831,851,1954,1740,614,16,724,300,1102,736,1408,1817,635,1886,312,255,1507,1199,1893,1709,350,429,814,1600,1804,1190,455,1875,1098,1476,586,1654,1792,1628,118,356,105,279,819,661,429,1298,623,634,905,666,1342,295,592,58,1260,1122,95,1149,1376,1827,382,1246,1190,1677,729,310,11,1500,1440,1903,1263,196,539,1871,630,104,1998,1500,988,79,1285,818,1432,478,1264,403,1463,1105,665,763,1848,1638,869,1671,1781,1223,684,1254,628,984,1825,14,1888,1789,685,1987,214,1341,1022,1202,90,495,1546,1632,1931,766,509,619,1663,1654,1947,625,1982,451,84,320,1195,1853,1444,1251,892,986,1073,838,1214,640,1473,558,1511,1945,76,939,1377,91,1714,777,1382,43,83,1382,1973,1213,1610,1036,580,1091,1656,1503,314,1583,1182,713,1032,452,313,1293,571,1138,1882,466,448,1062,989,699,1500,1595,1402,690,228,1443,259,620,747,1621,1392,88,1805,1158,1207,1725,1421,73,1891,1477,552,3,464,443,561,680,1080,923,1156,1739,225,1290,1550,550,1090,30,663,1393,18,202,961,962,169,933,1262,446,999,1683,1499,368,1457,74,256,907,391,63,1910,409,1503,1654,1776,1343,631,469,11,1045,990,1993,207,1106,1768,888,997,1383,1001,516,1134,1690,1119,767,1901,1625,1167,965,1448,1313,1147,650,237,1469,1253,1605,1197,562,972,556,14,237,877,1813,1357,16,898,392,1552,1697,1414,108,987,784,1170,1526,837,967,1426,1927,714,243,1496,940,200,351,248,35,1932,855,365,1394,987,1502,356,1253,1406,988,237,215,842,266,807,318,657,1918,1411,624,1843,734,1201,1968,746,1691,984,516,1661,1710,1076,414,1343,1050,1946,1242,684,110,683,1695,1542,1411,1205,1198,1598,163,152,1849,199,202,1239,1426,1680,1614,746,1667,1591,1894,1126,671,1401,20,7,456,799,1721,472,786,41,1844,634,1223,887,1078,278,770,710,79,1223,1211,1735,1030,92,384,424,1737,1883,553,604,345,993,532,1828,844,1931,1267,1625,1002,1460,326,1912,610,610,1141,602,445,1152,1966,1310,1100,1290,1182,1280,330,1515,121,927,662,1300,1976,119,479,1576,1362,486,1042,1511,781,965,1244,413,1776,6,229,1547,1851,124,1237,621,1251,1452,296,1479,510,654,870,1130,1350,1870,409,1474,570,1444,37,1811,1285,4,1552,828,1629,453,612,91,1611,1297,363,444,1713,1723,746,708,969,368,1669,1657,456,213,1626,628,1366,1124,1,1939,937,1047,204,616,1568,367,1436,841,126,1361,1536,794,891,1063,1849,1640,1897,536,609,981,981,469,1602,146,1448,229,145,323,362,674,533,1896,1468,1749,454,545,956,882,277,1163,33,407,739,1498,1068,80,1735,128,379,1833,589,636,976,1987,1259,1969,915,202,983,871,100,153,1400,536,1449,1537,683,920,1475,1040,925,242,1370,1662,452,1493,967,1239,353,186,243,797,144,1669,1254,51,1737,891,377,1706,1718,866,12,781,1045,1631,682,213,530,1308,1759,452,134,711,1449,802,11,6,1725,536,877,221,1863,202,1354,1442,774,268,475,929,153,1003,1229,76,1877,777,805,602,1413,1689,297,824,1283,1562,1159,1569,1111,271,64,1387,362,1304,1762,283,1977,911,1101,1534,466,1210,890,139,693,1710,200,1399,1430,653,1793,1348,877,497,1686,1229,1888,1965,240,487,490,1245,1433,284,1529,1694,914,844,137,1705,78,236,842,1562,1637,293,155,1380,387,827,223,1658,1139,1524,614,1277,1349,20,1115,1333,1178,1455,667,1900,1292,1137,81,780,996,252,448,86,1628,1316,515,210,1957,1929,432,845,1760,590,520,236,1977,993,1192,1063,252,721,893,183,300,372,608,1857,1554,209,396,1429,789,171,406,40,304,590,670,1663,1415,1497,1795,1894,1570,148,42,64,1383,499,151,731,1453,671,589,228,1574,1860,433,1897,1297,1282,254,804,42,1939,483,999,1502,1200,1106,125,792,213,1267,1175,807,465,1279,747,767,110,703,974,1368,45,686,1962,789,1983,459,184,803,972,1823,1429,1759,1173,1391,693,437,35,1715,1391,1576,611,1534,1907,894,287,1368,1802,1960,386,1918,546,1502,1997,655,1440,294,842,1406,236,1122,425,1798,768,1240,904,727,830,114,1130,431,227,1410,1240,38,1766,1200,1314,1301,1254,1266,88,1237,1620,127,703,569,1987,1627,710,161,323,1522,1274,2,638,62,566,1992,791,1121,1765,1337,1433,246,1132,48,227,1460,546,244,434,963,464,373,8,851,1318,813,1104,1421,1375,1722,1760,1977,109,1656,1455,133,1600,349,820,370,958,1276,629,1638,1627,861,866,364,1997,461,1415,1183,1245,631,29,760,1724,365,973,1626,422,1727,1533,1448,32,1090,1371,364,1765,1798,812,537,238,474,1415,1168,1892,385,1815,810,1958,955,1868,933,815,1903,1521,764,432,838,981,1806,1754,506,360,1873,399,1031,1104,948,897,928,209,1628,943,477,1077,352,189,1337,1822,1536,721,1467,313,1946,1797,1870,412,1608,490,1097,434,1741,135,741,971,1239,456,427,1634,1047,1731,1845,1019};
//1000    	int holeCapacity[] = {0,0,7,0,2,0,6,0,0,0,0,1,0,0,9,2,1,4,0,6,0,0,2,7,0,1,0,6,0,2,2,9,1,1,1,1,1,2,1,5,0,0,5,0,6,7,7,0,9,0,0,0,1,7,0,0,6,2,0,5,6,1,0,0,4,1,0,0,0,6,9,0,1,7,1,6,1,0,0,0,1,5,0,5,0,7,0,1,0,1,0,0,0,0,5,0,0,6,0,0,0,5,1,1,0,0,5,0,9,1,7,0,2,1,1,1,6,0,5,5,2,1,0,0,1,0,0,0,0,0,6,7,1,0,0,0,9,6,0,6,1,1,0,0,1,5,1,0,0,0,0,0,0,0,0,0,5,1,6,0,1,2,0,9,1,1,1,9,0,0,0,0,2,0,5,1,1,0,0,7,2,2,1,0,7,1,1,0,0,0,0,9,1,1,0,0,0,0,0,0,1,7,6,0,2,0,9,0,9,2,6,0,0,9,0,1,0,1,6,0,5,1,5,5,0,0,0,0,0,9,0,1,0,2,0,6,1,1,0,0,0,9,0,0,9,0,0,1,0,1,0,5,0,0,1,0,2,1,0,0,1,2,0,0,0,2,0,7,0,0,0,6,2,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,7,9,0,6,9,0,0,6,1,5,0,0,5,5,2,2,8,1,0,0,0,0,6,2,0,7,0,0,0,1,6,2,0,0,0,0,0,0,2,0,0,9,0,0,0,0,1,0,2,0,0,0,0,0,0,0,1,1,6,0,0,2,0,2,1,9,6,2,7,0,1,0,0,0,5,0,1,0,1,0,1,0,0,1,2,0,1,0,0,5,0,2,0,0,0,2,0,0,7,2,0,6,0,0,0,0,0,0,0,9,0,5,0,1,6,0,5,1,5,0,1,7,0,1,0,2,1,0,7,0,0,0,0,1,0,0,0,2,9,1,0,0,8,1,0,2,0,6,7,9,1,1,0,0,0,1,0,5,0,0,0,0,2,6,0,0,0,0,1,2,7,7,0,0,0,2,0,2,0,0,0,4,7,0,1,0,0,1,1,1,0,0,0,6,0,7,0,9,0,2,0,0,0,5,0,0,6,0,0,0,0,0,0,0,0,2,5,0,0,0,8,0,0,0,6,0,1,2,1,1,0,0,9,1,0,0,0,1,0,6,0,9,0,9,0,0,0,0,7,0,8,5,1,0,7,0,0,1,1,0,0,1,0,0,6,1,0,0,2,5,1,0,0,5,0,1,0,0,0,0,0,0,6,0,5,2,0,0,0,0,7,0,5,1,0,0,0,9,0,0,2,0,0,1,5,0,1,0,9,0,0,7,0,9,0,9,6,0,2,0,1,0,0,1,9,0,1,0,1,2,0,7,0,0,1,1,1,0,0,2,0,7,0,0,0,6,2,0,0,1,0,0,0,5,0,7,0,0,0,0,0,1,0,5,0,2,0,4,2,4,0,0,0,6,9,0,1,2,0,1,1,0,0,0,1,5,2,0,7,6,1,2,1,0,2,9,0,1,1,0,7,1,0,1,0,9,0,0,1,0,0,6,0,9,2,0,9,0,2,0,1,1,1,0,0,0,0,0,1,0,2,1,5,4,4,2,0,1,0,0,2,0,1,0,9,0,0,0,0,4,2,1,1,0,0,0,1,2,5,0,9,1,6,1,5,0,0,0,0,1,0,1,0,0,0,9,0,0,0,0,9,1,2,2,0,2,9,0,0,6,2,0,7,7,0,1,1,5,1,2,1,6,9,0,0,0,1,0,0,0,0,0,2,1,0,1,4,6,0,1,1,1,1,0,1,0,7,9,9,6,0,0,0,9,0,2,0,0,1,0,0,1,9,1,0,9,2,0,0,5,0,9,0,0,0,9,1,0,1,0,0,0,2,0,1,0,0,1,0,0,0,1,0,0,0,0,2,5,1,1,0,0,0,1,9,0,0,0,0,1,0,0,1,7,0,0,5,8,2,9,0,1,0,7,7,0,0,7,5,0,0,2,0,0,0,7,6,6,0,9,0,0,0,0,0,1,0,0,0,0,1,0,0,2,0,9,0,7,2,0,1,5,1,2,1,1,7,0,0,0,1,1,6,0,7,0,9,0,5,1,9,2,5,2,1,0,0,0,1,0,9,0,4,0,6,0,1,0,1,1,2,0,2,1,2,9,6,2,0,0,0,2,7,0,0,1,2,2,0,1,2,5,7,9,1,0,7,0,5,9,0,0,0,1,0,0,0,7,0,0,2,0,6,6,0,0,0,0,0,0,1,2,1,7,0,1,0,2,1,9,0,0,0,1,0,0,1,0,0,0,1,2,0,0,0,6,2,1,0,0,1,7,7,0,7,6,2,0,0,1,0,0,0,2,0,6,5,0,0,0,9,0,0,5,0,0,5,0,8,0,0,0,0,0,0,0,1,0,0,1,0,5,0,0,5,9,1,2,1,0,5,0,0,1,1,0,7,0,9,0,0,1,1,0,6,1,0,1,0,9,1,0,0,1,0,1,0,6,0,0,0,1,5,0,1,0,6,0,0,7,4,2,5,1,0,0,2,1,1,2,0,6,0,1,0,0,0,0,0,0,0,1,7,0,2,0,6,1,0,1,0,0,1,0,5,0,0,1,0,2,0,1,0,0,1,7,0,6,9,0,1,0,0,0,0,5,0,0,0,0,7,1,0,5,0,0,1,0,0,0,0,0,0,6,0,0,0,0,0,9,7,1,1,5,0,8,0,0,0,1,1,2,0,0,0,0,0,7,1,1,1,0,6,0,0,0,2,9,1,0,2,1,1,1,0,6,0,0,0,0,1,0,6,1,0,0,0,0,0,9,0,2,5,7,2,9,1,5,1,0,0,0,1,1,0,1,0,0,0,0,0,0,0,9,5,6,1,2,0,1,5,0,0,0,0,0,0,1,0,0,0,7,6,1,7,0,0,0,0,0,0,0,0,2,1,0,1,1,0,2,9,1,0,1,0,0,5,5,1,0,1,0,0,0,2,0,0,1,6,1,0,0,0,0,7,0,0,0,0,0,0,0,5,5,0,0,1,6,0,1,0,0,7,9,0,0,0,0,2,9,0,9,2,1,0,0,0,1,1,0,0,1,0,0,1,1,1,0,9,0,6,0,2,0,0,1,6,0,0,1,1,0,8,0,0,0,1,1,0,9,5,1,2,0,9,8,0,0,1,0,1,0,0,1,2,0,0,0,7,2,2,0,1,2,0,1,1,7,0,1,2,7,5,0,1,1,1,6,1,0,1,0,2,1,0,1,1,2,1,0,2,0,1,2,0,8,7,0,0,1,1,1,1,0,1,2,0,2,1,5,1,0,1,0,0,0,1,0,1,0,6,5,0,0,2,0,0,2,0,2,1,6,1,5,5,0,0,0,0,7,1,1,0,0,0,1,0,0,8,7,1,2,5,6,0,2,0,1,0,0,1,1,0,7,0,0,7,0,0,9,0,0,0,1,0,0,0,0,2,0,1,6,0,1,0,0,1,0,0,5,2,5,0,0,0,6,6,0,0,0,5,0,0,0,0,0,8,0,9,0,7,5,0,0,0,0,0,2,0,0,0,6,0,6,0,1,2,1,9,1,0,1,0,0,0,1,1,2,0,1,5,0,1,2,1,9,0,9,1,0,0,6,1,2,0,0,0,0,0,5,6,0,1,7,1,6,0,0,0,0,0,0,0,1,1,2,0,8,0,0,0,1,0,1,0,2,0,0,1,0,1,1,7,1,5,9,0,0,5,2,0,1,5,0,0,0,0,0,0,0,0,1,0,5,0,0,0,0,2,0,1,0,0,1,2,0,0,0,0,1,0,2,0,6,2,0,1,1,0,1,0,0,2,7,0,7,4,0,1,5,0,0,0,0,1,0,2,2,6,0,1,0,0,0,0,2,0,0,5,1,7,0,0,0,6,0,2,1,0,0,0,1,0,0,0,2,0,1,0,0,0,9,6,1,0,0,7,1,6,0,0,1,7,0,9,1,1,1,0,0,6,0,0,1,0,0,0,0,0,0,5,6,1,0,1,2,1,6,9,0,7,7,9,5,1,0,1,0,2,0,1,2,1,7,1,9,9,0,2,0,0,0,0,9,5,9,6,7,0,0,0,0,1,0,2,0,6,0,1,2,0,6,0,1,2,1,0,0,0,0,0,7,2,0,1,0,0,0,4,0,7,7,0,1,0,2,9,0,1,0,5,0,2,0,0,7,1,2,0,7,2,0,0,0,0,0,0,7,0,0,0,9,6,0,0,0,0,1,0,5,5,0,0,6,0,1,6,0,0,0,2,2,0,9,2,0,0,5,0,0,2,0,0,0,9,0,0,0,7,1,1,0,0,9,2,1,5,9,2,0,0,0,0,9,0,1,0,9,1,2,0,0,0,0,0,4,0,0,0,0,7,5,2,0,7,2,9,5,0,1,1,0,0,0,1,2,1,5,0,0,7,0,0,0,1,0,6,1,1,0,1,5};
//500    	int micePosition[] = {661,68,197,987,438,612,743,123,644,75,681,918,313,721,384,248,76,331,318,832,463,398,275,688,382,563,788,666,630,678,241,123,408,217,137,241,298,161,904,632,691,870,31,696,533,743,414,159,812,481,170,226,122,878,374,161,159,465,484,455,345,505,582,160,862,936,633,940,528,29,87,222,171,700,68,679,70,330,715,997,662,292,311,350,432,627,873,619,613,839,480,213,979,773,153,525,845,619,276,870,500,674,542,599,497,643,956,733,36,413,122,691,311,289,9,55,973,187,615,819,877,699,971,847,626,633,956,153,990,748,892,955,66,159,166,69,316,752,115,589,230,873,300,129,207,759,123,128,946,974,883,658,980,629,68,694,902,562,805,718,274,634,998,331,953,645,150,14,679,360,162,946,900,501,363,530,915,893,226,213,762,959,965,870,997,800,418,515,714,773,163,562,703,694,990,382,827,247,171,132,891,369,381,468,256,272,949,82,372,301,571,603,678,807,193,729,314,745,895,229,740,43,943,53,734,349,616,985,404,423,932,69,946,367,154,231,55,950,732,715,101,263,571,34,288,467,519,132,219,9,96,281,941,306,980,744,842,37,121,815,727,469,919,586,201,414,865,893,795,41,758,945,231,33,769,261,416,373,145,632,770,683,136,436,789,790,642,413,928,596,234,534,135,982,369,10,383,1000,16,206,992,66,264,211,670,763,908,111,771,456,944,433,600,80,962,431,612,664,846,363,353,359,263,993,182,222,292,502,934,153,464,422,824,983,183,326,704,375,971,761,80,921,404,963,26,542,97,293,308,644,436,449,909,771,133,361,64,454,664,751,82,987,960,971,915,331,255,346,826,894,55,385,72,297,520,63,4,249,986,654,486,248,292,596,982,197,562,336,348,703,974,394,583,399,287,365,936,676,186,457,340,49,958,307,5,490,187,134,295,559,97,121,327,452,249,244,895,14,710,431,98,330,164,638,744,218,268,474,526,323,679,789,25,951,3,854,633,309,420,208,969,766,326,358,938,644,364,202,425,904,54,522,558,931,979,158,481,427,351,335,822,206,61,890,629,409,209,207,823,866,257,577,484,234,745,177,47,610,960,512,169,367,913,127,835,139,960,765,701,589,384,117,788,260,454,638,579,680,275,753};
//500    	int holeCapacity[] = {0,0,9,0,5,5,0,0,2,0,0,0,0,0,1,9,1,1,0,0,9,0,0,0,2,0,0,0,0,6,0,2,0,0,1,0,0,1,9,0,0,0,0,2,0,0,7,0,0,0,0,0,0,2,1,0,0,0,5,9,6,0,5,1,0,0,9,7,0,0,1,1,0,0,0,2,1,6,0,5,2,0,9,1,6,5,0,7,0,0,2,1,0,0,0,7,2,0,0,0,5,9,2,2,0,0,1,0,0,2,2,0,1,0,0,0,0,2,9,0,9,0,5,2,1,5,0,1,9,2,1,0,0,2,0,9,0,0,1,1,0,0,0,0,0,0,0,0,0,2,0,1,0,0,1,0,0,2,2,2,0,2,0,1,7,7,0,0,2,0,1,1,0,7,0,7,1,0,0,0,0,0,1,0,1,1,0,0,0,0,9,1,0,0,1,1,1,1,0,1,6,6,0,0,0,0,5,0,1,0,1,0,1,0,2,6,0,0,0,0,0,0,1,0,6,9,0,6,0,0,0,0,0,7,1,2,2,0,0,6,1,0,1,0,0,1,1,0,6,1,1,6,0,6,2,5,5,5,1,9,0,6,7,0,2,9,0,2,1,0,1,0,6,5,9,0,1,9,0,6,0,1,0,0,2,0,1,6,0,7,7,2,0,1,0,2,1,1,9,0,0,7,9,0,1,0,6,0,0,1,5,0,0,7,0,1,0,1,5,1,9,0,0,0,0,0,0,0,0,0,2,5,1,2,9,7,0,0,1,1,2,1,0,0,0,0,2,0,0,0,0,0,6,1,9,1,0,0,0,5,9,0,0,0,0,0,2,2,0,1,0,7,6,0,0,7,2,0,0,6,1,0,6,0,0,1,0,0,1,0,1,1,6,9,6,0,0,1,1,1,1,0,0,0,6,0,0,0,0,0,1,1,0,1,0,0,0,0,0,2,0,0,2,1,9,0,0,2,0,1,2,1,0,0,1,1,6,5,2,6,5,0,0,0,0,0,0,5,1,6,0,0,1,7,5,0,0,1,0,6,2,5,1,0,9,0,0,1,0,9,7,1,0,0,2,0,0,2,0,9,1,0,0,0,7,6,6,0,1,9,1,1,1,0,0,0,0,6,0,5,9,0,0,0,0,5,0,0,0,5,0,0,1,0,7,2,0,1,0,7,0,1,1,0,1,1,0,0,0,1,0,0,0,0,0,2,1,1,0,0,0,0,2,7,6,0,2,0,0,0,2,0,1,0,0,1,5,2,2,1,0,7,0,0,1,9,0,1,0,0,7,1,2,1,0,0,0,0,0,0,1,1,1,2,1,0,0,6,1,1,0,0,1,0,2,2,0,0,6,0,0,0,0,0,0,0,0,0,2,7,0,0,5,9,5,1,1,0,1,0,0,7,0,0,0,0,0,0,5,1,0,1,2,0,0,6,1,1,5,5,2,2,0,0,0,1,0,6,9,1,5,0,1,1,6,2,1,0,0,1,2,1,0,9,9,1,0,1,2,7,1,0,2,1,0,0,0,0,0,9,1,0,0,7,0,1,2,0,7,0,0,0,0,1,9,0,0,0,5,0,0,0,0,0,6,1,7,0,0,6,0,2,6,0,0,7,9,1,0,1,1,2,7,0,1,5,1,0,0,0,0,1,0,0,0,1,1,6,2,7,1,1,1,1,1,7,0,7,1,0,0,1,5,0,0,7,2,1,1,6,2,6,0,1,0,6,2,0,6,1,0,7,0,0,1,7,1,1,0,0,0,0,0,0,1,6,1,9,5,0,1,9,0,0,1,0,0,0,6,7,1,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,0,6,0,1,2,1,7,1,0,0,0,1,1,0,1,1,1,0,1,0,6,0,0,1,2,0,0,0,1,9,0,0,0,0,2,0,5,2,6,2,0,9,2,0,0,0,5,0,9,0,0,0,1,2,6,6,0,0,2,0,6,0,0,5,0,0,0,0,2,0,9,1,0,2,0,1,0,7,1,1,5,0,0,0,7,0,2,0,1,0,0,0,0,2,9,0,0,2,0,6,0,2,1,0,0,1,0,9,0,1,0,6,2,0,1,5,2,0,1,1,0,6,9,7,7,0,0,0,2,0,2,2,0,1,0,1,6,5,9,0,1,1,1,6,1,2,0,0,0,1,6,1,0,6,1,1,2,0,0,1,1,0,1,0,0,1,9,0,1,5,2,0,0,2,0,0,0,0,9,6,1,1,0};

    	int micePosition[] = {515,562,382,143,178,540,424,39,25,578,63,510,506,481,422,218,584,77,340,514,64,580,82,451,142,470,449,489,371,348,189,272,327,397,435,317,264,215,355,324,148,489,4,560,562,341,494,164,568,412,390,292,399,462,467,286,227,429,153,131,119,126,169,371,268,323,452,164,267,373,376,172,424,543,464,54,112,158,565,240,28,577,514,588,466,354,459,272,106,94,20,36,5,251,278,387,14,95,252,527,600,30,344,269,391,235,243,235,467,248,213,599,573,345,156,291,168,595,53,428,247,25,199,23,591,434,311,129,132,415,336,266,233,142,267,596,268,201,359,523,295,441,575,230,27,200,284,129,544,325,277,197,82,157,372,99,501,69,1,374,91,147,2,113,207,390,32,218,26,409,227,268,295,309,410,270,82,209,548,514,358,315,202,365,476,548,526,376,23,37,367,370,496,344,298,283,423,126,165,383,303,30,165,584,105,586,169,457,163,417,72,577,137,405,345,156,538,533,37,379,133,311,213,208,412,86,146,246,144,318,486,32,211,215,325,56,48,14,87,319,533,23,111,145,442,400,103,47,589,109,5,547,242,340,369,392,239,26,72,596,152,189,324,177,275,415,519,153,210,412,183,135,383,597,561,589,392,408,77,315,361,269,398,21,278,83,152,467,492,260,382,347,548,439,161,224,581,574,214,24};
    	int holeCapacity[] = {0,6,5,0,0,1,0,2,0,1,9,6,0,7,2,0,0,0,6,1,0,6,5,0,0,0,2,1,0,2,6,2,0,0,2,0,0,0,9,6,6,0,0,0,0,1,0,1,7,1,2,5,2,0,1,1,0,0,0,5,5,0,0,0,2,0,1,0,1,0,0,0,2,2,0,0,1,2,0,2,1,5,1,0,1,1,0,0,0,7,2,0,1,0,6,1,7,0,0,2,9,1,1,0,1,0,5,0,5,0,1,0,0,0,6,0,0,7,0,0,0,0,1,1,1,0,0,2,9,0,0,0,0,0,0,1,1,5,1,0,1,0,0,6,7,1,1,0,1,0,6,1,7,0,2,0,7,0,9,0,2,1,1,1,0,0,2,1,2,0,1,2,0,0,0,0,0,1,0,2,9,1,9,7,0,1,0,0,2,0,6,2,0,0,0,0,0,0,1,1,0,0,0,9,6,1,1,6,2,1,0,1,7,5,0,7,2,0,0,7,1,0,0,1,0,0,5,0,9,2,0,1,1,0,0,0,6,0,0,0,1,2,1,0,1,0,6,0,5,0,1,1,9,6,0,0,0,0,5,6,0,6,0,0,1,0,0,0,2,0,0,0,0,9,0,0,1,0,0,2,0,0,6,0,0,1,0,0,0,0,0,0,0,0,1,0,1,7,2,5,0,0,7,5,0,6,0,0,6,0,0,1,0,1,5,5,0,1,0,9,1,2,2,6,0,6,0,0,1,2,0,0,1,0,6,0,0,7,2,1,2,0,2,0,1,1,7,6,0,5,0,7,0,0,0,1,0,0,9,1,0,0,0,1,6,0,2,0,7,1,1,1,1,0,0,7,0,0,1,1,1,2,5,0,0,1,1,0,7,1,0,0,1,0,2,2,0,1,5,0,0,2,6,6,0,0,0,0,6,1,0,0,2,0,0,1,0,1,0,0,0,5,7,0,0,2,0,6,2,5,5,0,0,0,0,1,2,0,7,0,0,0,9,0,0,1,7,6,0,2,5,0,0,0,0,0,2,0,9,1,0,9,0,6,0,5,7,0,0,0,6,5,0,0,0,0,9,0,0,2,0,2,0,0,6,0,1,1,0,2,0,0,5,0,0,0,1,0,9,0,9,7,0,7,0,7,0,0,0,0,0,0,0,1,1,9,0,1,0,0,2,0,1,1,1,1,1,2,0,0,0,0,2,0,1,0,0,1,2,6,5,0,0,2,0,0,0,1,0,7,0,2,0,0,1,2,1,0,0,1,1,0,0,0,2,5,0,0,5,0,0,0,0,0,1,0,1,6,0,0,1,1,2,0,0,0,1,2,0,0,2,0,7,0,0,1,5,0,0,0};
    	
//200    	int micePosition[] = {392,335,397,24,233,239,180,39,186,354,283,31,229,299,182,79,282,203,191,45,326,13,125,361,216,42,146,141,82,249,364,165,384,330,326,357,201,54,170,297,176,28,313,321,349,174,265,235,206,291,257,150,144,144,345,349,67,372,59,197,344,123,399,304,320,356,92,62,264,144,91,182,391,258,182,244,116,42,315,18,286,116,209,382,57,135,339,367,279,332,37,338,279,107,398,271,160,349,367,32,127,180,224,266,275,208,264,262,244,106,26,5,266,16,114,379,318,252,15,370,399,67,144,258,171,112,142,251,323,328,133,247,3,259,357,282,138,242,216,157,292,252,259,299,285,202,214,140,85,398,297,18,13,187,139,122,31,103,276,358,50,60,327,205,129,314,89,397,165,296,341,88,180,287,169,262,212,7,54,143,287,30,29,399,225,395,28,231,191,288,187,115,256,214,191,100,353,181,52,100};
//200    	int holeCapacity[] = {0,2,9,0,0,2,0,0,0,0,0,9,7,0,6,0,9,0,5,0,5,6,1,0,0,1,0,1,0,2,1,1,0,6,1,0,0,7,1,7,0,0,1,1,9,8,0,9,0,1,6,0,0,1,0,0,0,1,0,0,8,0,5,1,0,0,0,0,1,5,0,0,1,1,0,1,5,0,1,1,2,0,0,0,0,5,0,2,6,0,4,0,0,0,0,2,0,0,1,6,0,4,7,2,0,0,1,0,0,5,1,7,2,0,0,0,0,0,0,0,5,1,0,0,1,0,2,0,0,0,1,0,1,0,0,0,1,0,1,5,9,0,5,6,0,0,0,1,1,0,0,1,0,0,1,0,6,0,0,9,7,0,0,6,1,1,0,1,0,9,0,0,0,6,0,1,0,0,1,6,0,2,0,0,1,0,1,0,6,0,8,0,2,5,6,0,4,2,0,0,0,0,6,0,7,0,0,0,1,9,0,0,0,0,0,0,1,9,0,0,0,0,0,1,2,0,8,0,0,5,1,1,0,6,1,0,2,4,0,9,1,5,7,7,0,1,0,0,9,1,1,6,7,0,0,0,5,0,0,0,0,7,9,0,0,1,0,0,0,5,0,0,1,0,5,1,0,0,9,0,0,0,1,1,0,0,1,0,1,0,0,0,4,6,1,0,5,1,1,4,0,0,2,0,0,5,0,7,9,1,0,0,1,9,0,7,2,0,0,0,5,0,0,5,0,2,0,8,1,7,7,5,2,1,2,0,0,0,6,0,5,0,0,0,6,0,0,0,1,0,1,1,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,5,2,0,0,0,6,1,0,2,0,2,0,1,4,0,0,9,2,1,5,1,8,6,0,2,4,0,0,1,1,0,0,0};
//100    	int micePosition[] = {90,86,155,86,163,134,70,17,68,194,63,139,7,181,28,57,25,140,189,171,99,18,158,17,134,47,38,91,148,97,49,56,79,10,79,17,194,171,134,45,145,153,65,96,100,172,89,167,73,42,88,12,31,32,153,109,164,44,112,85,198,141,75,126,48,144,50,81,39,159,125,99,142,168,133,145,137,154,36,179,197,111,13,99,136,66,55,95,144,115,145,111,107,39,118,141,67,46,66,199};    	
//100    	int holeCapacity[] = {0,0,0,0,7,9,0,1,1,2,0,0,7,0,0,2,1,0,2,0,2,9,1,0,0,0,2,0,1,0,1,0,0,0,2,0,9,9,0,1,2,0,0,0,9,1,0,0,1,0,1,0,2,2,0,2,2,0,5,2,9,0,2,0,2,0,2,0,1,1,5,0,0,0,7,2,1,0,0,5,0,1,0,0,1,1,1,2,0,0,5,0,0,0,1,2,0,0,0,0,5,2,2,1,0,7,2,0,2,0,0,0,0,2,1,0,0,5,7,2,0,5,0,5,0,2,2,0,0,2,0,1,0,1,7,2,2,0,0,1,0,0,0,0,0,0,2,0,7,0,0,9,0,1,0,1,1,2,0,0,7,2,1,0,0,0,0,2,0,5,0,1,1,1,0,2,5,0,0,9,6,0,0,0,0,5,2,0,1,9,0,1,0,0,2,0,2,0,0,0};    	
//20    	int micePosition[] = {33,34,7,10,11,8,7,18,31,32,13,39,39,40,5,9,13,40,13,24};
//20    	int holeCapacity[] = {0,0,0,9,0,0,9,1,0,0,2,0,1,0,1,1,0,6,0,0,0,5,1,0,7,5,9,0,0,0,7,0,0,0,1,0,2,0,1,0};
//10    	int micePosition[] = {4, 0, 6, 7, 9, 4, 8, 1, 17, 19};
//10    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 1, 0, 0, 2, 5, 0, 1};
//9    	int micePosition[] = {4, 0, 6, 7, 9, 4, 8, 1, 17};
//9    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 1, 0, 0, 1, 4};
//8    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 2, 0, 0};
//7    	int micePosition[] = {4, 0, 6, 7, 9, 4, 8};
//7    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 0, 1};
//6    	int micePosition[] = {4, 0, 6, 7, 9, 4};
//6    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 6, 1, 0, 0};
//5    	int micePosition[] = {4, 0, 6, 7, 9};
//5    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0, 4, 0};
//4    	int micePosition[] = {4, 0, 6, 7};
//4    	int holeCapacity[] = {0, 1, 6, 7, 0, 0, 0, 0};

    	Estado getInicial() { return new MiceAndHoles(micePosition,holeCapacity); }
    }
    
    public static void main(String[] args) {
        AppletDemoBusca applet = new AppletDemoBusca();
        applet.isStandalone = true;
        Frame frame;
        frame = new Frame() {
            protected void processWindowEvent(WindowEvent e) {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                    System.exit(0);
                }
            }
            public synchronized void setTitle(String title) {
                super.setTitle(title);
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            }
        };
        frame.setTitle("Demonstracao de algoritmos de busca");
        frame.add(applet, BorderLayout.CENTER);
        applet.init();
        applet.start();
        frame.setSize(700,500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }
}
