package br.gov.ba.saude.teleconsulta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ba.saude.teleconsulta.exception.ReservaException;
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
    public Reserva criar(Reserva reserva) throws ReservaException {
        // Validações de tempo
        if (reserva.getDataHoraTermino().isBefore(reserva.getDataHoraInicio()) || 
            reserva.getDataHoraTermino().isEqual(reserva.getDataHoraInicio())) {
            throw new ReservaException("O término da reserva deve ser posterior ao início.");
        }
        
        // Verifica conflito de horário
        List<Reserva> conflitos = reservaRepository.findBySalaAndDataHoraTerminoAfterAndDataHoraInicioBefore(
            reserva.getSala(), 
            reserva.getDataHoraInicio(), 
            reserva.getDataHoraTermino()
        );

        if (!conflitos.isEmpty()) {
            throw new ReservaException("Já existe uma reserva para esta sala no período solicitado.");
        }
        
        return reservaRepository.save(reserva);
    }
    
    /**
     * Valida e edita a nova reserva.
     */
    @Transactional
    @Override
    public Reserva editar(Reserva reserva) throws ReservaException {
        // Validações de tempo
        if (reserva.getDataHoraTermino().isBefore(reserva.getDataHoraInicio()) || 
            reserva.getDataHoraTermino().isEqual(reserva.getDataHoraInicio())) {
            throw new ReservaException("O término da reserva deve ser posterior ao início.");
        }
        
        // Verifica conflito de horário
        List<Reserva> conflitos = reservaRepository.findBySalaAndDataHoraTerminoAfterAndDataHoraInicioBefore(
            reserva.getSala(), 
            reserva.getDataHoraInicio(), 
            reserva.getDataHoraTermino()
        );
        
        if (!conflitos.isEmpty()) {
        	if (conflitos.size() > 1
        			|| conflitos.getFirst().getId() != reserva.getId()) {
        		throw new ReservaException("Já existe uma reserva para esta sala no período solicitado.");
        	}
        }
        
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
