package br.gov.ba.saude.teleconsulta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.model.Sala;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	/** 
	* Consulta para verificar conflito de horário em uma sala específica. 
	* */
	List<Reserva> findBySalaAndDataHoraTerminoAfterAndDataHoraInicioBefore(Sala sala, LocalDateTime inicio, LocalDateTime termino);
}
