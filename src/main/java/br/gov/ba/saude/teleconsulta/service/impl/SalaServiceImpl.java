package br.gov.ba.saude.teleconsulta.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.model.Sala;
import br.gov.ba.saude.teleconsulta.repository.SalaRepository;
import br.gov.ba.saude.teleconsulta.service.SalaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
		return salaRepository.findAllOrderByUnidade();
	}
    
    /**
     * Busca todas as salas de acordo com um termo.
     * 
     */
    @Transactional(readOnly = true)
    @Override
    public List<Sala> buscarPorTermo(String termoBusca) {
        return salaRepository.findByNomeOrSiglaUnidade(termoBusca.trim());
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
    
    /**
     * Implementação da consulta de disponibilidade utilizando Criteria API.
     * Retorna salas que NÃO possuem reservas conflitantes no período.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Sala> consultarDisponibilidade(Long unidadeId, LocalDateTime inicio, LocalDateTime fim) {
        // Busca todas as reservas que se cruzam com o período (conflitantes) na Unidade
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reserva> cq = cb.createQuery(Reserva.class);
        Root<Reserva> rootReserva = cq.from(Reserva.class);
        
        Predicate joinPredicate = cb.equal(rootReserva.get("sala").get("unidadeSaude").get("id"), unidadeId);

        // Cláusula de Conflito: (FIM_NOVA > INICIO_EXISTENTE E INICIO_NOVA < FIM_EXISTENTE)
        Predicate conflitoInicio = cb.lessThan(rootReserva.get("dataHoraInicio"), fim);
        Predicate conflitoFim = cb.greaterThan(rootReserva.get("dataHoraTermino"), inicio);
        
        Predicate conflitoTotal = cb.and(conflitoInicio, conflitoFim, joinPredicate);
        cq.where(conflitoTotal);
        
        List<Reserva> reservasConflitantes = entityManager.createQuery(cq).getResultList();
        
        // Extrai os IDs das salas que estão ocupadas
        List<Long> salasOcupadasIds = reservasConflitantes.stream()
            .map(reserva -> reserva.getSala().getId())
            .distinct()
            .collect(Collectors.toList());

        //  Busca todas as salas da unidade que NÃO estão na lista de ocupadas
        CriteriaQuery<Sala> cqSala = cb.createQuery(Sala.class);
        Root<Sala> rootSala = cqSala.from(Sala.class);
        
        Predicate unidadeFiltro = cb.equal(rootSala.get("unidadeSaude").get("id"), unidadeId);
        
        if (!salasOcupadasIds.isEmpty()) {
            // Desconsidera as salas ocupadas
            Predicate naoOcupada = cb.not(rootSala.get("id").in(salasOcupadasIds));
            cqSala.where(unidadeFiltro, naoOcupada);
        } else {
            cqSala.where(unidadeFiltro);
        }

        return entityManager.createQuery(cqSala).getResultList();
    }

}
