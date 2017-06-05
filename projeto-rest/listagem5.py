#!/usr/bin/python
from flask import Flask, jsonify
from flask import abort
from flask import make_response
from flask import request
import subprocess

import os

app = Flask(__name__)

# Como invocar na linha de comando
# Invocando o leitor de PDFs xpdf tem um bug no ubuntu
# Editar sudo vi /usr/bin/xpdf +27 e acrescenter -exec no case
# -z|-g|-geometry|-remote|-rgb|-papercolor|-eucjp|-t1lib|-ps|-paperw|-paperh|-upw|-exec)
#
# curl -i http://localhost:5000/xpdf/{arquivo.pdf}
#
@app.route('/xpdf/<nome>', methods=['GET'])
def abre_pdf(nome):
	# Chamada de processo nao bloqueante - fica em background
	parametro = '-remote projetor ' + nome
	p1 = os.spawnlp(os.P_NOWAIT,"xpdf","xpdf",parametro)
	return jsonify({'resultado': True}), 201

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/xpdf/{arquivo.pdf}/2
#
@app.route('/xpdf/<nome>/<int:pagina>', methods=['GET'])
def avanca_paginas(nome,pagina):
	pagina = 'gotoPage('+str(pagina)+')'
	# chamada de processo bloqueante, porem o xpdf termina logo apos a execucao
	subprocess.call(['xpdf','-remote','projetor','-exec',pagina])
	return jsonify({'resultado': True}), 201

# Para apresentar erro 404 HTTP se tentar acessar um recurso que nao existe
@app.errorhandler(404)
def not_found(error):
	return make_response(jsonify({'erro': 'Recurso Nao encontrado'}), 404)

if __name__ == "__main__":
	print "Servidor no ar!"
	app.run(host='0.0.0.0', debug=True)
