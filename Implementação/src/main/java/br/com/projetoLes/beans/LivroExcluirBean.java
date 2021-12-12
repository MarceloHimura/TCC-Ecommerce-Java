package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Estoque;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class LivroExcluirBean {

	@Inject
	private FachadaRegras fachada;
	
	@Transactional
	public String delete(Livro livro, Estoque estoque) {
		Resultado res = new Resultado();
		livro.setEstoque(estoque);
		livro.setAtivo(false);
		res.setEntidade(livro);
		fachada.editar(res);
		return "/admin/livros/lista?faces-redirect=true";
	}

}
