#!/usr/bin/python
from flask import Flask, jsonify
from flask import abort
from flask import make_response
from flask import request
from flask import url_for
from flask.ext.httpauth import HTTPBasicAuth

auth = HTTPBasicAuth()
app = Flask(__name__)

docentes = [
	{
	'id' : 1,
	'docente' : 'Cleber Jorge Amaral',
	'disciplinas' : ['PJI29006','PRG29002'],
	},
	{
	'id' : 2,
	'docente' : 'Arliones Hoeller',
	'disciplinas' : ['PJI29006'], 
	}
]

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/docentes
#
@app.route('/docentes', methods=['GET'])
def obtem_docentes():
	return jsonify({'docentes': docentes})

# Como invocar na linha de comando
#
# curl -i http://localhost:5000/docentes/1
#
@app.route('/docentes/<int:idDocente>', methods=['GET'])
def detalhe_docente(idDocente):
	resultado = [resultado for resultado in docentes if resultado['id'] == idDocente]
	if len(resultado) == 0:
		abort(404)
	return jsonify({'docente': resultado[0]})

# Como invocar na linha de comando
#
# curl -i -X DELETE http://localhost:5000/docentes/2
#
@app.route('/docentes/<int:idDocente>', methods=['DELETE'])
def excluir_docente(idLivro):
	resultado = [resultado for resultado in docentes if resultado['id'] == idDocente]
	if len(resultado) == 0:
		abort(404)
	docentes.remove(resultado[0])
	return jsonify({'resultado': True})

# Como invocar na linha de comando
#
# curl -i -H "Content-Type: application/json" -X POST -d '{"nome":"nome do docente","disciplinas":"?lista?"}' http://localhost:5000/docentes
#
@app.route('/docentes', methods=['POST'])
def criar_docente():
	if not request.json or not 'docente' in request.json:
		abort(400)
	docentes = {
		'id': livros[-1]['id'] + 1,
		'docente': request.json['docente'],
		'disciplinas': request.json.get('disciplinas', "")
	}
	docentes.append(livro)
	return jsonify({'docente': docente}), 201

# Como invocar na linha de comando
#
# curl -i -H "Content-Type: application/json" -X PUT -d '{"docente":"Novo Docente"}' http://localhost:5000/docentes/2
#
@app.route('/docentes/<int:idDocente>', methods=['PUT'])
def atualizar_docente(idDocente):
	resultado = [resultado for resultado in docentes if resultado['id'] == idDocente]
	if len(resultado) == 0:
		abort(404)
	if not request.json:
		abort(400)
	if 'docente' in request.json and type(request.json['docente']) != unicode:
		abort(400)
	if 'disciplinas' in request.json and type(request.json['disciplinas']) 	is not unicode:
		abort(400)
	resultado[0]['docente'] = request.json.get('docente', resultado[0]['docente'])
	resultado[0]['disciplinas'] = request.json.get('disciplinas', resultado[0]['disciplinas'])
	return jsonify({'docente': resultado[0]})

#### Autenticacao simples ####
# Como invocar na linha de comando
#
# curl -u aluno:senha123 -i http://localhost:5000/livrosautenticado
#
@app.route('/docentesautenticado', methods=['GET'])
@auth.login_required
def obtem_docentes_autenticado():
	return jsonify({'docentes': docentes})

# Autenticacao simples
@auth.get_password
def get_password(username):
	if username == 'aluno':
		return 'senha123'
	return None

@auth.error_handler
def unauthorized():
	return make_response(jsonify({'erro': 'Acesso Negado'}), 403)

##############################
# Para apresentar erro 404 HTTP se tentar acessar um recurso que nao existe
@app.errorhandler(404)
def not_found(error):
	return make_response(jsonify({'erro': 'Recurso Nao encontrado'}), 404)

if __name__ == "__main__":
	print "Servidor no ar!"
	app.run(host='0.0.0.0', debug=True)
