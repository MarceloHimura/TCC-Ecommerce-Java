package br.com.projetoLes.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.CartaoCredito;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.Endereco;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_CLIENTE;
import br.com.projetoLes.dominio.TIPO_ENDERECO;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
@SessionScoped
public class PerfilBean implements Serializable {

	@Inject
	private FachadaRegras fachada;

	private static final long serialVersionUID = 7819372664905570613L;
	private Cliente cliente = new Cliente();
	private CartaoCredito card = new CartaoCredito();
	private Endereco end = new Endereco();
	private Documento doc = new Documento();
	private boolean logado = false;

	public List<Endereco> entrega() {
		return cliente.getEnderecos().stream().filter(end -> end.getTipoEndereco().equals(TIPO_ENDERECO.ENTREGA)
				|| end.getTipoEndereco().equals(TIPO_ENDERECO.ENTREGA_E_COBRANCA)).collect(Collectors.toList());
	}

	public List<CartaoCredito> cartoes() {
		List<CartaoCredito> cartoes = new ArrayList<CartaoCredito>(cliente.getCartoes());
		for (CartaoCredito cc : cartoes) {
			if (cc.isPrincipal() == true) {
				cartoes.remove(cc);
				cartoes.add(cc);
				break;
			}
		}
		Collections.reverse(cartoes);
		return cartoes;
	}

	public String carregar(Integer id) {
		Resultado res = new Resultado();
		try {
			res.setMensagem(id.toString());
			res.setEntidade(cliente);
			cliente = (Cliente) fachada.buscaById(res).getEntidade();
			logado = true;
			return null;
		} catch (Exception e) {
			return "index/?faces-redirect=true";
		}
	}

	public int getId() {
		return cliente.getId();
	}

	public Boolean isAdmin() {
		return (cliente.getTipoCliente() == TIPO_CLIENTE.Admin);
	}

	@Transactional
	public String editarCliente() {
		try {

			this.cliente = (Cliente) fachada.editar(cliente).getEntidade();

			return "/cliente/perfil?faces-redirect=true";

		} catch (Exception e) {

			return "/cliente/edit-form?faces-redirect=true";

		}
	}

	public String redirCartao(CartaoCredito card) {

		this.card = card;
		return "/cliente/cartao/edit-form?faces-redirect=true";

	}

	public String redirDocumento(Documento doc) {

		this.doc = doc;

		return "/cliente/documento/edit-form?faces-redirect=true";

	}

	public String redirEndereco(Endereco end) {

		this.end = end;
		return "/cliente/endereco/edit-form?faces-redirect=true";

	}

	@Transactional
	public List<Cupom> getCupomByCliente() {
		Resultado res = new Resultado();
		res.setEntidade(new Cupom());
		res.setMensagem(cliente.getId().toString());
		return converteListaEntidade(fachada.listarPorCliente(res).getEntidades());
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
