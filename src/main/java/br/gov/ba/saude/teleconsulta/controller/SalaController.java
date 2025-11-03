package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;
import br.gov.ba.saude.teleconsulta.service.SalaService;
import br.gov.ba.saude.teleconsulta.service.UnidadeSaudeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("salaController")
@Component
@ViewScoped
public class SalaController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final SalaService salaService;
    private final UnidadeSaudeService unidadeSaudeService;
    
    private List<Sala> salas;
    private Sala selecionada;
    private List<UnidadeSaude> unidades;
    private boolean edicao;

    public SalaController(SalaService salaService, UnidadeSaudeService unidadeSaudeService) {
        this.salaService = salaService;
        this.unidadeSaudeService = unidadeSaudeService;
    }

    @PostConstruct
    public void carregar() {
        this.salas = salaService.buscarTodas();
        this.unidades = unidadeSaudeService.buscarTodas();
        this.edicao = false;
        prepararNovo();
    }

    public void prepararNovo() {
    	if (this.selecionada == null || this.edicao == true) {
	        this.selecionada = new Sala();
	        this.selecionada.setUnidadeSaude(new UnidadeSaude());
	        this.edicao = false;
    	}
    }
    
    public void editar(Sala selecionada) {
    	this.selecionada = selecionada;
    	this.edicao = true;
    }
    
    public void salvar() {
        try {
            if (selecionada.getUnidadeSaude() == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "A sala deve estar associada a uma Unidade de Saúde."));
                return;
            }
            
            if (!edicao) {
            	salaService.criar(selecionada);
            	
            	FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala criada com sucesso."));
            } else {
            	salaService.editar(selecionada);
            	
            	FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala atualizada com sucesso."));
            }
            
            this.selecionada = null;
            carregar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar a sala."));
        }
    }

    public void excluir(Long id) {
        try {
        	salaService.remover(id);
            carregar();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sala excluída com sucesso."));
        } catch (Exception e) {
             FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir a sala."));
        }
    }

    public List<Sala> getSalas() {
    	return salas;
    }
    
    public Sala getSelecionada() {
    	return selecionada;
    }
    
    public List<UnidadeSaude> getUnidades() {
    	return unidades;
    }
    
    public boolean getEdicao() {
        return edicao;
	}
    
}
