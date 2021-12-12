package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.BANDEIRA;
import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class CartaoSalvarBean {

	@Inject
	private FachadaRegras fachada;

	private static String erro = "";

	private CartaoCredito card = new CartaoCredito();

	@Transactional
	public String salvarCartao(Cliente cliente, boolean checkout) {
		Resultado res = new Resultado();
		List<CartaoCredito> cartoes = new ArrayList<CartaoCredito>();

		try {
			for (CartaoCredito cc : cliente.getCartoes()) {
				if (cc.isPrincipal() == true) {
					cc.setPrincipal(false);
					break;
				}
			}
			res.setEntidade(new CartaoCredito());
			cartoes = converteLista(fachada.listar(res));
			for (CartaoCredito cc : cartoes) {

				if (cc.getNumeroCartao().equals(card.getNumeroCartao())) {
//					System.out.println("Kang Seulgi");
					erro = "Cartão já cadastrado";
					if (checkout)
						return "/checkout/cartao/form?faces-redirect=true";
					else
						return "/cliente/cartao/form?faces-redirect=true";
				}
			}

			cliente.getCartoes().add(card);
			res.setEntidade(cliente);
			res.add(new CartaoCredito());
			erro = fachada.editar(res).getMensagem();
			if (erro != null && !erro.isEmpty()) {
				if (checkout)
					return "/checkout/cartao/form?faces-redirect=true";
				else
					return "/cliente/cartao/form?faces-redirect=true";
			}

			if (checkout)
				return "/checkout/checkout?faces-redirect=true";
			else
				return "/cliente/perfil?faces-redirect=true";

		} catch (Exception e) {

			e.printStackTrace();

			return "/cliente/cartao/form?faces-redirect=true";
		}
	}

	private List<CartaoCredito> converteLista(Resultado listar) {

		List<CartaoCredito> lista = new ArrayList<CartaoCredito>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((CartaoCredito) e);
		}

		return lista;
	}

	public BANDEIRA[] getBandeiras() {
		return BANDEIRA.values();
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		CartaoSalvarBean.erro = erro;
	}
}
