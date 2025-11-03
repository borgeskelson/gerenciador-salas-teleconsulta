package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;
import br.gov.ba.saude.teleconsulta.repository.UnidadeSaudeRepository;
import br.gov.ba.saude.teleconsulta.service.UnidadeSaudeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UnidadeSaudeServiceImpl implements UnidadeSaudeService {
	
	private final UnidadeSaudeRepository unidadeSaudeRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public UnidadeSaudeServiceImpl(UnidadeSaudeRepository unidadeSaudeRepository, EntityManager entityManager) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
        this.entityManager = entityManager;
    }
    
    /**
     * Busca todas as unidades de saúde existentes.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<UnidadeSaude> buscarTodas() {
		return unidadeSaudeRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
    
    /**
     * Busca todas as unidades de saúde de acordo com um termo.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<UnidadeSaude> buscarPorTermo(String termoBusca) {
		return unidadeSaudeRepository.findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCaseOrCnpjContainingIgnoreCase(
        		termoBusca.trim(), termoBusca.trim(), termoBusca.trim());
	}
    
    /**
     * Salva a nova unidade de saúde.
     */
    @Transactional
	@Override
	public UnidadeSaude criar(UnidadeSaude unidade) {
		return unidadeSaudeRepository.save(unidade);
	}
    
    /**
     * Edita a unidade de saúde.
     */
    @Transactional
	@Override
	public UnidadeSaude editar(UnidadeSaude unidade) {
		return unidadeSaudeRepository.save(unidade);
	}
    
    /** 
     * Remove a unidade de saúde.
     */
    @Transactional
	@Override
	public void remover(Long id) {
    	Optional<UnidadeSaude> unidadeSaude = unidadeSaudeRepository.findById(id);
        if (unidadeSaude.isPresent()) {
        	unidadeSaudeRepository.delete(unidadeSaude.get());
        }
	}

}
