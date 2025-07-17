// Servicio para analytics
import { apiService } from './api';
import { ApiResponse } from '../types';

export class AnalyticsService {
  static async getKPIs(): Promise<ApiResponse<any>> {
    return apiService.get<any>('/analytics/kpi');
  }

  static async getCustomerSegments(): Promise<ApiResponse<any>> {
    return apiService.get<any>('/analytics/customer-segments');
  }

  static async getSalesTrends(days: number = 30): Promise<ApiResponse<any>> {
    return apiService.get<any>(`/analytics/sales-trends?days=${days}`);
  }
}