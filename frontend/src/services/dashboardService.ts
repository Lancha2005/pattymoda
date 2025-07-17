// Servicio para el dashboard
import { apiService } from './api';
import { ApiResponse } from '../types';

interface DashboardStats {
  totalProducts: number;
  activeProducts: number;
  lowStockProducts: number;
  totalCustomers: number;
  activeCustomers: number;
  monthlyRevenue: number;
  monthlySales: number;
  dailyRevenue: number;
  dailySales: number;
  totalUsers: number;
}

interface RecentActivity {
  activities: Array<{
    type: string;
    message: string;
    time: string;
    priority: string;
  }>;
  lastUpdate: string;
}

export class DashboardService {
  static async getDashboardStats(): Promise<ApiResponse<DashboardStats>> {
    return apiService.get<DashboardStats>('/dashboard/stats');
  }

  static async getRecentActivity(): Promise<ApiResponse<RecentActivity>> {
    return apiService.get<RecentActivity>('/dashboard/recent-activity');
  }
}