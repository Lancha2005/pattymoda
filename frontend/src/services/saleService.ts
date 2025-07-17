// Servicio de ventas
import { apiService } from './api';
import { Sale, ApiResponse, DashboardStats } from '../types';

export class SaleService {
  static async getAllSales(): Promise<ApiResponse<Sale[]>> {
    return apiService.get<Sale[]>('/ventas');
  }

  static async getSaleById(id: string): Promise<ApiResponse<Sale>> {
    return apiService.get<Sale>(`/ventas/${id}`);
  }

  static async createSale(sale: Omit<Sale, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<Sale>> {
    return apiService.post<Sale>('/ventas', sale);
  }

  static async updateSale(id: string, sale: Partial<Sale>): Promise<ApiResponse<Sale>> {
    return apiService.put<Sale>(`/ventas/${id}`, sale);
  }

  static async deleteSale(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/ventas/${id}`);
  }

  static async getDashboardStats(): Promise<ApiResponse<DashboardStats>> {
    return apiService.get<DashboardStats>('/reportes/dashboard');
  }

  static async getSalesByDateRange(startDate: string, endDate: string): Promise<ApiResponse<Sale[]>> {
    return apiService.get<Sale[]>(`/reportes/ventas/rango-fechas?start=${startDate}&end=${endDate}`);
  }
}