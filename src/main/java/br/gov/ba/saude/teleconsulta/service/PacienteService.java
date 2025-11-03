package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Paciente;

public interface PacienteService {
	List<Paciente> buscarTodos();
	Paciente criar(Paciente paciente);
	Paciente editar(Paciente paciente);
	void remover(Long id);
}
