package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.repository.SalaRepository;
import br.gov.ba.saude.teleconsulta.service.SalaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class SalaServiceImpl implements SalaService {
	
	private final SalaRepository salaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public SalaServiceImpl(SalaRepository salaRepository, EntityManager entityManager) {
        this.salaRepository = salaRepository;
        this.entityManager = entityManager;
    }
    
    /**
     * Busca todas as salas existentes.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<Sala> buscarTodas() {
		return salaRepository.findAll();
	}
    
    /**
     * Salva a nova sala.
     */
    @Transactional
    @Override
    public Sala criar(Sala sala) {
        return salaRepository.save(sala);
    }
    
    /**
     * Edita a sala.
     */
    @Transactional
    @Override
    public Sala editar(Sala sala) {
        return salaRepository.save(sala);
    }
    
    /** 
     * Remove a sala.
     */
    @Transactional
    @Override
    public void remover(Long id) {
        Optional<Sala> sala = salaRepository.findById(id);
        if (sala.isPresent()) {
        	salaRepository.delete(sala.get());
        }
    }

}
