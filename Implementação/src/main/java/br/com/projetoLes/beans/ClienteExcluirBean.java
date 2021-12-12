package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class ClienteExcluirBean {

	@Inject
	private FachadaRegras fachada;
	
	@Transactional
	public void excluir(Cliente cli) {
		Resultado res = new Resultado();
		cli.setAtivo(false);
		res.setEntidade(cli);
		fachada.editar(res);
		
	}
	 
}
