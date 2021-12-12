package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class CupomEditarBean {

	@Inject
	private FachadaRegras fachada;

	private Cupom cupom = new Cupom();

	@Transactional
	public String editar() {
		Resultado res = new Resultado();
		cupom.setAtivo(true);
		res.setEntidade(cupom);

		fachada.editar(res);

		return "/admin/cupom/lista?faces-redirect=true";
	}

	public String redir(Cupom cupom) {
		this.cupom = cupom;
		return "/admin/cupom/edit-form";
	}

}
