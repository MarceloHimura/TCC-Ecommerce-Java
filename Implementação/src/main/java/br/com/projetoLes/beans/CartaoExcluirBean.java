package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class CartaoExcluirBean {

	@Inject
	private FachadaRegras fachada;

	@Transactional
	public String excluirCartao(CartaoCredito card, Cliente cliente) {

		Resultado res = new Resultado();

		for (CartaoCredito cards : cliente.getCartoes()) {
			if (cards.getId().equals(card.getId())) {
				cliente.getCartoes().remove(cards);
				break;
			}
		}

		res.setEntidade(cliente);
		fachada.editar(res);
		
		res.setEntidade(card);
		fachada.excluir(res);

		return "/cliente/perfil?faces-redirect=true";
	}

}
