import { useState, useEffect } from 'react';
import { 
  DollarSign, 
  Package, 
  Users, 
  TrendingUp, 
  AlertTriangle,
  ShoppingBag,
  Calendar,
  Store,
  Plus,
  BarChart3,
  Settings
} from 'lucide-react';
import { StatsCard } from './StatsCard';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import { Button } from '../ui/Button';
import { DashboardService } from '../../services/dashboardService';

interface DashboardStats {
  totalProducts: number;
  totalCustomers: number;
  lowStockProducts: number;
  totalRevenue: number;
  activeProducts: number;
  vipCustomers: number;
  dailyRevenue?: number;
  monthlySales?: number;
  monthlyGrowth?: number;
}

export function Dashboard() {
  const [stats, setStats] = useState<DashboardStats>({
    totalProducts: 0,
    totalCustomers: 0,
    lowStockProducts: 0,
    totalRevenue: 0,
    activeProducts: 0,
    vipCustomers: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [recentActivity, setRecentActivity] = useState<any[]>([]);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [dashboardResponse, activityResponse] = await Promise.all([
        DashboardService.getDashboardStats(),
        DashboardService.getRecentActivity()
      ]);

      const dashboardStats = dashboardResponse.data;
      const activityData = activityResponse.data;
      
      setStats({
        totalProducts: dashboardStats.totalProducts,
        totalCustomers: dashboardStats.totalCustomers,
        lowStockProducts: dashboardStats.lowStockProducts,
        totalRevenue: dashboardStats.monthlyRevenue,
        activeProducts: dashboardStats.activeProducts,
        vipCustomers: Math.round(dashboardStats.totalCustomers * 0.15), // Estimación VIP
        dailyRevenue: dashboardStats.dailyRevenue,
        monthlySales: dashboardStats.monthlySales,
        monthlyGrowth: 15.3 // Valor por defecto para el crecimiento
      });
      
      setRecentActivity(activityData.activities || []);
      
    } catch (err: any) {
      setError("Error al cargar datos del dashboard: " + (err.message || "Error desconocido"));
      console.error("Error loading dashboard data:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleNavigateTo = (section: string) => {
    window.location.hash = `#${section}`;
    window.location.reload();
  };

  const handleNewSale = () => {
    handleNavigateTo('new-sale');
  };

  const handleAddProduct = () => {
    handleNavigateTo('products');
  };

  const handleAddCustomer = () => {
    handleNavigateTo('customers');
  };

  const handleViewReports = () => {
    handleNavigateTo('reports');
  };
  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500"></div>
        <span className="ml-3 text-gray-600">Cargando dashboard...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-12">
        <div className="text-red-500 mb-4">{error}</div>
        <Button onClick={loadDashboardData}>Reintentar</Button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="text-gray-600 mt-1">Bienvenido a tu panel de control DPattyModa</p>
        </div>
        <div className="text-right">
          <p className="text-sm text-gray-500">Hoy es</p>
          <p className="font-semibold text-gray-900">
            {new Date().toLocaleDateString('es-PE', { 
              weekday: 'long', 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}
          </p>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatsCard
          title="Ingresos del Mes"
          value={`S/ ${stats.totalRevenue.toLocaleString()}`}
          change={stats.monthlyGrowth || 15.3}
          icon={DollarSign}
          color="green"
        />
        <StatsCard
          title="Productos"
          value={stats.totalProducts}
          icon={Package}
          color="blue"
        />
        <StatsCard
          title="Clientes"
          value={stats.totalCustomers}
          change={8.2}
          icon={Users}
          color="purple"
        />
        <StatsCard
          title="Ventas Hoy"
          value={`S/ ${(stats.dailyRevenue || 0).toLocaleString()}`}
          icon={ShoppingBag}
          color="yellow"
        />
      </div>

      {/* Alertas */}
      {stats.lowStockProducts > 0 && (
        <Card className="border-l-4 border-l-orange-500 bg-orange-50">
          <div className="flex items-center p-4">
            <AlertTriangle className="w-5 h-5 text-orange-500 mr-3" />
            <div className="flex-1">
              <p className="font-medium text-orange-800">
                Productos con Stock Bajo
              </p>
              <p className="text-sm text-orange-700">
                {stats.lowStockProducts} productos necesitan reposición
              </p>
            </div>
            <Button variant="outline" size="sm" onClick={() => handleNavigateTo('products')}>
              Ver Productos
            </Button>
            <Button variant="primary" size="sm" onClick={handleNewSale}>
              Nueva Venta
            </Button>
          </div>
        </Card>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Información de la Tienda */}
        <Card>
          <CardHeader>
            <CardTitle>Información de DPattyModa</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="p-4 bg-gradient-to-r from-yellow-50 to-orange-50 rounded-lg">
                <h4 className="font-bold text-gray-900 mb-2">📍 Ubicación</h4>
                <p className="text-gray-700">Pampa Hermosa, Loreto, Perú</p>
              </div>
              
              <div className="p-4 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg">
                <h4 className="font-bold text-gray-900 mb-2">📞 Contacto</h4>
                <p className="text-gray-700">+51 965 123 456</p>
                <p className="text-gray-700">info@dpattymoda.com</p>
              </div>
              
              <div className="p-4 bg-gradient-to-r from-green-50 to-emerald-50 rounded-lg">
                <h4 className="font-bold text-gray-900 mb-2">🕒 Horarios</h4>
                <p className="text-gray-700">Lun-Sab: 9:00 AM - 8:00 PM</p>
                <p className="text-gray-700">Dom: 10:00 AM - 6:00 PM</p>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Acciones Rápidas */}
        <Card>
          <CardHeader>
            <CardTitle>Acciones Rápidas</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-4">
              <Button 
                className="h-20 flex flex-col items-center justify-center space-y-2"
                onClick={handleNewSale}
              >
                <ShoppingBag className="w-6 h-6" />
                <span>Nueva Venta</span>
              </Button>
              
              <Button 
                variant="outline" 
                className="h-20 flex flex-col items-center justify-center space-y-2"
                onClick={handleAddProduct}
              >
                <Package className="w-6 h-6" />
                <span>Agregar Producto</span>
              </Button>
              
              <Button 
                variant="outline" 
                className="h-20 flex flex-col items-center justify-center space-y-2"
                onClick={handleAddCustomer}
              >
                <Users className="w-6 h-6" />
                <span>Nuevo Cliente</span>
              </Button>
              
              <Button 
                variant="outline" 
                className="h-20 flex flex-col items-center justify-center space-y-2"
                onClick={handleViewReports}
              >
                <TrendingUp className="w-6 h-6" />
                <span>Ver Reportes</span>
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Actividad Reciente */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Actividad Reciente</CardTitle>
            <div className="flex space-x-2">
              <Button variant="outline" size="sm" onClick={() => handleNavigateTo('analytics')}>
                <BarChart3 className="w-4 h-4 mr-1" />
                Ver Analytics
              </Button>
              <Button variant="outline" size="sm" onClick={() => handleNavigateTo('settings')}>
                <Settings className="w-4 h-4 mr-1" />
                Configurar
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {recentActivity.length > 0 ? (
            <div className="space-y-4">
              {recentActivity.map((activity, index) => (
                <div key={index} className="flex items-center space-x-4 p-4 bg-gray-50 rounded-lg">
                  <div className={`w-3 h-3 rounded-full ${
                    activity.priority === 'high' ? 'bg-red-500' :
                    activity.priority === 'medium' ? 'bg-yellow-500' : 'bg-green-500'
                  }`}></div>
                  <div className="flex-1">
                    <p className="text-sm font-medium text-gray-900">{activity.message}</p>
                    <p className="text-xs text-gray-500">{activity.time}</p>
                  </div>
                  <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                    activity.priority === 'high' ? 'bg-red-100 text-red-800' :
                    activity.priority === 'medium' ? 'bg-yellow-100 text-yellow-800' : 'bg-green-100 text-green-800'
                  }`}>
                    {activity.priority === 'high' ? 'Urgente' : 
                     activity.priority === 'medium' ? 'Medio' : 'Bajo'}
                  </span>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <Activity className="w-12 h-12 mx-auto mb-4 opacity-50" />
              <p>No hay actividad reciente</p>
              <p className="text-sm">Las actividades aparecerán aquí cuando ocurran eventos en el sistema</p>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}