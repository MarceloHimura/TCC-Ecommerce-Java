package br.com.projetoLes.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PedidoTroca extends EntidadeDominio{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer pedId;
	private Integer prdId;
	private Integer quantidade;
	private String imagemPath;
	private String motivo;
	private boolean emAberto = false;
}
