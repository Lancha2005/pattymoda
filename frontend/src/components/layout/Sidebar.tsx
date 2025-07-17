// Sidebar de navegación con control de roles
import { 
  Home, 
  Package, 
  Users, 
  ShoppingCart, 
  BarChart3, 
  Settings, 
  Tag,
  Store,
  TrendingUp,
  UserCircle,
  UserCheck
} from 'lucide-react';
import { cn } from '../../utils/cn';

interface SidebarProps {
  activeTab: string;
  onTabChange: (tab: string) => void;
  isCollapsed: boolean;
  userRole: string;
  hasPermission: (permission: string) => boolean;
}

const navigation = [
  { id: 'dashboard', name: 'Dashboard', icon: Home },
  { id: 'products', name: 'Productos', icon: Package },
  { id: 'customers', name: 'Clientes', icon: Users },
  { id: 'sales', name: 'Ventas', icon: ShoppingCart },
  { id: 'new-sale', name: 'Nueva Venta', icon: ShoppingCart },
  { id: 'categories', name: 'Categorías', icon: Tag },
  { id: 'reports', name: 'Reportes', icon: BarChart3 },
  { id: 'analytics', name: 'Analytics', icon: TrendingUp },
  { id: 'users', name: 'Usuarios', icon: UserCheck },
  { id: 'settings', name: 'Configuración', icon: Settings },
];

const roleDisplayNames: Record<string, string> = {
  SUPER_ADMIN: 'Super Admin',
  ADMIN: 'Administrador',
  MANAGER: 'Gerente',
  VENDEDOR: 'Vendedor',
  CAJERO: 'Cajero',
  INVENTARIO: 'Inventario'
};

export function Sidebar({ activeTab, onTabChange, isCollapsed, userRole, hasPermission }: SidebarProps) {
  const filteredNavigation = navigation.filter(item => hasPermission(item.id));

  return (
    <aside className={cn(
      'bg-gradient-to-b from-gray-900 to-gray-800 text-white transition-all duration-300 flex flex-col',
      isCollapsed ? 'w-16' : 'w-64'
    )}>
      {/* Logo */}
      <div className="p-6 border-b border-gray-700">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-lg flex items-center justify-center">
            <Store className="w-6 h-6 text-black" />
          </div>
          {!isCollapsed && (
            <div>
              <h1 className="text-xl font-bold bg-gradient-to-r from-yellow-400 to-yellow-300 bg-clip-text text-transparent">
                DPattyModa
              </h1>
              <p className="text-xs text-gray-400">Pampa Hermosa, Loreto</p>
            </div>
          )}
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 px-4 py-6 space-y-2">
        {filteredNavigation.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.id;
          
          return (
            <button
              key={item.id}
              onClick={() => onTabChange(item.id)}
              className={cn(
                'w-full flex items-center px-3 py-3 rounded-lg transition-all duration-200 group',
                isActive 
                  ? 'bg-gradient-to-r from-yellow-400 to-yellow-500 text-black shadow-lg' 
                  : 'text-gray-300 hover:bg-gray-700 hover:text-white'
              )}
            >
              <Icon className={cn(
                'w-5 h-5 flex-shrink-0',
                isActive ? 'text-black' : 'text-gray-400 group-hover:text-white'
              )} />
              {!isCollapsed && (
                <span className="ml-3 font-medium">{item.name}</span>
              )}
            </button>
          );
        })}
      </nav>

      {/* User Info */}
      <div className="p-4 border-t border-gray-700">
        <div className="flex items-center space-x-3">
          <div className="w-8 h-8 bg-yellow-400 rounded-full flex items-center justify-center">
            <UserCircle className="w-5 h-5 text-black" />
          </div>
          {!isCollapsed && (
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-white truncate">
                {roleDisplayNames[userRole] || userRole}
              </p>
              <p className="text-xs text-gray-400 truncate">
                {userRole === 'SUPER_ADMIN' ? 'Acceso Total' : 'Acceso Limitado'}
              </p>
            </div>
          )}
        </div>
      </div>
    </aside>
  );
}