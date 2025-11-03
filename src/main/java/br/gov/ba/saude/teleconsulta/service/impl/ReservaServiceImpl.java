package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.repository.ReservaRepository;
import br.gov.ba.saude.teleconsulta.service.ReservaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ReservaServiceImpl implements ReservaService {
	
	private final ReservaRepository reservaRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public ReservaServiceImpl(ReservaRepository reservaRepository, EntityManager entityManager) {
        this.reservaRepository = reservaRepository;
        this.entityManager = entityManager;
    }
    
    /**
     * Busca todas as reservas existentes.
     * 
     */
    @Transactional(readOnly = true)
    @Override
    public List<Reserva> buscarTodas() {
        return reservaRepository.findAll();
    }
    
    /**
     * Valida e salva a nova reserva.
     */
    @Transactional
    @Override
    public Reserva criar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
    
    /**
     * Valida e edita a nova reserva.
     */
    @Transactional
    @Override
    public Reserva editar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
    
    /** 
     * Cancela a reserva.
     */
    @Transactional
    @Override
    public void remover(Long id) {
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if (reserva.isPresent()) {
            reservaRepository.delete(reserva.get());
        }
    }
    
}
