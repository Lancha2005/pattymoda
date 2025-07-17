// Aplicación principal con sistema de roles y funcionalidad completa
import React, { useState } from 'react';
import { Sidebar } from './components/layout/Sidebar';
import { Header } from './components/layout/Header';
import { Dashboard } from './components/dashboard/Dashboard';
import { ProductList } from './components/products/ProductList';
import { CustomerList } from './components/customers/CustomerList';
import { SalesList } from './components/sales/SalesList';
import { NewSale } from './components/sales/NewSale';
import { CategoryList } from './components/categories/CategoryList';
import { Reports } from './components/reports/Reports';
import { Analytics } from './components/analytics/Analytics';
import { Settings } from './components/settings/Settings';
import { UserList } from './components/users/UserList';
import { ForgotPassword } from './components/auth/ForgotPassword';
import { ResetPassword } from './components/auth/ResetPassword';
import { useAuthState } from './hooks/useAuth';
import { Input } from './components/ui/Input';
import { Button } from './components/ui/Button';
import { Store, Mail, Lock } from 'lucide-react';

function App() {
  const { user, login, logout, isLoading, isAuthenticated, hasPermission } = useAuthState();
  const [activeTab, setActiveTab] = useState('dashboard');
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loginError, setLoginError] = useState('');
  const [showForgotPassword, setShowForgotPassword] = useState(false);

  // Detectar si estamos en la página de reset password
  const urlParams = new URLSearchParams(window.location.search);
  const resetToken = urlParams.get('token');
  const isResetPasswordPage = window.location.pathname === '/reset-password' && resetToken;

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoginError('');
    try {
      await login(email, password);
    } catch (error: any) {
      setLoginError(error.message || 'Error al iniciar sesión');
    }
  };

  const handleTabChange = (tab: string) => {
    if (hasPermission(tab)) {
      setActiveTab(tab);
    }
  };

  const renderContent = () => {
    if (!hasPermission(activeTab)) {
      return (
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Acceso Denegado</h2>
            <p className="text-gray-600">No tienes permisos para acceder a esta sección</p>
          </div>
        </div>
      );
    }

    switch (activeTab) {
      case 'dashboard':
        return <Dashboard />;
      case 'products':
        return <ProductList />;
      case 'customers':
        return <CustomerList />;
      case 'sales':
        return <SalesList />;
      case 'new-sale':
        return <NewSale />;
      case 'categories':
        return <CategoryList />;
      case 'reports':
        return <Reports />;
      case 'analytics':
        return <Analytics />;
      case 'settings':
        return <Settings />;
      case 'users':
        return <UserList />;
      default:
        return <Dashboard />;
    }
  };

  // Página de reset password
  if (isResetPasswordPage) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-black flex items-center justify-center p-4">
        <div className="max-w-md w-full space-y-8">
          <div className="text-center">
            <div className="mx-auto w-20 h-20 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-2xl flex items-center justify-center mb-6">
              <Store className="w-10 h-10 text-black" />
            </div>
            <h1 className="text-4xl font-bold bg-gradient-to-r from-yellow-400 to-yellow-300 bg-clip-text text-transparent">
              DPattyModa
            </h1>
            <p className="text-gray-400 mt-2">Sistema de Gestión de Negocio</p>
          </div>

          <ResetPassword 
            token={resetToken!} 
            onSuccess={() => {
              window.location.href = '/';
            }}
          />

          <div className="text-center text-gray-500 text-sm">
            <p>&copy; 2024 DPattyModa. Todos los derechos reservados.</p>
          </div>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-black flex items-center justify-center p-4">
        <div className="max-w-md w-full space-y-8">
          <div className="text-center">
            <div className="mx-auto w-20 h-20 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-2xl flex items-center justify-center mb-6">
              <Store className="w-10 h-10 text-black" />
            </div>
            <h1 className="text-4xl font-bold bg-gradient-to-r from-yellow-400 to-yellow-300 bg-clip-text text-transparent">
              DPattyModa
            </h1>
            <p className="text-gray-400 mt-2">Sistema de Gestión de Negocio</p>
            <p className="text-sm text-gray-500">Pampa Hermosa, Loreto - Perú</p>
          </div>

          {showForgotPassword ? (
            <ForgotPassword onBackToLogin={() => setShowForgotPassword(false)} />
          ) : (
            <div className="bg-white rounded-2xl shadow-2xl p-8">
              <form onSubmit={handleLogin} className="space-y-6">
                <div>
                  <h2 className="text-2xl font-bold text-gray-900 text-center mb-6">
                    Iniciar Sesión
                  </h2>
                </div>

                {loginError && (
                  <div className="bg-red-50 border border-red-200 rounded-lg p-3">
                    <p className="text-red-600 text-sm">{loginError}</p>
                  </div>
                )}

                <Input
                  label="Email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  leftIcon={<Mail className="w-4 h-4 text-gray-400" />}
                  required
                />

                <Input
                  label="Contraseña"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  leftIcon={<Lock className="w-4 h-4 text-gray-400" />}
                  showPasswordToggle={true}
                  required
                />

                <div className="flex items-center justify-between">
                  <label className="flex items-center">
                    <input type="checkbox" className="rounded border-gray-300" />
                    <span className="ml-2 text-sm text-gray-600">Recordarme</span>
                  </label>
                  <button
                    type="button"
                    onClick={() => setShowForgotPassword(true)}
                    className="text-sm text-yellow-600 hover:text-yellow-500 font-medium"
                  >
                    ¿Olvidaste tu contraseña?
                  </button>
                </div>

                <Button
                  type="submit"
                  className="w-full"
                  isLoading={isLoading}
                  size="lg"
                >
                  Ingresar al Sistema
                </Button>
              </form>

              <div className="mt-6 text-center">
                <p className="text-sm text-gray-500">
                  Contacta al administrador para obtener acceso
                </p>
              </div>
            </div>
          )}

          <div className="text-center text-gray-500 text-sm">
            <p>&copy; 2024 DPattyModa. Todos los derechos reservados.</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar
        activeTab={activeTab}
        onTabChange={handleTabChange}
        isCollapsed={sidebarCollapsed}
        userRole={user?.role || 'VENDEDOR'}
        hasPermission={hasPermission}
      />
      
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header
          onToggleSidebar={() => setSidebarCollapsed(!sidebarCollapsed)}
          onLogout={logout}
          user={user}
        />
        
        <main className="flex-1 overflow-auto p-6">
          {renderContent()}
        </main>
      </div>
    </div>
  );
}

export default App;