// Agent owner in project beersponsor.mas2j
/* Initial beliefs and rules */

/* Initial goals */
!ask(beer).

/* Plans */
@a1
+!ask(beer) : true 
	<- .send(robot, achieve, bringbeer(owner,beer));
	.print("Go robot, bring a beer!").
	
@h1
+has(owner,beer)[source(robot)] : true
	<- 	!drink(beer);
		.print("I want more!");
		!ask(beer).	

@d1
+!drink(beer) : has(owner,beer)
	<- .send(robot, achieve, masterdrunkabeer(owner));
	.print("Yeah, I have drunk a cold beer!").
	
@s1
+addstock(beer,N) : true
	<- .print("Since there is beer, bring it!");
	.send(sponsor, tell, thanks(sponsor));
	!ask(beer).