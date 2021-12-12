package br.com.projetoLes.daos;

import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public interface IDao {

	Resultado salvar(EntidadeDominio ent);
	Resultado editar(EntidadeDominio ent);
	Resultado excluir(EntidadeDominio ent);
	Resultado listar(EntidadeDominio ent);
	Resultado buscaById(EntidadeDominio ent);
	Resultado filtro(EntidadeDominio ent);
}
