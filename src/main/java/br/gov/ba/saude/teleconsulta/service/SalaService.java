package br.gov.ba.saude.teleconsulta.service;

import java.time.LocalDateTime;
import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Sala;

public interface SalaService {
	List<Sala> buscarTodas();
	List<Sala> buscarPorTermo(String termoBusca);
	Sala criar(Sala sala);
	Sala editar(Sala sala);
	void remover(Long id);
	List<Sala> consultarDisponibilidade(Long unidadeId, LocalDateTime inicio, LocalDateTime fim);
}
