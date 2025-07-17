// Servicio de productos
import { apiService } from './api';
import { Product, ApiResponse } from '../types';

export class ProductService {
  static async getAllProducts(): Promise<ApiResponse<Product[]>> {
    return apiService.get<Product[]>('/productos');
  }

  static async getProductById(id: string): Promise<ApiResponse<Product>> {
    return apiService.get<Product>(`/productos/${id}`);
  }

  static async createProduct(product: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Product>> {
    return apiService.post<Product>('/productos', product);
  }

  static async updateProduct(id: string, product: Partial<Product>): Promise<ApiResponse<Product>> {
    return apiService.put<Product>(`/productos/${id}`, product);
  }

  static async deleteProduct(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/productos/${id}`);
  }

  static async searchProducts(query: string): Promise<ApiResponse<Product[]>> {
    return apiService.get<Product[]>(`/productos/search?q=${encodeURIComponent(query)}`);
  }

  static async getProductsByCategory(categoryId: string): Promise<ApiResponse<Product[]>> {
    return apiService.get<Product[]>(`/productos/categoria/${categoryId}`);
  }

  static async getLowStockProducts(): Promise<ApiResponse<Product[]>> {
    return apiService.get<Product[]>('/productos/stock-bajo');
  }
}