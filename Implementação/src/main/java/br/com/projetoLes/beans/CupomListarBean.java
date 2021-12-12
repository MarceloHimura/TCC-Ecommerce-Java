package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class CupomListarBean {

	@Inject
	private FachadaRegras fachada;

	@Transactional
	public List<Cupom> listar() {
		Resultado res = new Resultado();
		res.setEntidade(new Cupom());
		return converteListaEntidade(fachada.listar(res).getEntidades());
	}

	@Transactional
	public List<Cupom> listarAtivos() {
		Resultado filtro = new Resultado();
		Resultado coluna = new Resultado();
		filtro.setMensagem("true");
		coluna.setMensagem("ativo");
		filtro.add(new Cupom());
		filtro.add(coluna);
		return converteListaEntidade(fachada.filtro(filtro).getEntidades());
	}

	@Transactional
	public List<Cupom> getCuponsDesconto() {
		Resultado res = new Resultado();
		res.setMensagem("Desconto");
		return converteListaEntidade(fachada.listarCupomTipo(res).getEntidades());
	}

	@Transactional
	public List<Cupom> getCuponsTroca() {
		Resultado res = new Resultado();
		res.setMensagem("Troca");
		return converteListaEntidade(fachada.listarCupomTipo(res).getEntidades());
	}

	private List<Cupom> converteListaEntidade(List<EntidadeDominio> lst) {
		List<Cupom> cupons = new ArrayList<Cupom>();
		for (EntidadeDominio e : lst) {
			Cupom c = (Cupom) e;
			cupons.add(c);
		}

		return cupons;
	}
}
