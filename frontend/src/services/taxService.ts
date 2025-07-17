// Servicio para gestión de impuestos
import { apiService } from './api';
import { ApiResponse } from '../types';

interface TaxConfig {
  id: string;
  nombre: string;
  porcentaje: number;
  activo: boolean;
  aplicarPorDefecto: boolean;
  descripcion?: string;
  createdAt: Date;
  updatedAt: Date;
}

export class TaxService {
  static async getAllTaxes(): Promise<ApiResponse<TaxConfig[]>> {
    return apiService.get<TaxConfig[]>('/impuestos');
  }

  static async getActiveTaxes(): Promise<ApiResponse<TaxConfig[]>> {
    return apiService.get<TaxConfig[]>('/impuestos/activos');
  }

  static async getIGVConfig(): Promise<ApiResponse<TaxConfig | null>> {
    return apiService.get<TaxConfig | null>('/impuestos/igv');
  }

  static async createTax(tax: Omit<TaxConfig, 'id' | 'createdAt' | 'updatedAt'>): Promise<ApiResponse<TaxConfig>> {
    return apiService.post<TaxConfig>('/impuestos', tax);
  }

  static async updateTax(id: string, tax: Partial<TaxConfig>): Promise<ApiResponse<TaxConfig>> {
    return apiService.put<TaxConfig>(`/impuestos/${id}`, tax);
  }

  static async toggleIGV(activate: boolean): Promise<ApiResponse<void>> {
    return apiService.put<void>('/impuestos/igv/activar', { activar: activate });
  }

  static async updateIGVPercentage(percentage: number): Promise<ApiResponse<void>> {
    return apiService.put<void>('/impuestos/igv/porcentaje', { porcentaje: percentage });
  }

  static async deleteTax(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/impuestos/${id}`);
  }
}