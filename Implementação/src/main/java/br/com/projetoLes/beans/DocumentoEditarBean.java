package br.com.projetoLes.beans;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_DOCUMENTO;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class DocumentoEditarBean {

	@Inject
	private FachadaRegras fachada;

	private Documento documento = new Documento();

	private boolean cpfError;

	public String redir(Documento doc) {
		documento = doc;
		return "/cliente/documento/edit-form";
	}

	@Transactional
	public String editarDocumento() {
		
		cpfError = false;
		Resultado res = new Resultado();
		res.setEntidade(documento);

		try {
			res = fachada.editar(res);
			System.out.println(res);
			System.out.println(res.getMensagem());
			if (res.getMensagem() != "") {
				cpfError = true;
				return "/cliente/documento/edit-form";
			}
			
			return "/cliente/perfil?faces-redirect=true";
		} catch (Exception e) {

			return "/cliente/documento/form?faces-redirect=true";

		}
	}

	public TIPO_DOCUMENTO[] getTipoDocumento() {
		return TIPO_DOCUMENTO.values();
	}

}
