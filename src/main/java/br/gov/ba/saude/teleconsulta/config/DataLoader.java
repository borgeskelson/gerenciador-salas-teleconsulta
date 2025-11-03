package br.gov.ba.saude.teleconsulta.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.gov.ba.saude.teleconsulta.model.Usuario;
import br.gov.ba.saude.teleconsulta.service.UsuarioService;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioService usuarioService;

    public DataLoader(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
    	String emailUsuario01 = "usuario1@dominio.com"; 
    	String senhaUsuario01 = "123456";
    	Usuario usuario01 = usuarioService.obterUsuarioPorEmail(emailUsuario01);
    	
    	if (usuario01 == null) {
	    	// Inserção de um usuário com perfil Admin
	    	usuario01 = new Usuario();
	    	usuario01.setNome("Usuário 01");
	    	usuario01.setEmail(emailUsuario01); // Email que será usado para login
	    	usuario01.setCpf("12345678900");
	    	usuario01.setPerfil("ADMINISTRADOR"); // Perfil definido na entidade
	    	usuario01.setSenha(senhaUsuario01); // Senha que será codificada pelo UsuarioService
	    	
	        try {
	            usuarioService.criar(usuario01);
	        } catch (Exception e) {
	        	// Ignora se o usuário já existe
	        }
    	}
        
        System.out.println(">>> Acesso Usuário 01 (Perfil Admin): "+emailUsuario01+" / "+senhaUsuario01);
        
        String emailUsuario02 = "usuario2@dominio.com"; 
    	String senhaUsuario02 = "123456";
        Usuario usuarioAte = usuarioService.obterUsuarioPorEmail(emailUsuario02);
        
        if (usuarioAte == null) {
	        // Inserção de um usuário RESTRITO
        	usuarioAte = new Usuario();
        	usuarioAte.setNome("Usuário 02");
        	usuarioAte.setEmail(emailUsuario02); // Email que será usado para login
        	usuarioAte.setCpf("09876543211");
	        usuarioAte.setPerfil("RESTRITO"); // Perfil definido na entidade
	        usuarioAte.setSenha(senhaUsuario02); // Senha que será codificada pelo UsuarioService
	        
	        try {
	            usuarioService.criar(usuarioAte);
	        } catch (Exception e) {
	            // Ignora se o usuário já existe
	        }
        }
        
        System.out.println(">>> Acesso Usuário 02 (Perfil Restrito): "+emailUsuario02+" / "+senhaUsuario02);
    }
}
