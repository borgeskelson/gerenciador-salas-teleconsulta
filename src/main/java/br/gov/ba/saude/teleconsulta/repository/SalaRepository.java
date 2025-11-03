package br.gov.ba.saude.teleconsulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
       
}
