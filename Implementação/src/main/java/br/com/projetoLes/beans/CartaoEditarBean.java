package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.BANDEIRA;
import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class CartaoEditarBean {

	@Inject
	private FachadaRegras fachada;
	
	private CartaoCredito card = new CartaoCredito();

	public String redir(CartaoCredito card) {
		this.card = card;
		return "/cliente/cartao/edit-form";
	}

	@Transactional
	public String editarCartao(Cliente cliente) {
		Resultado res = new Resultado();
		if(card.isPrincipal()) {
			for (CartaoCredito cc : cliente.getCartoes()) {
				if (cc.isPrincipal() == true) {
					cc.setPrincipal(false);
					res.setEntidade(cliente);
					fachada.editar(res);
					break;
				}
			}
		}
		
		res.setEntidade(card);
		try {
			fachada.editar(res);
			return "/cliente/perfil?faces-redirect=true";
		} catch (Exception e) {
			return "/cliente/cartao/form?faces-redirect=true";
		}
	}

	public BANDEIRA[] getBandeiras() {
		return BANDEIRA.values();
	}

}
