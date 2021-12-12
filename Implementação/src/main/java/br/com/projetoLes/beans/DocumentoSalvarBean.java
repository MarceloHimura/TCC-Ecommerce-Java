package br.com.projetoLes.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.TIPO_DOCUMENTO;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Model
public class DocumentoSalvarBean {

	@Inject
	private FachadaRegras fachada;

	private Documento documento = new Documento();
	private String data = null;
	private boolean cpfError;

	@Transactional
	public String salvarDocumento(Cliente cliente) throws ParseException {
		Resultado res = new Resultado();
		Date d = new SimpleDateFormat("yyyy-MM-dd").parse(data);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		documento.setValidade(cal);

		res.setEntidade(documento);
		res = fachada.salvar(res);
		
		System.out.println("mensagem" + res.getMensagem());
		if (res.getMensagem() != null) {
			cpfError = true;
			return "/cliente/documento/form";
		}
		
		res = new Resultado();
		cliente.getDocumentos().add(documento);
		res.setEntidade(cliente);
		res = fachada.editar(res);
		
		return "/cliente/perfil?faces-redirect=true";

	}

	public TIPO_DOCUMENTO[] getDocumentos() {
		return TIPO_DOCUMENTO.values();
	}

}
