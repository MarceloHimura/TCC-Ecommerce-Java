package br.com.projetoLes.daos;

import java.util.ArrayList;
import java.util.List;

import br.com.projetoLes.dominio.Cliente;
import br.com.projetoLes.dominio.Cupom;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Pedido;
import br.com.projetoLes.dominio.PedidoTroca;
import br.com.projetoLes.dominio.Resultado;
import br.com.projetoLes.dominio.Senha;
import br.com.projetoLes.seguranca.CriptografaSenha;

public class Dao extends AbstractDao {

	@Override
	public Resultado salvar(EntidadeDominio ent) {
		Resultado resultado = (Resultado) ent;
		
		abrirConexao();

		manager.getTransaction().begin();
		manager.persist(resultado.getEntidade());
		manager.flush();
		manager.getTransaction().commit();
		
		fechaConexao();

		return resultado;

	}

	@Override
	public Resultado editar(EntidadeDominio ent) {
		Resultado resultado = (Resultado) ent;

		abrirConexao();
		
		manager.getTransaction().begin();
		manager.merge(resultado.getEntidade());
		manager.flush();
		manager.getTransaction().commit();

		fechaConexao();
		return resultado;
	}

	@Override
	public Resultado excluir(EntidadeDominio ent) {
		Resultado resultado = (Resultado) ent;

		abrirConexao();

		manager.getTransaction().begin();
		manager.remove(manager.contains(resultado.getEntidade()) ? resultado.getEntidade() : manager.merge(resultado.getEntidade()));
		manager.getTransaction().commit();
		
		fechaConexao();

		return resultado;
	}

	@Override
	public Resultado listar(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		String tabela = res.getEntidade().getClass().getName();
		String jpql = "select distinct(c) from " + tabela + " c";
		List<? extends EntidadeDominio> lista = new ArrayList<>();

		abrirConexao();
		manager.getTransaction().begin();

		lista = manager.createQuery(jpql, res.getEntidade().getClass()).getResultList();

		manager.getTransaction().commit();
		fechaConexao();

		for (EntidadeDominio e : lista) {
			res.add(e);
		}

		return res;
	}


	@Override
	public Resultado buscaById(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		String tabela = res.getEntidade().getClass().getName();
		Integer id = Integer.parseInt(res.getMensagem());
		String jpql = "select distinct(c) from " + tabela + " c where c.id = :id";
		abrirConexao();
		manager.getTransaction().begin();
		res.setEntidade(
				manager.createQuery(jpql, res.getEntidade().getClass()).setParameter("id", id).getSingleResult());
		manager.getTransaction().commit();
		fechaConexao();
		return res;
	}

	@Override
	public Resultado filtro(EntidadeDominio ent) {
		Resultado filtro = (Resultado) ent;
		Resultado coluna = (Resultado) filtro.getEntidades().get(1);
		String tabela = filtro.getEntidades().get(0).getClass().getName();
		@SuppressWarnings("unused")
		List<? extends EntidadeDominio> lista = new ArrayList<>();
		String jpql = "select c from " + tabela + " c where c." + coluna.getMensagem() + " LIKE '%"
				+ filtro.getMensagem() + "%' ";
		if (!tabela.contains("Pedido")) {
			jpql += "and c.ativo = 1";
		}
		abrirConexao();
		manager.getTransaction().begin();
		lista = manager.createQuery(jpql, filtro.getEntidades().get(0).getClass()).getResultList();
		manager.getTransaction().commit();
		fechaConexao();
		filtro = new Resultado();
		for (EntidadeDominio e : lista) {
			filtro.add(e);
		}

		return filtro;
	}

	public Resultado listarCupomTipo(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		String jpql = "select C from Cupom C where C.tipoCupom = '" + res.getMensagem() + "' and C.ativo = 1";
		List<Cupom> lista = new ArrayList<>();

		abrirConexao();

		manager.getTransaction().begin();
		lista = manager.createQuery(jpql, Cupom.class).getResultList();
		manager.getTransaction().commit();

		fechaConexao();

		for (EntidadeDominio e : lista) {
			res.add(e);
		}

		return res;
	}
	
	public Resultado listarPorCliente(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		String tabela = res.getEntidade().getClass().getName();
		Integer clienteId = Integer.parseInt(res.getMensagem());
		List<? extends EntidadeDominio> lista = new ArrayList<>();
		String jpql = "";
		if(tabela == Cupom.class.getName())
			 jpql = "select p from " + tabela + " p where p.cliente.id = :clienteId and p.ativo = 1";
		else
			 jpql = "select p from " + tabela + " p where p.cliente.id = :clienteId";
		abrirConexao();
		manager.getTransaction().begin();
		lista = manager.createQuery(jpql, res.getEntidade().getClass()).setParameter("clienteId", clienteId)
				.getResultList();
		manager.getTransaction().commit();
		fechaConexao();
		res = new Resultado();
		for (EntidadeDominio e : lista) {
			res.add(e);
		}
		return res;
	}
	
	public Resultado getTroca(EntidadeDominio ent) {
		Resultado res = (Resultado) ent;
		int id = Integer.parseInt(res.getMensagem());
		
		abrirConexao();
		String jpql = "select p from PedidoTroca p where p.pedId = :id and p.emAberto = true";

		manager.getTransaction().begin();
		List<PedidoTroca> pedidos = manager.createQuery(jpql, PedidoTroca.class).setParameter("id", id).getResultList();
		manager.getTransaction().commit();

		fechaConexao();
		res = new Resultado();
		
		for (PedidoTroca p : pedidos) {
			res.add(p);
		}
		return res;
	}

	
	public List<Pedido> grafico() {

		abrirConexao();
		String jpql = "select p from Pedido p order by p.dataAtualizacao asc";

		manager.getTransaction().begin();
		List<Pedido> lista = manager.createQuery(jpql, Pedido.class).getResultList();
		manager.getTransaction().commit();

		fechaConexao();
		return lista;
	}
	
	public Cliente login(Cliente cliente) {
		String jpql = "select distinct(c) from Cliente c  where c.email = :email and c.senha= :senha and c.ativo = true";

		CriptografaSenha crp = new CriptografaSenha();
		Senha senha = new Senha();
		senha.setSenha(crp.criptoSenha(cliente.getSenha().getSenha()));
		cliente.setSenha(senha);

		try {
			abrirConexao();
			cliente = manager.createQuery(jpql, Cliente.class).setParameter("email", cliente.getEmail())
					.setParameter("senha", cliente.getSenha()).getSingleResult();
			fechaConexao();
		} catch (Exception e) {
			return null;
		}

		return cliente;
	}
	
	public Integer validaExistencia(String validador) {
		String jpql = null;
		if (validador.contains("@")) {
			jpql = "select distinct(c) from Cliente c  where c.email = :validador";
		} else {
			jpql = "select distinct(d) from Documento d  where d.codigo = :validador";
		}
		abrirConexao();
		manager.getTransaction().begin();
		Integer tamanho = manager.createQuery(jpql).setParameter("validador", validador).getResultList().size();

		manager.getTransaction().commit();
		fechaConexao();
		return tamanho;
	}
}
