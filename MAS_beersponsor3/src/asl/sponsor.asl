// Agent sponsor in project beersponsor.mas2j
@g1
+!giving_beer   // this goal is created by the GUI of the agent 
    <- .broadcast(tell, addstock(beer,2));
	.print("Broadcasting about the delivery.").
	   
@t1 
+thanks(sponsor)[source(owner)] : true
	<- thankyou(sponsor);
	.print("Thanking sponsor.").