package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;
import br.gov.ba.saude.teleconsulta.service.UnidadeSaudeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("unidadeController")
@Component
@ViewScoped
public class UnidadeSaudeController implements Serializable {
    private static final long serialVersionUID = 1L;

	private final UnidadeSaudeService unidadeSaudeService;
    
    private List<UnidadeSaude> unidades;
    private UnidadeSaude selecionada;
    private boolean edicao;

    // Injeção de dependência do Spring
    public UnidadeSaudeController(UnidadeSaudeService unidadeSaudeService) {
        this.unidadeSaudeService = unidadeSaudeService;
    }

    /**
     * Método executado após a criação do bean para carregar os dados iniciais.
     */
    @PostConstruct
    public void carregar() {
        this.unidades = unidadeSaudeService.buscarTodas();
        this.edicao = false;
    }

    /**
     * Prepara um novo objeto para cadastro.
     */
    public void prepararNovo() {
    	if (this.selecionada == null || this.edicao == true) {
    		this.selecionada = new UnidadeSaude();
    		this.edicao = false;
    	}
    }
    
    public void editar(UnidadeSaude selecionado) {
		this.selecionada = selecionado;
		this.edicao = true;
	}

    /**
     * Salva (cria ou atualiza) a unidade de saúde.
     */
    public void salvar() {
        try {
        	if (!edicao) {
        		unidadeSaudeService.criar(selecionada);
        		
        		FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Unidade criada com sucesso."));
        	} else {
        		unidadeSaudeService.editar(selecionada);
        		
        		FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Unidade atualizada com sucesso."));
        	}
            
        	this.selecionada = null;
            carregar();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar a unidade."));
        }
    }

    /**
     * Exclui uma unidade de saúde.
     */
    public void excluir(Long id) {
        try {
        	unidadeSaudeService.remover(id);
            carregar(); 
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Unidade excluída com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir a unidade."));
        }
    }

    public List<UnidadeSaude> getUnidades() {
        return unidades;
    }

    public UnidadeSaude getSelecionada() {
        return selecionada;
    }

    public void setSelecionada(UnidadeSaude selecionada) {
        this.selecionada = selecionada;
    }
    
    public boolean getEdicao() {
        return edicao;
	}
    
}