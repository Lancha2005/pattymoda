// Servicio de configuración de tienda
import { apiService } from './api';
import { StoreConfig, ApiResponse } from '../types';

export class SettingsService {
  static async getConfig(): Promise<ApiResponse<StoreConfig>> {
    return apiService.get<StoreConfig>('/configuracion');
  }

  static async updateConfig(config: Partial<StoreConfig>): Promise<ApiResponse<StoreConfig>> {
    return apiService.put<StoreConfig>('/configuracion', config);
  }
}
