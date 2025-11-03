package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.Paciente;
import br.gov.ba.saude.teleconsulta.service.PacienteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("pacienteController")
@Component
@ViewScoped
public class PacienteController implements Serializable {
	private static final long serialVersionUID = 1L;

	private final PacienteService pacienteService;
	
	private List<Paciente> pacientes;
	private Paciente selecionado;
	private boolean edicao;
	
	// Campo de busca
	private String termoBusca;

	// Injeção de dependência do Spring
	public PacienteController(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	/**
	 * Método executado após a criação do bean para carregar os dados iniciais.
	 */
	@PostConstruct
	public void carregar() {
		this.pacientes = pacienteService.buscarTodos();
		this.edicao = false;
	}

	/**
	 * Prepara um novo objeto Paciente para cadastro.
	 */
	public void prepararNovo() {
		if (this.selecionado == null || this.edicao == true) {
			this.selecionado = new Paciente();
			this.edicao = false;
		}
	}
	
	public void editar(Paciente selecionado) {
		this.selecionado = selecionado;
		this.edicao = true;
	}

	/**
	 * Salva (cria ou atualiza) o paciente.
	 */
	public void salvar() {
		try {
			if (!edicao) {
				pacienteService.criar(selecionado);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Paciente criado com sucesso."));
			} else {
				pacienteService.editar(selecionado);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Paciente atualizado com sucesso."));
			}
			
			this.selecionado = null;
			carregar(); 
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar o paciente."));
		}
	}

	/**
	 * Exclui um paciente.
	 */
	public void excluir(Long id) {
		try {
			pacienteService.remover(id);
			carregar(); 
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Paciente excluído com sucesso."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir o paciente."));
		}
	}
	
	/**
	 * Realiza a busca na tabela por múltiplos campos.
	 */
	public void buscar() {
		try {
			if (termoBusca == null || termoBusca.trim().isEmpty()) {
				carregar(); 
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Busca", "Lista completa carregada."));
			} else {
				this.pacientes = pacienteService.buscarPorTermo(termoBusca);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Busca", this.pacientes.size() + " pacientes encontrados."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar a busca."));
			e.printStackTrace();
		}
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public Paciente getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Paciente selecionado) {
		this.selecionado = selecionado;
	}
	
	public boolean getEdicao() {
		return edicao;
	}
	
	public String getTermoBusca() {
		return termoBusca;
	}

	public void setTermoBusca(String termoBusca) {
		this.termoBusca = termoBusca;
	}
	
}
