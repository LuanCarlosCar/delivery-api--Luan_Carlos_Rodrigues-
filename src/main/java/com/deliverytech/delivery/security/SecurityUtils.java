package com.deliverytech.delivery.security;

import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        Usuario user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static String getCurrentUserEmail() {
        Usuario user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    public static Role getCurrentUserRole() {
        Usuario user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    public static Long getCurrentUserRestauranteId() {
        Usuario user = getCurrentUser();
        return user != null ? user.getRestauranteId() : null;
    }

    public static boolean hasRole(String role) {
        Role currentRole = getCurrentUserRole();
        return currentRole != null && currentRole.name().equals(role);
    }

    public static boolean hasRole(Role role) {
        Role currentRole = getCurrentUserRole();
        return currentRole != null && currentRole.equals(role);
    }

    public static boolean isAdmin() {
        return hasRole(Role.ADMIN);
    }

    public static boolean isCliente() {
        return hasRole(Role.CLIENTE);
    }

    public static boolean isRestaurante() {
        return hasRole(Role.RESTAURANTE);
    }

    public static boolean isEntregador() {
        return hasRole(Role.ENTREGADOR);
    }

    public static boolean isCurrentUserOrAdmin(Long userId) {
        return isAdmin() || getCurrentUserId().equals(userId);
    }

    public static boolean isRestauranteOwnerOrAdmin(Long restauranteId) {
        return isAdmin() || (isRestaurante() && getCurrentUserRestauranteId().equals(restauranteId));
    }
}