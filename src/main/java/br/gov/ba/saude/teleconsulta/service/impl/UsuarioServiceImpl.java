package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.repository.UsuarioRepository;
import br.gov.ba.saude.teleconsulta.service.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {
	
    private final UsuarioRepository usuarioRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Busca todos os usuários existentes.
     * 
     */
    @Transactional(readOnly = true)
    @Override
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
    
    /**
     * Busca todos os usuários de acordo com um termo.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<Usuario> buscarPorTermo(String termoBusca) {
		return usuarioRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCpfContainingIgnoreCase(
				termoBusca.trim(), termoBusca.trim(), termoBusca.trim());
	}
    
    /**
     * Salva o novo usuário.
     */
    @Transactional
    @Override
    public Usuario criar(Usuario usuario) {
        // Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Edita o usuário.
     */
    @Transactional
    @Override
	public Usuario editar(Usuario usuario) {
    	// Codifica a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
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
	
    @Transactional
	@Override
	public Usuario obterUsuarioPorEmail(String email) {
		return usuarioRepository.findByEmail(email).orElse(null);
	}
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados pelo e-mail 
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
        
        // Cria a lista de autoridades a partir do campo 'perfil'
        Set<GrantedAuthority> authorities = Collections.singleton(
    		new SimpleGrantedAuthority(usuario.getPerfil())
        );

        // retorna o objeto UserDetails padrão do Spring Security
        return new User(
    		usuario.getEmail(), // username (email)
            usuario.getSenha(), // password (senha hasheada)
            authorities         // authorities (perfil)
        );
    }
	
}
