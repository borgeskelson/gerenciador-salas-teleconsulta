package br.gov.ba.saude.teleconsulta.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.repository.SalaRepository;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Named;

@Component
@Named("salaConverter")
public class SalaConverter implements Converter<Sala> {

    @Autowired
    private SalaRepository salaRepository;
    
    /**
     * Converte o Objeto Sala em uma String para ser enviado ao front-end.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Sala value) {
        if (value == null || value.getId() == null) {
            return null;
        }
        return String.valueOf(value.getId());
    }
    
    /**
     * Converte a String vinda do front-end de volta em um Objeto Sala completo.
     */
    @Override
    public Sala getAsObject(FacesContext context, UIComponent component, String value) {
    	if (value == null || value.isEmpty() || "null".equals(value)) {
            return null;
        }

        try {
            Long id = Long.valueOf(value);
            return salaRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}