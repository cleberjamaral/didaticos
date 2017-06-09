canario(piupiu).
peixe(nemo).
tubarao(tutu).
morcego(vamp).
avestruz(xica).
salmao(alfred).

temPele(X) :- 
	animal(X).

temNadadeira(X) :- 
	peixe(X).

podeNadar(X) :- 
	peixe(X).

poeOvo(X) :- 
	peixe(X), 
	tubarao(X),
	passaro(X).

peixe(X) :- 
	tubarao(X),
	salmao(X),
	peixe(X).

%poeOvo(X) 	:- \+ mamifero(X).
%poeOvo(X)	:- passaro(X).

