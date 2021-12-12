package br.com.projetoLes.fachada;

import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public interface IFachada {

	Resultado salvar(EntidadeDominio ent);
	Resultado editar(EntidadeDominio ent);
	Resultado excluir(EntidadeDominio ent);
	Resultado listar(EntidadeDominio ent);
	Resultado listarCupomTipo(EntidadeDominio ent);
	Resultado listarPorCliente(EntidadeDominio ent);
	Resultado buscaById(EntidadeDominio ent);
	Resultado filtro(EntidadeDominio ent);
}
