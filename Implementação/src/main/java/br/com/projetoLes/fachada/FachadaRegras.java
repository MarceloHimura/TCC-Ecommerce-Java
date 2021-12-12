package br.com.projetoLes.fachada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import br.com.projetoLes.daos.Dao;
import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.strategies.IStrategy;
import br.com.projetoLes.strategies.ValidaCPF;
import br.com.projetoLes.strategies.ValidaCartao;
import br.com.projetoLes.strategies.ValidaExistenciaCPF;
import br.com.projetoLes.strategies.ValidaExistenciaClientePorCPF;
import br.com.projetoLes.strategies.ValidaExistenciaPorEmail;
import br.com.projetoLes.strategies.ValidaValoresPagamento;
import br.com.projetoLes.strategies.ValorCartoes;
import br.com.projetoLes.strategies.ValorCupom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Model
public class FachadaRegras implements IFachada {

	@Inject
	private Dao dao;

	private Map<String, Map<String, List<IStrategy>>> regrasNegocio;

	private StringBuilder sb = new StringBuilder();
	private Resultado resultado;

	public FachadaRegras() {
		regrasNegocio = new HashMap<String, Map<String, List<IStrategy>>>();

		// Cliente
		List<IStrategy> rnsClienteSalvar = new ArrayList<IStrategy>();
		rnsClienteSalvar.add(new ValidaCPF());
		rnsClienteSalvar.add(new ValidaExistenciaClientePorCPF());
		rnsClienteSalvar.add(new ValidaExistenciaPorEmail());
		rnsClienteSalvar.add(new ValidaCartao());
		
		List<IStrategy> rnsClienteEditar = new ArrayList<IStrategy>();
		rnsClienteEditar.add(new ValidaCartao());

		Map<String, List<IStrategy>> mapaCliente = new HashMap<String, List<IStrategy>>();

		mapaCliente.put("Salvar", rnsClienteSalvar);
		mapaCliente.put("Editar", rnsClienteEditar);

		regrasNegocio.put(Cliente.class.getName(), mapaCliente);

		// Documento
		List<IStrategy> rnsDocumentoSalvar = new ArrayList<IStrategy>();
		rnsDocumentoSalvar.add(new ValidaCPF());
		rnsDocumentoSalvar.add(new ValidaExistenciaCPF());

		List<IStrategy> rnsDocumentoEditar = new ArrayList<IStrategy>();
		rnsDocumentoEditar.add(new ValidaCPF());
		rnsDocumentoSalvar.add(new ValidaExistenciaCPF());

		Map<String, List<IStrategy>> mapaDocumento = new HashMap<String, List<IStrategy>>();

		mapaDocumento.put("Salvar", rnsDocumentoSalvar);
		mapaDocumento.put("Editar", rnsDocumentoEditar);

		regrasNegocio.put(Documento.class.getName(), mapaDocumento);

		// Pedido
		List<IStrategy> rnsPedidoSalvar = new ArrayList<IStrategy>();
		rnsPedidoSalvar.add(new ValorCartoes());
		rnsPedidoSalvar.add(new ValorCupom());
		rnsPedidoSalvar.add(new ValidaValoresPagamento());

//		List<IStrategy> rnsPedidoEditar = new ArrayList<IStrategy>();
//		rnsPedidoEditar.add(new ValidaCPF());

		Map<String, List<IStrategy>> mapaPedido = new HashMap<String, List<IStrategy>>();

		mapaPedido.put("Salvar", rnsPedidoSalvar);
//		mapaPedido.put("Editar", rnsPedidoEditar);

		regrasNegocio.put(Pedido.class.getName(), mapaPedido);

	}

	@Override
	public Resultado salvar(EntidadeDominio entidade) {
		System.out.println("- Fachada Salvar");
		resultado = new Resultado();
		Resultado res = new Resultado();
		res = (Resultado) entidade;
		sb.setLength(0);
		String nmClasse = res.getEntidade().getClass().getName();
		try {
			Map<String, List<IStrategy>> mapaEntidade = regrasNegocio.get(nmClasse);
			List<IStrategy> rnsEntidade = mapaEntidade.get("Salvar");
	
			executarRegras(entidade, rnsEntidade);
			
			System.out.println("tamanho"+sb.length());
			
			if (sb.length() == 0) {
				dao.salvar(entidade);
				resultado.add(entidade);
			} else {
				resultado.add(entidade);
				resultado.setMensagem(sb.toString());
			}
			return resultado;
		}
		catch (Exception e) {
			return dao.salvar(entidade);
		}
	}

	@Override
	public Resultado editar(EntidadeDominio entidade) {
		System.out.println("- Fachada Editar");
		resultado = new Resultado();
		Resultado res = new Resultado();
		res = (Resultado) entidade;
		sb.setLength(0);
		String nmClasse = res.getEntidade().getClass().getName();
		System.out.println(nmClasse);
		try {
			Map<String, List<IStrategy>> mapaEntidade = regrasNegocio.get(nmClasse);
			List<IStrategy> rnsEntidade = mapaEntidade.get("Editar");
	
			executarRegras(entidade, rnsEntidade);
	
			System.out.println("tamanho"+sb.length());
			
			if (sb.length() == 0) {
				System.out.println("if");
				dao.editar(entidade);
				resultado.add(entidade);
				resultado.setMensagem("");
			} else {
				System.out.println("else");
				resultado.add(entidade);
				resultado.setMensagem(sb.toString());
			}
			System.out.println("TRY");
	
			return resultado;
		}
		catch (Exception e) {
			return dao.editar(entidade);
		}
	}

	private void executarRegras(EntidadeDominio entidade, List<IStrategy> rnsEntidade) {
		for (IStrategy rn : rnsEntidade) {
			String msg = rn.processar(entidade);
			if (msg != null) {
				sb.append(msg + "\n");
			}
		}
	}

	@Override
	public Resultado excluir(EntidadeDominio ent) {
		return dao.excluir(ent);
	}

	@Override
	public Resultado listar(EntidadeDominio ent) {
		return dao.listar(ent);
	}

	@Override
	public Resultado buscaById(EntidadeDominio ent) {
		return dao.buscaById(ent);
	}

	@Override
	public Resultado filtro(EntidadeDominio ent) {
		return dao.filtro(ent);
	}

	@Override
	public Resultado listarCupomTipo(EntidadeDominio ent) {
		return dao.listarCupomTipo(ent);
	}

	@Override
	public Resultado listarPorCliente(EntidadeDominio ent) {
		return dao.listarPorCliente(ent);
	}

	public Resultado getTroca(EntidadeDominio ent) {
		return dao.getTroca(ent);
	}

	public Cliente login(Cliente cli) {
		return dao.login(cli);
	}
	
	public List<Pedido> grafico() {
		return dao.grafico();
	}

}
