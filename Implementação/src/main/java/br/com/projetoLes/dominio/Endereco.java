package br.com.projetoLes.dominio;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Endereco extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

	@Embedded
	private Cidade cidade = new Cidade();
	private String descricao;
	private String logradouro;
	private String numero;
	private String cep;
	private String complemento;
	private String bairro;
	private boolean principal = false;

	@Enumerated(EnumType.STRING)
	private TIPO_ENDERECO tipoEndereco;

	
	public String tipo() {
		return tipoEndereco.toString().replace("_", " ");
	}
	
	public Endereco() {

	}

	public  Endereco(Endereco end) {
		this.cidade = end.cidade;
		this.logradouro = end.logradouro;
		this.numero = end.numero;
		this.cep = end.cep;
		this.complemento = end.complemento;
		this.bairro = end.bairro;
		this.tipoEndereco = end.tipoEndereco;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Id == null) ? 0 : Id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (Id == null) {
			if (other.Id != null)
				return false;
		} else if (!Id.equals(other.Id))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return getId().toString();
	}

}
