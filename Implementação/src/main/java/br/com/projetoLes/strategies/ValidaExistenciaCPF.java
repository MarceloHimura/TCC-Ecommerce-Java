package br.com.projetoLes.strategies;

import br.com.projetoLes.daos.Dao;
import br.com.projetoLes.dominio.Documento;
import br.com.projetoLes.dominio.EntidadeDominio;
import br.com.projetoLes.dominio.Resultado;

public class ValidaExistenciaCPF implements IStrategy {

	@Override
	public String processar(EntidadeDominio ent) {
		Dao dao = new Dao();
		Resultado res = (Resultado) ent;
		Documento doc = (Documento) res.getEntidade();

		System.out.println("valida existencia de cpf doc");
		if (dao.validaExistencia(doc.getCodigo()) > 0)
			return "Cpf já Cadastrado  ";

		return null;
	}

}
