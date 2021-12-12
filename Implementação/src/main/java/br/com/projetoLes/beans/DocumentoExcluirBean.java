package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;

@Model
public class DocumentoExcluirBean {

	@Inject
	private FachadaRegras fachada;

	@Transactional
	public String excluirDocumento(Documento doc, Cliente cliente) {

		Resultado res = new Resultado();

		for (Documento docs : cliente.getDocumentos()) {
			if (docs.getId().equals(doc.getId())) {
				cliente.getDocumentos().remove(docs);
				break;
			}

		}

		res.setEntidade(cliente);
		fachada.editar(res);

		res.setEntidade(doc);
		fachada.excluir(res);

		return "/cliente/perfil?faces-redirect=true";

	}

}
