package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.projetoLes.dominio.CATEGORIA;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Livro;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class HomeBean {

	private String busca = "";

	private List<Livro> livros = new ArrayList<>();
	
	@Inject
	private FachadaRegras fachada;

	public List<Livro> getLivros() {
		Resultado res = new Resultado();
		res.setEntidade(new Livro());
		if (busca.equals(""))
			this.livros = converteLista(fachada.listar(res));
		return livros;
	}

	public void filtraLivros() {
		Resultado res = new Resultado();
		Resultado col = new Resultado();
		res.setMensagem(busca);
		col.setMensagem("nome");
		res.add(new Livro());
		res.add(col);
		this.livros = converteLista(fachada.filtro(res));
		if (this.livros == null)
			this.livros = converteLista(fachada.listar(new Livro()));
	}

	private List<Livro> converteLista(Resultado listar) {

		List<Livro> lista = new ArrayList<Livro>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((Livro) e);
		}

		return lista;
	}

	public String getBusca() {
		return busca;
	}

	public void setBusca(String busca) {
		this.busca = busca;
	}

	public CATEGORIA[] getRaridade() {
		return CATEGORIA.values();
	}

}
