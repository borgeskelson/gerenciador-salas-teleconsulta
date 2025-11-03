package br.gov.ba.saude.teleconsulta.util;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("securityUtil")
@Component
@ViewScoped
public class SecurityUtilBean {

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Usuário Anônimo";
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    
    /**
     * Retorna a primeira authority do usuário para exibição.
     */
    public String getPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Não Logado";
        }
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        if (authorities != null && !authorities.isEmpty()) {
            // Retorna o primeiro perfil encontrado.
        	// As authorities são: "ADMINISTRADOR" ou "RESTRITO"
            return authorities.iterator().next().getAuthority();
        }
        
        return "Nenhum Perfil";
    }
    
    public boolean isAdmin() {
        return getPerfil().equals("ADMINISTRADOR");
    }
    
    public boolean isRestrito() {
        return getPerfil().equals("RESTRITO");
    }
    
    /**
     * Listener para o evento preRenderView.
     * Captura a mensagem de erro do Spring Security e a adiciona ao FacesContext.
     */
    public void showSpringSecurityError(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        String errorMessage = (String) context.getViewRoot().getAttributes().get("error");

        if (errorMessage != null && !errorMessage.isEmpty()) {
            // Tratamento para mensagen
            if (errorMessage.contains("Bad credentials") || errorMessage.contains("UserDetailsService returned null")) {
                errorMessage = "Credenciais inválidas. Verifique seu e-mail e senha.";
            } else if (errorMessage.contains("Usuário não encontrado")) {
                errorMessage = "O e-mail informado não está cadastrado.";
            }
            
            // Adiciona a mensagem ao FacesContext, que será capturada pelo p:msg
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Acesso", errorMessage);
            context.addMessage(null, message); 
            context.getViewRoot().getAttributes().remove("error");
        }
    }
    
}
