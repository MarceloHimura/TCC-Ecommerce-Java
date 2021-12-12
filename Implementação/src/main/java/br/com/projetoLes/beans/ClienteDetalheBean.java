package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class ClienteDetalheBean {

	@Inject
	private FachadaRegras fachada;
	
	private Cliente cliente;
	private Integer id;

	public void carregaDetalhe() {
		Resultado res  = new Resultado();
		res.setEntidade(new Cliente());
		res.setMensagem(id.toString());
		this.setCliente((Cliente) fachada.buscaById(res).getEntidade());
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
