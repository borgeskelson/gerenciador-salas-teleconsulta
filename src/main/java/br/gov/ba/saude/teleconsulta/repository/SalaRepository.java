package br.gov.ba.saude.teleconsulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
	/** 
	 * Busca todas as salas ordenando pela sigla da unidade. 
	 */
	@Query("SELECT s FROM Sala s ORDER BY s.unidadeSaude.sigla, s.nome")
	List<Sala> findAllOrderByUnidade();
	
	/**
	* Busca salas por Nome da Sala OU pelo Nome/Sigla da Unidade de Saúde associada.
	*/
	@Query("SELECT s FROM Sala s JOIN s.unidadeSaude u WHERE " +
		"LOWER(s.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
		"LOWER(u.sigla) LIKE LOWER(CONCAT('%', :termo, '%'))")
	List<Sala> findByNomeOrSiglaUnidade(@Param("termo") String termo);
}
