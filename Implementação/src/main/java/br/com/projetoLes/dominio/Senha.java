package br.com.projetoLes.dominio;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Senha {

	private String senha;

	@Transient
	private String confirmaSenha;

}
