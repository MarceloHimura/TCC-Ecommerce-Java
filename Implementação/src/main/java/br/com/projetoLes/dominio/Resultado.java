package br.com.projetoLes.dominio;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Resultado extends EntidadeDominio{

	private String mensagem;
	private EntidadeDominio entidade;
	private List<EntidadeDominio> entidades;

	public Resultado() {
		entidades = new ArrayList<EntidadeDominio>();
	}

	public void add(EntidadeDominio ent) {
		entidades.add(ent);
	}
}
