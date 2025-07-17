// Servicio de clientes
import { apiService } from './api';
import { Customer, ApiResponse } from '../types';

export class CustomerService {
  static async getAllCustomers(): Promise<ApiResponse<Customer[]>> {
    return apiService.get<Customer[]>('/clientes');
  }

  static async getCustomerById(id: string): Promise<ApiResponse<Customer>> {
    return apiService.get<Customer>(`/clientes/${id}`);
  }

  static async createCustomer(customer: Omit<Customer, 'id' | 'totalPurchases' | 'createdAt'>): Promise<ApiResponse<Customer>> {
    return apiService.post<Customer>('/clientes', customer);
  }

  static async updateCustomer(id: string, customer: Partial<Customer>): Promise<ApiResponse<Customer>> {
    return apiService.put<Customer>(`/clientes/${id}`, customer);
  }

  static async deleteCustomer(id: string): Promise<ApiResponse<void>> {
    return apiService.delete<void>(`/clientes/${id}`);
  }

  static async searchCustomers(query: string): Promise<ApiResponse<Customer[]>> {
    return apiService.get<Customer[]>(`/clientes/search?q=${encodeURIComponent(query)}`);
  }
}