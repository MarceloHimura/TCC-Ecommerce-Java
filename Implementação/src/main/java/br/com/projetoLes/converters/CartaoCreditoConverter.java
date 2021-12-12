package br.com.projetoLes.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.projetoLes.dominio.CartaoCredito;

@FacesConverter("CartaoCreditoConverter")
public class CartaoCreditoConverter implements Converter {

	@Override
	public CartaoCredito  getAsObject(FacesContext context, UIComponent component, String cartao) {

		if (cartao == null || cartao.trim().isEmpty()) {
			return null;
		}

		CartaoCredito card = new CartaoCredito();
		System.out.println("Cartao:"+cartao);
//		card.setId(Integer.valueOf(cartao));

		return card;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object cartao) {

		if (cartao == null) {
			return null;
		}

		CartaoCredito card = (CartaoCredito) cartao;

		return card.toString();
	}

}
