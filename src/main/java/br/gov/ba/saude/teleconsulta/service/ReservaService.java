package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Reserva;

public interface ReservaService {
	List<Reserva> buscarTodas();
	List<Reserva> buscarPorTermo(String termoBusca);
	Reserva criar(Reserva reserva) throws Exception;
	Reserva editar(Reserva reserva) throws Exception;
	void remover(Long id);
}
