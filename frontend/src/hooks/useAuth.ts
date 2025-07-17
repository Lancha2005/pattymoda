// Hook de autenticación mejorado con roles
import { useState, useEffect } from 'react';
import { User } from '../types';
import { AuthService } from '../services/authService';

export function useAuthState() {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    // Verificar si hay un usuario logueado al cargar
    const currentUser = AuthService.getCurrentUser();
    if (currentUser) {
      setUser(currentUser);
    }
  }, []);

  const login = async (email: string, password: string) => {
    setIsLoading(true);
    try {
      const response = await AuthService.login({ email, password });
      const userData: User = {
        id: response.id.toString(),
        email: response.email,
        firstName: response.nombre.split(' ')[0],
        lastName: response.nombre.split(' ').slice(1).join(' ') || '',
        role: response.rol as 'SUPER_ADMIN' | 'ADMIN' | 'MANAGER' | 'VENDEDOR' | 'CAJERO' | 'INVENTARIO',
        createdAt: new Date(),
        updatedAt: new Date(),
      };
      setUser(userData);
    } catch (error: any) {
      console.error('Login error:', error);
      throw new Error(error.message || 'Error al iniciar sesión');
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    AuthService.logout();
    setUser(null);
  };

  const hasPermission = (permission: string): boolean => {
    if (!user) return false;
    
    const rolePermissions: Record<string, string[]> = {
      SUPER_ADMIN: ['*'],
      ADMIN: ['dashboard', 'products', 'customers', 'sales', 'categories', 'reports', 'analytics', 'settings', 'users'],
      MANAGER: ['dashboard', 'products', 'customers', 'sales', 'categories', 'reports', 'analytics'],
      VENDEDOR: ['dashboard', 'products', 'customers', 'sales', 'new-sale'],
      CAJERO: ['dashboard', 'sales', 'new-sale', 'customers'],
      INVENTARIO: ['dashboard', 'products', 'categories', 'reports']
    };
    
    const permissions = rolePermissions[user.role] || [];
    return permissions.includes('*') || permissions.includes(permission);
  };

  return {
    user,
    login,
    logout,
    isLoading,
    isAuthenticated: !!user,
    hasPermission,
  };
}