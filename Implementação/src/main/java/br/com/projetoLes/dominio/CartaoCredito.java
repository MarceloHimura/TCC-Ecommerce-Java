package br.com.projetoLes.dominio;

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
public class CartaoCredito extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

	private String numeroCartao;

	@Enumerated(EnumType.STRING)
	private BANDEIRA bandeira;
	private boolean principal = false;
	private String nomeImpresso;
	private String validade;
	private String codigoSeguranca;

	public CartaoCredito(String numeroCartao, BANDEIRA bandeira, String nomeImpresso, String validade,
			String codigoSeguranca, Cliente cliente) {
		this.numeroCartao = numeroCartao;
		this.setBandeira(bandeira);
		this.nomeImpresso = nomeImpresso;
		this.validade = validade;
		this.codigoSeguranca = codigoSeguranca;
	}

	public CartaoCredito() {

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
		CartaoCredito other = (CartaoCredito) obj;
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
