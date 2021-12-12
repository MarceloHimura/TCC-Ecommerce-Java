package br.com.projetoLes.strategies;

import br.com.projetoLes.dominio.EntidadeDominio;

public interface IStrategy {

	String processar(EntidadeDominio ent);
}
