package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Endereco;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class EnderecoExcluirBean {

	@Inject
	private FachadaRegras fachada;

	@Transactional
	public String excluirEndereco(Endereco end, Cliente cliente) {

		Resultado res = new Resultado();

		for (Endereco ends : cliente.getEnderecos()) {
			if (ends.getId().equals(end.getId())) {
				cliente.getEnderecos().remove(ends);
				break;
			}

		}
		res.setEntidade(cliente);
		fachada.editar(res);

		res.setEntidade(end);
		fachada.excluir(res);

		return "/cliente/perfil?faces-redirect=true";

	}

}
