// CArtAgO artifact code for project camelJaCaMoRobot

package camelartifacts;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import camelartifact.ArtifactComponent;
import camelartifact.CamelArtifact;
import cartago.ARTIFACT_INFO;
import cartago.OUTPORT;

/**
 * Artifact out ports TODO Cleber: Ask Cranefield, is it necessary to be in
 * Interface too? Is it part of the class and inherited?
 */
@ARTIFACT_INFO(outports = { @OUTPORT(name = "out-1"), @OUTPORT(name = "in-1") })
public class Router extends CamelArtifact {

	void init() {

		final Random rand = new Random();
		
		final CamelContext camel = new DefaultCamelContext();
		camel.addComponent("artifact", new ArtifactComponent(this/*
																 * this.
																 * incomingOpQueue
																 */));

		/* Create the routes */
		try {
			camel.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {
					from("timer:test?period=500")
							.process(new Processor() {
								public void process(Exchange exchange)
										throws Exception {
									log("Processing new messages...");

									Map<String, Object> throwData = new HashMap<String, Object>();
									throwData.put("inc", null);
									throwData.put("setValue", rand.nextInt(50));
									/**
									 * Delivering a MAP of events based on two
									 * strings (key, value)
									 */

									exchange.getIn().setHeader("ArtifactName",
											"counter");
									exchange.getIn().setHeader("OperationName",
											"inc");
									exchange.getIn().setBody(throwData);

								}
							}).to("artifact:cartago")
							.to("log:CamelArtifactLogger?level=info");

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// start routing
		log("Starting camel...");
		try {
			camel.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log("Starting router...");
	}
}
