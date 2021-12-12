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
public class EnderecoEditarBean {

	@Inject
	private FachadaRegras fachada;
	
	private Endereco endereco = new Endereco();

	public String redir(Endereco end) {
		endereco = end;
		return "/cliente/endereco/edit-form";
	}

	@Transactional
	public String editarEndereco(Cliente cliente) {
		Resultado res = new Resultado();
		
		if(endereco.isPrincipal()) {
			for (Endereco end : cliente.getEnderecos()) {
				if (end.isPrincipal() == true) {
					end.setPrincipal(false);
					System.out.println("Camila Branca");
					res.setEntidade(cliente);
					fachada.editar(res);
					break;
				}
			}
		}
		
		res.setEntidade(endereco);

		try {
			fachada.editar(res);
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

