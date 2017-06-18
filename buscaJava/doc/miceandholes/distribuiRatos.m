%Original
m = [4, 0, 6, 7];
h = [0 1 2 0 0 0 1 1 0];
d = size(m)(2)/sum(h)

for cenario = 1:10
	%Multiplica quantidade de ratos para os diferentes cenarios
	if cenario == 1
	    qtRatos = 4
	elseif cenario == 2
	    qtRatos = 5
	elseif cenario == 3
	    qtRatos = 6
	elseif cenario == 4
	    qtRatos = 8
	elseif cenario == 5
	    qtRatos = 10
	elseif cenario == 6
	    qtRatos = 20
	elseif cenario == 7
	    qtRatos = 100
	elseif cenario == 8
	    qtRatos = 300
	elseif cenario == 9
	    qtRatos = 500
	else
	    qtRatos = 1000
	end

	m1 = [];
	h1 = [];
	for i = 1: (qtRatos*2) h1 = [h1 randi([0,9])]; end;
	for i = 1: (qtRatos*1) m1 = [m1 randi(size(h1)(2))]; end;
	d1 = size(m1)(2)/sum(h1);


	%Normaliza se a diferença da taxa de ocupação for maior que 1%
	while ((d1 > 0.86) || (d1 < 0.80))
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

	fileID = fopen('exp.txt','a');
	fprintf(fileID,'//Gerado cenario %d ratos, densidade: %f\n',qtRatos,d1)
	fprintf(fileID,'int micePosition[] = {')
	fprintf(fileID,'%d,',m1)
	fprintf(fileID,'};\n')
	fprintf(fileID,'int holeCapacity[] = {')
	fprintf(fileID,'%d,',h1)
	fprintf(fileID,'};\n')
	fclose(fileID);
end
