package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Reserva;

public interface ReservaService {
	List<Reserva> buscarTodas();
	Reserva criar(Reserva reserva);
	Reserva editar(Reserva reserva);
	void remover(Long id);
}
