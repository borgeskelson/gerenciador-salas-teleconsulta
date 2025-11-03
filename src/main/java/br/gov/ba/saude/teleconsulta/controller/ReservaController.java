package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.exception.ReservaException;
import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;
import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.service.ReservaService;
import br.gov.ba.saude.teleconsulta.service.SalaService;
import br.gov.ba.saude.teleconsulta.service.UnidadeSaudeService;
import br.gov.ba.saude.teleconsulta.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("reservaController")
@Component
@ViewScoped
public class ReservaController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ReservaService reservaService;
	private SalaService salaService;
	private UsuarioService usuarioService;
	private UnidadeSaudeService unidadeSaudeService;
	
	private List<Reserva> reservas;
	private Reserva selecionada;
	private List<Sala> salas;
	private List<Usuario> usuarios;
	private List<UnidadeSaude> unidades;
	private boolean edicao;
	
	// Parâmetros de filtro
	private Long unidadeId;
	private LocalDateTime dataHoraInicio;
	private LocalDateTime dataHoraFim;
	
	// Resultado da consulta de salas disponíveis
	private List<Sala> salasDisponiveis;
	
	// Campo de busca
	private String termoBusca;
	
	public ReservaController(ReservaService reservaService, SalaService salaService, UsuarioService usuarioService, 
			UnidadeSaudeService unidadeSaudeService) {
		this.reservaService = reservaService;
		this.salaService = salaService;
		this.usuarioService = usuarioService;
		this.unidadeSaudeService = unidadeSaudeService;
	}
	
	@PostConstruct
	public void carregar() {
		this.reservas = reservaService.buscarTodas();
		this.salas = salaService.buscarTodas();
		this.usuarios = usuarioService.buscarTodos();
		this.unidades = unidadeSaudeService.buscarTodas();
		this.edicao = false;
		prepararNovo();
	}
	
	public void prepararNovo() {
		if (this.selecionada == null || this.edicao == true) {
			this.selecionada = new Reserva();
			this.selecionada.setSala(new Sala());
			this.selecionada.setUsuario(new Usuario());
			this.edicao = false;
		}
	}
	
	public void editar(Reserva selecionada) {
		this.selecionada = selecionada;
		this.edicao = true;
	}
	
	public void salvar() {
		try {
			if (!edicao) {
				reservaService.criar(selecionada);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Reserva criada com sucesso."));
			} else {
				reservaService.editar(selecionada);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Reserva atualizada com sucesso."));
			}
			
			this.selecionada = null;
			carregar();
		} catch (ReservaException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage()));
			
			if (edicao) {
				this.selecionada = null;
				carregar();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar a reserva."));
		}
	}
	
	public void excluir(Long id) {
		try {
			reservaService.remover(id);
			carregar();
			
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Reserva cancelada com sucesso."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir a reserva."));
		}
	}
	
	/**
	* Realiza a busca por múltiplos campos.
	*/
	public void buscar() {
		try {
			if (termoBusca == null || termoBusca.trim().isEmpty()) {
				carregar();
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Busca", "Lista completa carregada."));
			} else {
				this.reservas = reservaService.buscarPorTermo(termoBusca);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Busca", this.reservas.size() + " reservas encontrados."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar a busca."));
			e.printStackTrace();
		}
	}
	
	/**
	* Executa a consulta de salas disponíveis utilizando o serviço.
	* Esta ação dispara a Criteria API implementada no ReservaServiceImpl.
	*/
	public void consultarDisponibilidade() {
		if (unidadeId == null || dataHoraInicio == null || dataHoraFim == null) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Preencha todos os campos do filtro."));
			return;
		}

		if (dataHoraFim.isBefore(dataHoraInicio) || dataHoraFim.isEqual(dataHoraInicio)) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "A data/hora de término deve ser posterior à de início."));
			return;
		}

		this.salasDisponiveis = salaService.consultarDisponibilidade(unidadeId, dataHoraInicio, dataHoraFim);

		if (salasDisponiveis.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Nenhuma sala disponível encontrada para o período."));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", salasDisponiveis.size() + " sala(s) encontrada(s)."));
		}
	}
	
	/**
	* Prepara um novo objeto Reserva inicializado com a sala e os horários do filtro da consulta de disponibilidade
	*/
	public void prepararReservaRapida(Sala sala) {
		this.selecionada = new Reserva();
		this.selecionada.setSala(sala);
		this.selecionada.setDataHoraInicio(this.dataHoraInicio);
		this.selecionada.setDataHoraTermino(this.dataHoraFim);
		this.selecionada.setUsuario(new Usuario()); 
	}
	
	public List<Reserva> getReservas() {
		return reservas;
	}
	
	public Reserva getSelecionada() {
		return selecionada;
	}

	public void setSelecionada(Reserva selecionada) {
		this.selecionada = selecionada;
	}
	
	public List<Sala> getSalas() {
		return salas;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public List<UnidadeSaude> getUnidades() {
		return unidades;
	}
	
	public boolean getEdicao() {
		return edicao;
	}
	
	public Long getUnidadeId() {
		return unidadeId;
	}

	public void setUnidadeId(Long unidadeId) {
		this.unidadeId = unidadeId;
	}

	public LocalDateTime getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public LocalDateTime getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(LocalDateTime dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public List<Sala> getSalasDisponiveis() {
		return salasDisponiveis;
	}

	public String getTermoBusca() {
		return termoBusca;
	}

	public void setTermoBusca(String termoBusca) {
		this.termoBusca = termoBusca;
	}
	
}
