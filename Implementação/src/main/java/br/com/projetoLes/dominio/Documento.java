package br.com.projetoLes.dominio;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Documento extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String codigo;

	@Temporal(TemporalType.DATE)
	private Calendar validade;

	@Enumerated(EnumType.STRING)
	private TIPO_DOCUMENTO tipoDocumento;

}
