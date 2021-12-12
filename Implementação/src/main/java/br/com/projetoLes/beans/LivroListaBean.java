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
public class LivroListaBean {

	@Inject
	private FachadaRegras fachada;
	
	public List<Livro> getlivrosAdmin() {
		Resultado res = new Resultado();
		res.setEntidade(new Livro());
		return converteLista(fachada.listar(res));
	}

	private List<Livro> converteLista(Resultado listar) {

		List<Livro> lista = new ArrayList<Livro>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((Livro) e);
		}

		return lista;
	}

	public CATEGORIA[] getRaridade() {
		return CATEGORIA.values();
	}
}
