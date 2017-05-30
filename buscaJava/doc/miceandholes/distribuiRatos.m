%Original
m = [4, 0, 6, 7, 19, 4, 8, 9, 17, 10];
h = [0 1 6 7 0 0 0 0 9 1 0 0 2 1 0 0 2 5 0 1];
d = size(m)(2)/sum(h)

%Cria 10x mais ratos e distribui em um corredor 2 x maior
m1 = [];
h1 = [];
for i = 1: (size(h)(2)*30) h1 = [h1 h(randi(numel(h)))]; end;
for i = 1: (size(m)(2)*30) m1 = [m1 randi(size(h1)(2))]; end;
d1 = size(m1)(2)/sum(h1)


%Normaliza se a diferença da taxa de ocupação for maior que 1%
while ((d/d1 > 1.01) || (d1/d < 0.99))
	p = randi(size(h1)(2));
	if (d/d1 > 1) %deve-se diminuir vagas
		if (h1(p) > 0)
			h1(p) = h1(p) - 1;
		end
	else %deve-se aumentar vagas
		if (h1(p) < size(h1)(2))
			h1(p) = h1(p) + 1;
		end;
	end;
	d1 = size(m1)(2)/sum(h1);
end;
size(m1)(2)

fprintf('int micePosition[] = {')
fprintf('%d,',m1)
fprintf('};\n')
fprintf('int holeCapacity[] = {')
fprintf('%d,',h1)
fprintf('};\n')


%{
m1 = [4, 0, 6, 7, 9, 4, 8, 1, 17, 19];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 1, 0, 0, 2, 5, 0, 1];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7, 9, 4, 8, 1, 17];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 1, 0, 0, 1, 4];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7, 9, 4, 8, 1];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 2, 2, 0, 0];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7, 9, 4, 8];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 9, 1, 0, 0, 0, 1];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7, 9, 4];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 6, 1, 0, 0];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7, 9];
h1 = [0, 1, 6, 7, 0, 0, 0, 0, 4, 0];
d1 = size(m1)(2)/sum(h1)
m1 = [4, 0, 6, 7];
h1 = [0, 1, 6, 7, 0, 0, 0, 0];
d1 = size(m1)(2)/sum(h1)
%}
