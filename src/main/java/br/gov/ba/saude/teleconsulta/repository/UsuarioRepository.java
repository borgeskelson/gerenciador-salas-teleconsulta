package br.gov.ba.saude.teleconsulta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.ba.saude.teleconsulta.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	/**
	* Busca usuários por Nome OU Email OU CPF.
	*/
	List<Usuario> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCpfContainingIgnoreCase(String nome, String email, String cpf);
}
