package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.repository.UsuarioRepository;
import br.gov.ba.saude.teleconsulta.service.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
    private final UsuarioRepository usuarioRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, EntityManager entityManager) {
        this.usuarioRepository = usuarioRepository;
        this.entityManager = entityManager;
    }
    
    /**
     * Busca todos os usuários existentes.
     * 
     */
    @Transactional(readOnly = true)
    @Override
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
    
    /**
     * Salva o novo usuário.
     */
    @Transactional
    @Override
    public Usuario criar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Edita o usuário.
     */
    @Transactional
    @Override
	public Usuario editar(Usuario usuario) {
        return usuarioRepository.save(usuario);
	}
    
    /** 
     * Remove o usuário.
     */
    @Transactional
	@Override
	public void remover(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			usuarioRepository.delete(usuario.get());
		}
	}
	
}
