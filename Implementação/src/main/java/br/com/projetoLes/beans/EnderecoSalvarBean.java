package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.ESTADOS;
import br.com.projetoLes.dominio.Endereco;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_ENDERECO;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class EnderecoSalvarBean {

	@Inject
	private FachadaRegras fachada;
	
	private Endereco end = new Endereco();

	@Transactional
	public String salvarEndereco(Cliente cliente, boolean checkout) {
		Resultado res = new Resultado();

		try {
			for (Endereco end : cliente.getEnderecos()) {
				if (end.isPrincipal() == true) {
					end.setPrincipal(false);
					break;
				}
			}
			cliente.getEnderecos().add(end);
			res.setEntidade(cliente);
			fachada.editar(res);

		if(checkout)
			return "/checkout/checkout?faces-redirect=true";
		else
			return "/cliente/perfil?faces-redirect=true";

		} catch (Exception e) {

			return "/cliente/endereco/form?faces-redirect=true";

		}
	}

	public TIPO_ENDERECO[] getTipoEndereco() {
		return TIPO_ENDERECO.values();
	}
	public ESTADOS[] getEstados() {
		return ESTADOS.values();
	}

}
