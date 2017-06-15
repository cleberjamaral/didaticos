% * * * * * * * * * * * * * * * * * * * * * * * * * * 
% Premissas
% * * * * * * * * * * * * * * * * * * * * * * * * * * 
canario(piupiu).
peixe(nemo).
tubarao(tutu).
vaca(mimosa).
morcego(vamp).
avestruz(xica).
salmao(alfred).

% * * * * * * * * * * * * * * * * * * * * * * * * * * 
% Regras
% * * * * * * * * * * * * * * * * * * * * * * * * * * 
temPele(X) :- 
	ehAnimal(X).

ehAnimal(X) :- 
	ehPeixe(X);
	ehPassaro(X);
	ehMamifero(X).	

temNadadeira(X) :- 
	ehPeixe(X).

podeNadar(X) :- 
	ehPeixe(X).

temAsa(X) :-
	passaro(X).

podeVoar(X) :-
	(passaro(X),
	\+ avestruz(X));
	morcego(X).

poeOvo(X) :- 
	peixe(X), 
	\+ tubarao(X),
	passaro(X).

ehPeixe(X) :- 
	tubarao(X);
	salmao(X);
	peixe(X).

ehAmarelo(X) :-
	canario(X).

ehPassaro(X) :-
	canario(X);
	avestruz(X).

ehMamifero(X) :-
	morcego(X);
	vaca(X).

podeAndar(X) :-
	ehMamifero(X),
	\+ morcego(X).

daLeite(X) :-
	vaca(X).

daCarne(X) :-
	vaca(X).

% * * * * * * * * * * * * * * * * * * * * * * * * * * 
% Testes e relatório
% * * * * * * * * * * * * * * * * * * * * * * * * * * 
rel :- ehAnimal(X), print('Animal: '), print(X), nl, fail.
rel :- ehPassaro(X), print('Passaro: '), print(X), nl, fail.
rel :- ehPeixe(X), print('Peixe: '), print(X), nl, fail.
rel :- ehMamifero(X), print('Mamífero: '), print(X), nl, fail.
rel :- podeVoar(X), print('Voa: '), print(X), nl, fail.
rel :- poeOvo(X), print('Ovíparo: '), print(X), nl, fail.
rel.
