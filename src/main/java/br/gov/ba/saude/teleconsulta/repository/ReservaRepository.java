package br.gov.ba.saude.teleconsulta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Reserva;
import br.gov.ba.saude.teleconsulta.model.Sala;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
	/**
	* Busca reservas por Nome da Sala, OU pela Sigla da Unidade de Saúde, OU pelo Nome do Usuário associados.
	*/
	@Query("SELECT r FROM Reserva r JOIN r.sala s JOIN r.usuario u WHERE " +
		"LOWER(s.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
		"LOWER(s.unidadeSaude.sigla) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
		"LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
	List<Reserva> findByNomeOrSalaOrSiglaUnidadeOrUsuario(@Param("termo") String termo);

	/** 
	* Consulta para verificar conflito de horário em uma sala específica. 
	* */
	List<Reserva> findBySalaAndDataHoraTerminoAfterAndDataHoraInicioBefore(Sala sala, LocalDateTime inicio, LocalDateTime termino);
}
