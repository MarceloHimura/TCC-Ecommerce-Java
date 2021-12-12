package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class LivroDetalheBean {

	@Inject
	private FachadaRegras fachada;
	
	private Livro livro;
	private Integer id;

	public void carregaDetalhe() {
		Resultado res  = new Resultado();
		res.setEntidade(new Livro());
		res.setMensagem(id.toString());
		this.setLivro((Livro) fachada.buscaById(res).getEntidade());
	}
}
