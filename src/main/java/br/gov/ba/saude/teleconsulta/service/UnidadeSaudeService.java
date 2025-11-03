package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.UnidadeSaude;

public interface UnidadeSaudeService {
	List<UnidadeSaude> buscarTodas();
	UnidadeSaude criar(UnidadeSaude unidade);
	UnidadeSaude editar(UnidadeSaude unidade);
	void remover(Long id);
}
