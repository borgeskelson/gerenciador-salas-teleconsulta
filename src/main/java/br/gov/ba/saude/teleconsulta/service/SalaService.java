package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Sala;

public interface SalaService {
	List<Sala> buscarTodas();
	Sala criar(Sala sala);
	Sala editar(Sala sala);
	void remover(Long id);
}
