package br.gov.ba.saude.teleconsulta.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("usuarioController")
@Component
@ViewScoped
public class UsuarioController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final UsuarioService usuarioService;
	
	private List<Usuario> usuarios;
	private Usuario selecionado;
	private String novaSenha; // Campo auxiliar para a senha
	private final List<String> perfis = List.of("ADMINISTRADOR", "RESTRITO");
	private boolean edicao;
	
	// Campo de busca
	private String termoBusca;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	/**
	 * Método executado após a criação do bean para carregar os dados iniciais.
	 */
	@PostConstruct
	public void carregar() {
		this.usuarios = usuarioService.buscarTodos();
		this.edicao = false;
	}
	
	/**
	 * Prepara um novo objeto Usuario para cadastro.
	 */
	public void prepararNovo() {
		if (this.selecionado == null || this.edicao == true) {
			this.selecionado = new Usuario();
			this.edicao = false;
		}
		this.novaSenha = null; // Limpa a senha ao preparar novo
	}
	
	public void editar(Usuario selecionado) {
		this.selecionado = selecionado;
		this.edicao = true;
	}
	
	/**
	 * Salva (cria ou atualiza) o usuário.
	 */
	public void salvar() {
		try {
			// Se for um novo usuário ou a senha foi alterada, criptografa a nova senha
			if (selecionado.getId() == null || novaSenha != null && !novaSenha.isEmpty()) {
				selecionado.setSenha(novaSenha);
			}
			
			// Garante que o perfil básico seja setado (RESTRITO)
			if (selecionado.getPerfil() == null || selecionado.getPerfil().isEmpty()) {
				selecionado.setPerfil("RESTRITO");
			}
			
			if (!edicao) {
				usuarioService.criar(selecionado);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário criado com sucesso."));
			} else {
				usuarioService.editar(selecionado);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário atualizado com sucesso."));
			}
			
			this.selecionado = null;
			carregar();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar o usuário."));
		}
	}
	
	/**
	 * Exclui um usuário.
	 */
	public void excluir(Long id) {
		try {
			usuarioService.remover(id);
			carregar(); 
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário excluído com sucesso."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao excluir o usuário."));
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
				this.usuarios = usuarioService.buscarPorTermo(termoBusca);
				
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Busca", this.usuarios.size() + " usuários encontrados."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao realizar a busca."));
			e.printStackTrace();
		}
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public Usuario getSelecionado() {
		return selecionado;
	}
	
	public void setSelecionado(Usuario selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	
	public List<String> getPerfis() {
		return perfis;
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
