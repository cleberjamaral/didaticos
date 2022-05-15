# Didaticos
Projetos didaticos

## Minesweeper
Jogo "Campo minado" feito em python3 com a biblioteca gráfica TKinter. A implementação segue o tutorial feito por Jim e disponível no freecodecamp (https://www.freecodecamp.org/news/object-oriented-programming-with-python-code-a-minesweeper-game/). 

* Esta implementação pode ser realizada e testada via gitpod. Para isso basta abrir uma instância no gitpod direcionada a este repositório:
https://gitpod.io/#https://github.com/cleberjamaral/Didaticos, o gitpod deve abrir uma instância utilizando o Dockerfile presente na raiz deste repositório, que por sua vez utiliza a máquina ```workspace-full-vnc``` que tem suporte a vnc, permitindo que um cliente vnc compatível com web browser se conecte com a máquina e exiba a interface gráfica do sistema operacional hospedeiro.
* Para testar, é necessário dar acesso à porta 6080 (conforme configuração realizada no arquivo ```.gitpod``` presente na raiz deste repositório). O executável python3 que está direcionado a biblioteca que contém o TKinter configurado está em ```/usr/bin```, portanto, é necessário executar o comando ```/usr/bin/python3 workspace/Didaticos/minesweeper/main.py``` para que tudo funcione adequadamente.