// Servicio de reportes y analíticas
import { apiService } from './api';
import { SalesReport, ProductReport, CustomerReport, ApiResponse } from '../types';

export class ReportService {
  static async getSalesReport(params?: string): Promise<ApiResponse<SalesReport>> {
    return apiService.get<SalesReport>(`/reports/sales${params ? `?${params}` : ''}`);
  }

  static async getProductReport(params?: string): Promise<ApiResponse<ProductReport>> {
    return apiService.get<ProductReport>(`/reports/products${params ? `?${params}` : ''}`);
  }

  static async getCustomerReport(params?: string): Promise<ApiResponse<CustomerReport>> {
    return apiService.get<CustomerReport>(`/reports/customers${params ? `?${params}` : ''}`);
  }
}
