package br.gov.ba.saude.teleconsulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;

@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {
	/**
	* Busca unidades por Nome, Sigla ou CNPJ.
	*/
	List<UnidadeSaude> findByNomeContainingIgnoreCaseOrSiglaContainingIgnoreCaseOrCnpjContainingIgnoreCase(String nome, String sigla, String cnpj);
}
