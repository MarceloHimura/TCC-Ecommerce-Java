package br.com.projetoLes.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.wildfly.common.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cupom extends EntidadeDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String codigo;

	private boolean ativo;

	private double preco;

	@ManyToOne
	@Nullable
	private Cliente cliente;

	private String tipoCupom;

	@Override
	public String toString() {
		return getId().toString();
	}

	public String show() {
		return " Valor: R$" + preco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Cupom other = (Cupom) obj;

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}