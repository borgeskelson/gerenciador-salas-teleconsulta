package br.gov.ba.saude.teleconsulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	/**
	 * Busca pacientes por Nome, Nome Social, CPF ou CNS.
	 */
	List<Paciente> findByNomeContainingIgnoreCaseOrNomeSocialContainingIgnoreCaseOrCpfContainingIgnoreCaseOrCnsContainingIgnoreCase(
		String nome, String nomeSocial, String cpf, String cns);
}
