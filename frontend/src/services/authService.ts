// Servicio de autenticación conectado al backend
import { apiService } from './api';
import { User } from '../types';

interface LoginRequest {
  email: string;
  password: string;
}

interface LoginResponse {
  token: string;
  type: string;
  id: number;
  nombre: string;
  email: string;
  rol: string;
}

export class AuthService {
  static async login(credentials: LoginRequest): Promise<LoginResponse> {
    const apiResponse = await apiService.post<LoginResponse>('/auth/login', credentials);
    const response = (apiResponse as any).data ? (apiResponse as any).data : apiResponse;
    // Guardar token en localStorage
    if (response.token) {
      localStorage.setItem('authToken', response.token);
      localStorage.setItem('user', JSON.stringify({
        id: response.id.toString(),
        email: response.email,
        firstName: response.nombre.split(' ')[0],
        lastName: response.nombre.split(' ').slice(1).join(' ') || '',
        role: response.rol,
        createdAt: new Date(),
        updatedAt: new Date(),
      }));
    }
    return response;
  }

  static async register(userData: {
    nombre: string;
    email: string;
    password: string;
    rol?: string;
  }): Promise<string> {
    const response = await apiService.post<string>('/auth/register', userData);
    if (typeof response === 'string') {
      return response;
    }
    if (response && typeof (response as any).data === 'string') {
      return (response as any).data;
    }
    return '';
  }

  static logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }

  static getCurrentUser(): User | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch (error) {
        console.error('Error parsing user from localStorage:', error);
        return null;
      }
    }
    return null;
  }

  static getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  static isAuthenticated(): boolean {
    return !!this.getToken();
  }
}