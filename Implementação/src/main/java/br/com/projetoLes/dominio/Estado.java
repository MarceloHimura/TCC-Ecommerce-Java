package br.com.projetoLes.dominio;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Estado extends EntidadeDominio {

	@Enumerated(EnumType.STRING)
	private ESTADOS estado;

}
