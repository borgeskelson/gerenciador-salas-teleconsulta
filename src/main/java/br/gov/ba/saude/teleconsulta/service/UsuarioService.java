package br.gov.ba.saude.teleconsulta.service;

import java.util.List;

import br.gov.ba.saude.teleconsulta.model.Usuario;

public interface UsuarioService {
	List<Usuario> buscarTodos();
	List<Usuario> buscarPorTermo(String termoBusca);
	Usuario criar(Usuario usuario);
	Usuario editar(Usuario usuario);
	void remover(Long id);
}
