// Header principal con información del usuario
import { useState } from 'react';
import { Bell, Search, Menu, LogOut, User, Settings, Sun, Moon, Zap, Calculator, FileText, Download } from 'lucide-react';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Modal } from '../ui/Modal';

interface HeaderProps {
  onToggleSidebar: () => void;
  onLogout: () => void;
  user: any;
}

export function Header({ onToggleSidebar, onLogout, user }: HeaderProps) {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [isCalculatorOpen, setIsCalculatorOpen] = useState(false);
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [calculatorValue, setCalculatorValue] = useState('0');
  const [calculatorOperation, setCalculatorOperation] = useState('');
  const [calculatorPrevValue, setCalculatorPrevValue] = useState('');
  const [notifications] = useState([
    { id: 1, message: 'Stock bajo en productos', type: 'warning', time: '5 min' },
    { id: 2, message: 'Nueva venta registrada', type: 'success', time: '10 min' },
  ]);

  const roleDisplayNames: Record<string, string> = {
    SUPER_ADMIN: 'Super Administrador',
    ADMIN: 'Administrador',
    MANAGER: 'Gerente',
    VENDEDOR: 'Vendedor',
    CAJERO: 'Cajero',
    INVENTARIO: 'Encargado de Inventario'
  };

  const handleCalculatorClick = (value: string) => {
    if (value === 'C') {
      setCalculatorValue('0');
      setCalculatorOperation('');
      setCalculatorPrevValue('');
    } else if (value === '=') {
      if (calculatorOperation && calculatorPrevValue) {
        const prev = parseFloat(calculatorPrevValue);
        const current = parseFloat(calculatorValue);
        let result = 0;
        
        switch (calculatorOperation) {
          case '+': result = prev + current; break;
          case '-': result = prev - current; break;
          case '*': result = prev * current; break;
          case '/': result = prev / current; break;
        }
        
        setCalculatorValue(result.toString());
        setCalculatorOperation('');
        setCalculatorPrevValue('');
      }
    } else if (['+', '-', '*', '/'].includes(value)) {
      setCalculatorOperation(value);
      setCalculatorPrevValue(calculatorValue);
      setCalculatorValue('0');
    } else {
      setCalculatorValue(calculatorValue === '0' ? value : calculatorValue + value);
    }
  };

  const handleNewSale = () => {
    window.location.hash = '#new-sale';
    window.location.reload();
  };

  const handleExportData = () => {
    // Generar reporte de exportación
    const data = {
      fecha: new Date().toISOString(),
      usuario: user?.firstName + ' ' + user?.lastName,
      tipo: 'Exportación completa'
    };
    
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `dpattymoda-export-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };
  return (
    <>
      <header className="bg-white border-b border-gray-200 shadow-sm">
      <div className="px-6 py-4">
        <div className="flex items-center justify-between">
          {/* Left Section */}
          <div className="flex items-center space-x-6">
            <Button
              variant="ghost"
              size="sm"
              onClick={onToggleSidebar}
              className="lg:hidden hover:bg-gray-100"
            >
              <Menu className="w-5 h-5" />
            </Button>
            
            {/* Enhanced Search */}
            <div className="hidden md:block relative">
              <Input
                placeholder="Buscar productos, clientes, ventas..."
                leftIcon={<Search className="w-4 h-4 text-gray-400" />}
                className="w-96 bg-gray-50 border-gray-200 focus:bg-white focus:ring-2 focus:ring-yellow-500/20 focus:border-yellow-500 transition-all duration-200"
              />
              <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                <kbd className="px-2 py-1 text-xs bg-gray-200 rounded text-gray-500">⌘K</kbd>
              </div>
            </div>
          </div>

          {/* Right Section */}
          <div className="flex items-center space-x-4">
            {/* Quick Actions */}
            <div className="hidden lg:flex items-center space-x-2">
              <Button variant="ghost" size="sm" className="text-gray-600 hover:text-yellow-600">
                <Zap className="w-4 h-4 mr-1" />
                Nueva Venta
              </Button>
              
              <Button 
                variant="ghost" 
                size="sm" 
                className="text-gray-600 hover:text-blue-600"
                onClick={() => setIsCalculatorOpen(true)}
              >
                <Calculator className="w-4 h-4 mr-1" />
                Calculadora
              </Button>
              
              <Button 
                variant="ghost" 
                size="sm" 
                className="text-gray-600 hover:text-green-600"
                onClick={handleExportData}
              >
                <Download className="w-4 h-4 mr-1" />
                Exportar
              </Button>
            </div>

            {/* Theme Toggle */}
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setIsDarkMode(!isDarkMode)}
              className="text-gray-600 hover:text-yellow-600"
            >
              {isDarkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
            </Button>

            {/* Notifications */}
            <div className="relative">
              <Button 
                variant="ghost" 
                size="sm" 
                className="relative text-gray-600 hover:text-yellow-600"
                onClick={() => setIsNotificationsOpen(!isNotificationsOpen)}
              >
                <Bell className="w-5 h-5" />
                {notifications.length > 0 && (
                  <span className="absolute -top-1 -right-1 w-5 h-5 bg-gradient-to-r from-red-500 to-red-600 rounded-full text-xs flex items-center justify-center text-white font-bold animate-pulse">
                    {notifications.length}
                  </span>
                )}
              </Button>
              
              {/* Dropdown de notificaciones */}
              {isNotificationsOpen && (
                <div className="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg border border-gray-200 z-50">
                  <div className="p-4 border-b border-gray-200">
                    <h3 className="font-semibold text-gray-900">Notificaciones</h3>
                  </div>
                  <div className="max-h-64 overflow-y-auto">
                    {notifications.map((notification) => (
                      <div key={notification.id} className="p-4 border-b border-gray-100 hover:bg-gray-50">
                        <div className="flex items-start space-x-3">
                          <div className={`w-2 h-2 rounded-full mt-2 ${
                            notification.type === 'warning' ? 'bg-yellow-500' : 'bg-green-500'
                          }`}></div>
                          <div className="flex-1">
                            <p className="text-sm text-gray-900">{notification.message}</p>
                            <p className="text-xs text-gray-500 mt-1">{notification.time}</p>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                  <div className="p-4 border-t border-gray-200">
                    <Button variant="ghost" size="sm" className="w-full">
                      Ver todas las notificaciones
                    </Button>
                  </div>
                </div>
              )}
            </div>

            {/* User Profile */}
            <div className="flex items-center space-x-3">
              <div className="hidden sm:block text-right">
                <p className="text-sm font-medium text-gray-900">
                  {user?.firstName} {user?.lastName}
                </p>
                <p className="text-xs text-gray-500">
                  {roleDisplayNames[user?.role] || user?.role}
                </p>
              </div>
              
              <div className="relative">
                <div className="w-10 h-10 bg-gradient-to-r from-yellow-400 via-yellow-500 to-yellow-600 rounded-full flex items-center justify-center shadow-lg ring-2 ring-yellow-200">
                  <User className="w-5 h-5 text-black" />
                </div>
                <div className="absolute -bottom-1 -right-1 w-4 h-4 bg-green-500 rounded-full border-2 border-white"></div>
              </div>
            </div>

            {/* Settings & Logout */}
            <div className="flex items-center space-x-1">
              <Button variant="ghost" size="sm" className="text-gray-600 hover:text-yellow-600">
                <Settings className="w-5 h-5" />
              </Button>
              
              <Button 
                variant="ghost" 
                size="sm" 
                onClick={onLogout}
                className="text-gray-600 hover:text-red-600"
              >
                <LogOut className="w-5 h-5" />
              </Button>
            </div>
          </div>
        </div>

        {/* Quick Stats Bar */}
        <div className="mt-4 flex items-center justify-between bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-3">
          <div className="flex items-center space-x-6">
            <div className="flex items-center space-x-2">
              <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
              <span className="text-sm text-gray-600">Sistema Online</span>
            </div>
            <div className="text-sm text-gray-600">
              Usuario: <span className="font-bold text-yellow-600">{user?.firstName}</span>
            </div>
            <div className="text-sm text-gray-600">
              Rol: <span className="font-bold text-blue-600">{roleDisplayNames[user?.role] || user?.role}</span>
            </div>
          </div>
          
          <div className="text-xs text-gray-500">
            Última actualización: {new Date().toLocaleTimeString('es-PE')}
          </div>
        </div>
      </div>
      </header>

      {/* Modal Calculadora */}
      <Modal
        isOpen={isCalculatorOpen}
        onClose={() => setIsCalculatorOpen(false)}
        title="🧮 Calculadora"
        size="sm"
      >
        <div className="space-y-4">
          <div className="bg-gray-100 p-4 rounded-lg">
            <div className="text-right text-2xl font-mono font-bold text-gray-900">
              {calculatorValue}
            </div>
          </div>
          
          <div className="grid grid-cols-4 gap-2">
            {['C', '/', '*', '-', '7', '8', '9', '+', '4', '5', '6', '+', '1', '2', '3', '=', '0', '.', '='].map((btn, index) => (
              <Button
                key={index}
                variant={['C'].includes(btn) ? 'danger' : ['=', '+', '-', '*', '/'].includes(btn) ? 'primary' : 'outline'}
                size="sm"
                onClick={() => handleCalculatorClick(btn)}
                className={`h-12 ${btn === '0' ? 'col-span-2' : ''} ${btn === '=' && index > 15 ? 'row-span-2' : ''}`}
              >
                {btn}
              </Button>
            ))}
          </div>
        </div>
      </Modal>
    </>
  );
}