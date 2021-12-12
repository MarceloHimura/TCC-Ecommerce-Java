package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class CupomExcluirBean {

	@Inject
	private FachadaRegras fachada;
	
	@Transactional
	public String excluir(Cupom cup) {
		Resultado res = new Resultado();
		cup.setAtivo(false);
		res.setEntidade(cup);
		fachada.editar(res);

		return "/admin/cupom/lista?faces-redirect=true";
	}

}
