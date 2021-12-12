package br.com.projetoLes.dominio;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Cidade extends EntidadeDominio {

	private String cidade;

	@Embedded
	private Estado estado = new Estado();

}
