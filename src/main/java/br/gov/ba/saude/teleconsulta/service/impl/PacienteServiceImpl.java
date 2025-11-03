package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Paciente;
import br.gov.ba.saude.teleconsulta.repository.PacienteRepository;
import br.gov.ba.saude.teleconsulta.service.PacienteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PacienteServiceImpl implements PacienteService {
	
	private final PacienteRepository pacienteRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public PacienteServiceImpl(PacienteRepository pacienteRepository, EntityManager entityManager) {
        this.pacienteRepository = pacienteRepository;
        this.entityManager = entityManager;
    }
    
    /**
     * Busca todos os pacientes existentes.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<Paciente> buscarTodos() {
		return pacienteRepository.findAll();
	}
    
    /**
     * Busca todos os pacientes de acordo com um termo.
     * 
     */
    @Transactional(readOnly = true)
	@Override
	public List<Paciente> buscarPorTermo(String termoBusca) {
		return pacienteRepository.findByNomeContainingIgnoreCaseOrNomeSocialContainingIgnoreCaseOrCpfContainingIgnoreCaseOrCnsContainingIgnoreCase(
				termoBusca.trim(), termoBusca.trim(), termoBusca.trim(), termoBusca.trim());
	}
    
    /**
     * Salva o novo paciente.
     */
    @Transactional
	@Override
	public Paciente criar(Paciente paciente) {
		return pacienteRepository.save(paciente);
	}
    
    /**
     * Edita o paciente.
     */
    @Transactional
	@Override
	public Paciente editar(Paciente paciente) {
    	return pacienteRepository.save(paciente);
	}
    
    /** 
     * Remove o paciente.
     */
    @Transactional
	@Override
	public void remover(Long id) {
    	Optional<Paciente> paciente = pacienteRepository.findById(id);
        if (paciente.isPresent()) {
        	pacienteRepository.delete(paciente.get());
        }
	}

}
