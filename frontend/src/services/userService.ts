// Servicio de usuarios actualizado
import { apiService } from './api';
import { User, ApiResponse } from '../types';

export class UserService {
  static async getAllUsers(): Promise<ApiResponse<User[]>> {
    return apiService.get<User[]>('/usuarios');
  }

  static async getUserById(id: string): Promise<ApiResponse<User>> {
    return apiService.get<User>(`/usuarios/${id}`);
  }

  static async createUser(user: Omit<User, 'id'>): Promise<ApiResponse<User>> {
    return apiService.post<User>('/usuarios', user);
  }

  static async updateUser(id: string, user: Partial<User>): Promise<ApiResponse<User>> {
    return apiService.put<User>(`/usuarios/${id}`, user);
  }

  static async deleteUser(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/usuarios/${id}`);
  }

  static async getRoles(): Promise<ApiResponse<any>> {
    return apiService.get<any>('/usuarios/roles');
  }
}