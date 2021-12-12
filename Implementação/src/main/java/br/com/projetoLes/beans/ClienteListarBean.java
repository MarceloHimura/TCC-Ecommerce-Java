package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Model
public class ClienteListarBean {

	@Inject
	private FachadaRegras fachada;
	
	private List<Cliente> clientes = new ArrayList<Cliente>();
	private String busca = "";
	private Documento doc;


	public List<Cliente> getClientes() {
		Resultado res = new Resultado();
		res.setEntidade(new Cliente());
		if (clientes.size() != 0) {
			return clientes;
		}
		if (busca.equals("")) {
			this.clientes = converteLista(fachada.listar(res));
		}
		return clientes;
	}

	public void filtraClientes() {
		Cliente cli = new Cliente();
		Resultado res = new Resultado();
		Resultado coluna = new Resultado();
		res.setMensagem(busca);
		coluna.setMensagem("nome");
		res.add(cli);
		res.add(coluna);
		this.clientes = converteLista(fachada.filtro(res));
		if (this.clientes == null)
			this.clientes = converteLista(fachada.listar(new Cliente()));
	}
	
	private List<Cliente> converteLista(Resultado listar) {

		List<Cliente> lista = new ArrayList<Cliente>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((Cliente) e);
		}

		return lista;
	}

}
