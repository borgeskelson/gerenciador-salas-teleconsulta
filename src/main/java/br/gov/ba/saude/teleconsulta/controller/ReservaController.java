package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.exception.ReservaException;
import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.service.ReservaService;
import br.gov.ba.saude.teleconsulta.service.SalaService;
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
    
    private List<Reserva> reservas;
    private Reserva selecionada;
    private List<Sala> salas;
    private List<Usuario> usuarios;
    private boolean edicao;
    
    public ReservaController(ReservaService reservaService, SalaService salaService, UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.salaService = salaService;
        this.usuarioService = usuarioService;
    }
    
    @PostConstruct
    public void carregar() {
        this.reservas = reservaService.buscarTodas();
        this.salas = salaService.buscarTodas();
        this.usuarios = usuarioService.buscarTodos();
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
	
	public boolean getEdicao() {
        return edicao;
	}
    
}
