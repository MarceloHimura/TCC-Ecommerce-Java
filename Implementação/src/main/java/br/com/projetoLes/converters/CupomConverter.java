package br.com.projetoLes.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.projetoLes.dominio.Cupom;

@FacesConverter("CupomConverter")
public class CupomConverter implements Converter {

	@Override
	public Cupom getAsObject(FacesContext context, UIComponent component, String cupom) {

		if (cupom == null || cupom.trim().isEmpty()) {
			return null;
		}

		System.out.println("cp convert" + cupom);
		Cupom cpm = new Cupom();

		cpm.setId(Integer.valueOf(cupom));

		return cpm;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object cupom) {

		if (cupom == null) {
			return null;
		}
		Cupom end = (Cupom) cupom;

		return end.toString();
	}

}
