#!/usr/bin/python
from flask import Flask, jsonify
from flask import abort
from flask import make_response
from flask import request
from flask import url_for
from flask import send_file

import os

app = Flask(__name__)

### Como listar arquivos em um diretorio e em seus subdiretorios ###

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/listaarquivos
#
@app.route('/listaarquivos', methods=['GET'])
def obtem_arquivos():
	lista = []
	#https://docs.python.org/2/library/os.html
	for root, dirs, files in os.walk('.'):
		for nome in files:
			linha = {}
			linha['nome'] = nome
			linha['tipo'] = 'arquivo'
			linha['caminho'] = root
			lista.append(linha)
	return jsonify({'arquivos': lista}), 201

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/listaarquivos/{extensao-desejada}
# curl -i http://localhost:5000/listaarquivos/pdf
#
@app.route('/listaarquivos/<extensao>', methods=['GET'])
def obtem_arquivos2(extensao):
	lista = []
	#https://docs.python.org/2/library/os.html
	for root, dirs, files in os.walk('.'):
		for nome in files:
			if nome.endswith(extensao):
				path = os.path.join(root, nome)
				size = os.stat(path).st_size # in bytes
				linha = {}
				linha['tamanho'] = size
				linha['caminho'] = root
				linha['nome'] = nome
				lista.append(linha)
	return jsonify({'arquivos': lista}), 201

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/detalhesarquivo/nome.extensao
#
@app.route('/detalhesarquivo/<busca>', methods=['GET'])
def detalhe_arquivo(busca):
	lista = []
	#https://docs.python.org/2/library/os.html
	for root, dirs, files in os.walk('.'):
		for nome in files:
			path = os.path.join(root, nome)
			size = os.stat(path).st_size # in bytes
			linha = {}
			linha['tamanho'] = size
			linha['caminho'] = root
			linha['nome'] = nome
			lista.append(linha)
	resultado = [resultado for resultado in lista if resultado	['nome'] == busca]
	if len(resultado) == 0:
		abort(404)
	return jsonify({'arquivo': resultado[0]})

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/detalhesarquivo
#
@app.route('/detalhesarquivo', methods=['GET'])
def lista_arquivos():
	lista = []
	for root, dirs, files in os.walk('.'):
		for nome in files:
			path = os.path.join(root, nome)
			size = os.stat(path).st_size # in bytes
			linha = {}
			linha['tamanho'] = size
			linha['caminho'] = root
			linha['nome'] = nome
			lista.append(linha)
	return jsonify({'arquivos': [tornar_caminho_navegavel(arquivo) for arquivo in lista]})

# tornando os links navegaveis
def tornar_caminho_navegavel(arquivo):
	novo_arquivo = {}
	for field in arquivo:
		if field == 'nome':
			novo_arquivo['uri'] = url_for('detalhe_arquivo', busca=arquivo['nome'], _external=True)
		novo_arquivo[field] = arquivo[field]
	return novo_arquivo

# Como invocar (Use seu navegador web, p.e. Firefox)
#
# http://localhost:5000/obterarquivo/{caminho/nome.extensao}
# http://localhost:5000/obterarquivo/foto.png
#
@app.route('/obterarquivo/<path:filename>')
def download_file(filename):
	if '..' in filename or filename.startswith('/'):
		abort(404)
	return send_file(filename,None,False) # True envia como anexo e False como embutido.

# Para apresentar erro 404 HTTP se tentar acessar um recurso que nao existe
@app.errorhandler(404)
def not_found(error):
	return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == "__main__":
	print "Servidor no ar!"
	app.run(host='0.0.0.0', debug=True)
