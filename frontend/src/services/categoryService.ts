// Servicio de categorías
import { apiService } from './api';
import { Category, ApiResponse } from '../types';

export class CategoryService {
  static async getAllCategories(): Promise<ApiResponse<Category[]>> {
    return apiService.get<Category[]>('/categorias');
  }

  static async getCategoryById(id: string): Promise<ApiResponse<Category>> {
    return apiService.get<Category>(`/categorias/${id}`);
  }

  static async createCategory(category: Omit<Category, 'id'>): Promise<ApiResponse<Category>> {
    return apiService.post<Category>('/categorias', category);
  }

  static async updateCategory(id: string, category: Partial<Category>): Promise<ApiResponse<Category>> {
    return apiService.put<Category>(`/categorias/${id}`, category);
  }

  static async deleteCategory(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/categorias/${id}`);
  }

  static async searchCategories(query: string): Promise<ApiResponse<Category[]>> {
    return apiService.get<Category[]>(`/categorias/search?q=${encodeURIComponent(query)}`);
  }
}
