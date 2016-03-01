// Agent robot in project beersponsor.mas2j

/* Initial beliefs and rules */
// initially, I believe that there are N beers in the fridge
available(beer,fridge).
stock(beer,3).

/* Initial goals */

/* Plans */
@b1
+!bringbeer(owner,beer)
	: available(beer,fridge)
	<- -at(robot,owner); // Have sure this old belief is out
	!at(robot,fridge);
	.print("Grabbing a beer to my master.");
	!at(robot,owner);
	.date(YY,MM,DD); .time(HH,NN,SS);
	.send(owner, tell, has(owner,beer));
	.print("Here is your beer my master.");
	?stock(beer,Inventory);
	!updateStock(beer,Inventory);
	+consumed(YY,MM,DD,HH,NN,SS,beer).

@b2
+!bringbeer(owner,beer)
	: not available(beer,fridge)
	<- .print("Sorry, there is no beer!").
	
@a1
+!at(robot,P) : true// : not at(robot,P)
	<- -at(robot,_); // Have sure the old belief is out
	+at(robot,P);
	.print("Robot is at ",P).	
@m1	
+!masterdrunkabeer(owner) : true
	<- .send(owner, untell, has(owner,beer)).
	
@u1
+!updateStock(beer,Inventory) : Inventory > 1
	<- ?stock(beer,Tmp);
	Inv = Tmp - 1; 
	-+stock(beer,Inv);
	.print("Inventory had ",Inventory," beer(s). A beer is being taken, now the fridge has ",Inv," beer(s).").

@u2 //last beer
+!updateStock(beer,Inventory) : Inventory == 1
	<- -+stock(beer,0);
	-available(beer,fridge);
	.print("Inventory had its last beer. Master needs to ask supermarket for more beer!").
	
@u3	
+!updateStock(beer,Inventory) : Inventory == 0
	<- -available(beer,fridge);
	.print("Did I already told that there is no beer?").
	
@s1
+addstock(beer,N) : N > 0
	<- -+stock(beer,N);
	+available(beer,fridge);
	.print("More ",N," beers just arrived! There are available beer again!").
