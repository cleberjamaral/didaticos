/* Multiagent System to Small Series Production
 * BRAGECRIM 013/09 
 * Authors: LOCH G. N.; RIGOBELLO, T. F.; ROLOFF, M. L.; SOUZA, V. O.
 * 
 * Resumo:  este artefato �� a forma como o agente planner interage com o ambiente.
 * O agente tem como objetivo definir qual ser�� a sequ��ncia de produtos a serem produzidos
 * a partir do in��cio da opera����o do Sistema Multiagente. Ele receber�� uma lista de produtos
 * a serem produzidos selecionados pelo usu��rio na interface do ScadaBR. O agente dever�� criar uma
 * lista ordenada dos produtos e ap��s enviar o produto a ser produzido para o agente configurador no
 * momento oportuno.
 *  
 * 2013-05-06 - MAS initial infrastructure for SSP 
 */

// CArtAgO artifact code for project mas2ssp

package artifacts;

import cartago.*;

/**
 * Artifact out ports
 */
@ARTIFACT_INFO(outports = { @OUTPORT(name = "out-1"), @OUTPORT(name = "in-1") })

public class Counter extends Artifact {

	void init(int initialValue) throws Exception {
		defineObsProperty("count", initialValue);
	}
	
	@LINK
	void inc2() {
		log("Counter:inc2 called! A tick signal is going to be send.");
		//signal("tick");
	}

	@LINK
	void inc3(String str, int i) {
		log("Counter:inc3 called! A tick signal is going to be send. Parameters: " + str + ", " + i);
		//signal("tick");
	}

	@LINK
	void inc(OpFeedbackParam<String> value) {
		defineObsProperty("count", 1);
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue()+1);
		signal("tick");
		value.set(prop.toString());
	}

	@LINK
	void setValue(int newValue, OpFeedbackParam<String> oldValue) {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(newValue);
		log("setValue invoked sucessfully! old: "+prop.toString()+", received: "+ newValue +" opid:"+thisOpId.toString());
		oldValue.set(prop.toString());
	}

	@LINK
	void writeinputAr(String v) {
		log("writeinputAr invoked sucessfully! received: "+ v +" opid:"+thisOpId.toString());
	}

}

