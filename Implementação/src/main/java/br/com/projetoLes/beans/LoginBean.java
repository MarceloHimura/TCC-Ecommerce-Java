package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.TIPO_CLIENTE;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class LoginBean {

	@Inject
	private FachadaRegras fachada;

	@Inject
	private HttpServletRequest req;

	private Cliente cliente = new Cliente();
	boolean checkout = false;

	public String loginCheckout() {

		checkout = true;
		return login();

	}

	@Transactional
	public String login() {

		HttpSession session = req.getSession(false);
		cliente = fachada.login(cliente);

		if (cliente != null) {
			session.setAttribute("cliente", cliente);
			session.setAttribute("cli_nome", cliente.getNome());
			session.setAttribute("cli_id", cliente.getId());
			session.setAttribute("funcao", cliente.getTipoCliente().toString());
			session.setAttribute("status", true);
			if (checkout)
				return "/checkout/checkout?faces-redirect=true";
			if (cliente.getTipoCliente().equals(TIPO_CLIENTE.Admin))
				return "/admin/admin?faces-redirect=true";
			return "/cliente/perfil?faces-redirect=true";
		} else {
			return "/cliente/login?faces-redirect=true";
		}

	}

	public String logout() {
		req.getSession().invalidate();
		return "/index?faces-redirect=true";
	}

	public boolean listaVazia() {

		boolean as;

		if (cliente.getCartoes().isEmpty()) {
			as = true;
		} else {
			as = false;
		}

		return as;
	}

}
