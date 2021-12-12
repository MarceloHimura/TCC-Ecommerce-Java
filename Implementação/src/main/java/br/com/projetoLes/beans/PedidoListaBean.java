package br.com.projetoLes.beans;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.fachada.FachadaRegras;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class PedidoListaBean {

	@Inject
	private FachadaRegras fachada;

	private String busca = "";
	private String busca_tratada = "";
	private String filtro = "statusPedido";
	private int totalPedidos = 0;

	public void paginacaoAdmin() {
		totalPedidos = todosPedidos().size();
	}

	@Transactional
	public List<Pedido> pedidos(Integer cliId) {
		Resultado res = new Resultado();
		res.setEntidade(new Pedido());
		res.setMensagem(cliId.toString());
		return converteLista(fachada.listarPorCliente(res));
	}

	@Transactional
	public List<Pedido> todosPedidos() {
		Resultado res = new Resultado();
		if (getBusca().equals("")) {
			res.setEntidade(new Pedido());
			return converteLista(fachada.listar(res));
		} else {
			if (filtro.equals("statusPedido")) {
				Resultado coluna = new Resultado();
				busca_tratada = busca.replace(" ", "_").toLowerCase();
				res.setMensagem(busca_tratada);
				coluna.setMensagem(filtro);
				res.add(new Pedido());
				res.add(coluna);
				return converteLista(fachada.filtro(res));
			} else {
				res.setMensagem(busca);
				res.setEntidade(new Pedido());
				List<Pedido> lista = new ArrayList<Pedido>();
				try {
					lista.add((Pedido) fachada.buscaById(res).getEntidade());
					return lista;
				} catch (Exception e) {
					return converteLista(fachada.listar(res));
				}
			}
		}
	}

	private List<Pedido> converteLista(Resultado listar) {

		List<Pedido> lista = new ArrayList<Pedido>();

		for (EntidadeDominio e : listar.getEntidades()) {
			lista.add((Pedido) e);
		}

		return lista;
	}

}
