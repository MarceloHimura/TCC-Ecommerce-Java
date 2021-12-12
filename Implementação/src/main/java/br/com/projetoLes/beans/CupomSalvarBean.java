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
public class CupomSalvarBean {

	@Inject
	private FachadaRegras fachada;

	private Cupom cupom = new Cupom();

	@Transactional
	public String salvar() {
		Resultado res = new Resultado();
		cupom.setTipoCupom("Desconto");
		cupom.setAtivo(true);
		res.setEntidade(cupom);
		fachada.salvar(res);
		return "/admin/cupom/lista?faces-redirect=true";
	}

}
