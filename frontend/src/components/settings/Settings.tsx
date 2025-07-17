// Módulo de configuración
import { useState } from 'react';
import { Save, Store, User, Bell, Shield, Palette, Database, Printer, Calculator, Download, Upload, FileText, HardDrive } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { TaxSettings } from './TaxSettings';

export function Settings() {
  const [activeTab, setActiveTab] = useState('business');
  const [businessData, setBusinessData] = useState({
    name: 'DPattyModa',
    address: 'Pampa Hermosa, Loreto, Perú',
    phone: '+51 965 123 456',
    email: 'info@dpattymoda.com',
    ruc: '20123456789',
    description: 'Tienda de ropa moderna y elegante',
  });

  const settingsTabs = [
    { id: 'business', name: 'Negocio', icon: Store },
    { id: 'profile', name: 'Perfil', icon: User },
    { id: 'taxes', name: 'Impuestos', icon: Calculator },
    { id: 'notifications', name: 'Notificaciones', icon: Bell },
    { id: 'security', name: 'Seguridad', icon: Shield },
    { id: 'appearance', name: 'Apariencia', icon: Palette },
    { id: 'data', name: 'Datos', icon: Database },
    { id: 'printing', name: 'Impresión', icon: Printer },
  ];

  const handleExportData = () => {
    const exportData = {
      fecha: new Date().toISOString(),
      tienda: businessData,
      configuracion: {
        version: '1.0.0',
        exportadoPor: 'DPattyModa System'
      }
    };
    
    const blob = new Blob([JSON.stringify(exportData, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `dpattymoda-backup-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleImportData = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.json';
    input.onchange = (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const data = JSON.parse(e.target?.result as string);
            console.log('Datos importados:', data);
            alert('Datos importados exitosamente');
          } catch (error) {
            alert('Error al importar datos: Archivo inválido');
          }
        };
        reader.readAsText(file);
      }
    };
    input.click();
  };

  const handlePrintTest = () => {
    const printWindow = window.open('', '_blank');
    if (printWindow) {
      printWindow.document.write(`
        <html>
          <head>
            <title>Prueba de Impresión - DPattyModa</title>
            <style>
              body { font-family: Arial, sans-serif; padding: 20px; }
              .header { text-align: center; margin-bottom: 20px; }
              .content { margin: 20px 0; }
            </style>
          </head>
          <body>
            <div class="header">
              <h1>DPattyModa</h1>
              <p>Pampa Hermosa, Loreto - Perú</p>
              <p>+51 965 123 456</p>
            </div>
            <div class="content">
              <h2>Prueba de Impresión</h2>
              <p>Fecha: ${new Date().toLocaleDateString('es-PE')}</p>
              <p>Hora: ${new Date().toLocaleTimeString('es-PE')}</p>
              <p>Esta es una prueba de impresión del sistema DPattyModa.</p>
              <p>Si puedes ver este texto correctamente, la impresora está funcionando bien.</p>
            </div>
          </body>
        </html>
      `);
      printWindow.document.close();
      printWindow.print();
    }
  };
  const renderBusinessSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Información del Negocio</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Nombre del Negocio"
              value={businessData.name}
              onChange={(e) => setBusinessData({...businessData, name: e.target.value})}
            />
            <Input
              label="RUC"
              value={businessData.ruc}
              onChange={(e) => setBusinessData({...businessData, ruc: e.target.value})}
            />
          </div>
          
          <Input
            label="Dirección"
            value={businessData.address}
            onChange={(e) => setBusinessData({...businessData, address: e.target.value})}
          />
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Teléfono"
              value={businessData.phone}
              onChange={(e) => setBusinessData({...businessData, phone: e.target.value})}
            />
            <Input
              label="Email"
              type="email"
              value={businessData.email}
              onChange={(e) => setBusinessData({...businessData, email: e.target.value})}
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descripción
            </label>
            <textarea
              value={businessData.description}
              onChange={(e) => setBusinessData({...businessData, description: e.target.value})}
              rows={3}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
            />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Configuración de Impuestos</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input label="IGV (%)" defaultValue="18" type="number" />
            <Input label="Moneda" defaultValue="PEN" disabled />
          </div>
          
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <p className="text-sm text-yellow-800">
              <strong>Nota:</strong> La configuración detallada de impuestos se encuentra en la pestaña "Impuestos". 
              Allí puedes activar/desactivar el IGV y configurar el porcentaje según tu región.
            </p>
          </div>
        </CardContent>
      </Card>
    </div>
  );

  const renderProfileSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Información Personal</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center space-x-6">
            <div className="w-20 h-20 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-full flex items-center justify-center text-black font-bold text-2xl">
              A
            </div>
            <div>
              <Button variant="outline" size="sm">Cambiar Foto</Button>
              <p className="text-xs text-gray-500 mt-1">JPG, PNG hasta 2MB</p>
            </div>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input label="Nombre" defaultValue="Admin" />
            <Input label="Apellido" defaultValue="DPattyModa" />
          </div>
          
          <Input label="Email" type="email" defaultValue="admin@dpattymoda.com" />
          <Input label="Teléfono" defaultValue="+51 965 123 456" />
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Cambiar Contraseña</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <Input label="Contraseña Actual" type="password" />
          <Input label="Nueva Contraseña" type="password" />
          <Input label="Confirmar Contraseña" type="password" />
        </CardContent>
      </Card>
    </div>
  );

  const renderNotificationSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Notificaciones del Sistema</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {[
            { id: 'lowStock', label: 'Stock bajo', description: 'Cuando un producto tenga stock menor al mínimo' },
            { id: 'newSale', label: 'Nueva venta', description: 'Notificar cuando se registre una nueva venta' },
            { id: 'newCustomer', label: 'Nuevo cliente', description: 'Cuando se registre un nuevo cliente' },
            { id: 'dailyReport', label: 'Reporte diario', description: 'Resumen diario de ventas y métricas' },
          ].map((notification) => (
            <div key={notification.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
              <div>
                <h4 className="font-medium text-gray-900">{notification.label}</h4>
                <p className="text-sm text-gray-500">{notification.description}</p>
              </div>
              <input type="checkbox" className="rounded" defaultChecked />
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  );

  const renderSecuritySettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Configuración de Seguridad</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <div>
              <h4 className="font-medium text-gray-900">Autenticación de dos factores</h4>
              <p className="text-sm text-gray-500">Añade una capa extra de seguridad</p>
            </div>
            <Button variant="outline" size="sm">Configurar</Button>
          </div>
          
          <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <div>
              <h4 className="font-medium text-gray-900">Sesiones activas</h4>
              <p className="text-sm text-gray-500">Gestiona tus sesiones activas</p>
            </div>
            <Button variant="outline" size="sm">Ver Sesiones</Button>
          </div>
          
          <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
            <div>
              <h4 className="font-medium text-gray-900">Backup automático</h4>
              <p className="text-sm text-gray-500">Respaldo automático de datos</p>
            </div>
            <input type="checkbox" className="rounded" defaultChecked />
          </div>
        </CardContent>
      </Card>
    </div>
  );

  const renderAppearanceSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>Personalización</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tema
            </label>
            <div className="grid grid-cols-3 gap-3">
              {['Claro', 'Oscuro', 'Automático'].map((theme) => (
                <button
                  key={theme}
                  className="p-3 border-2 border-gray-300 rounded-lg hover:border-yellow-500 transition-colors"
                >
                  {theme}
                </button>
              ))}
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Color Principal
            </label>
            <div className="flex space-x-2">
              {['#FFD700', '#3B82F6', '#10B981', '#F59E0B', '#EF4444'].map((color) => (
                <button
                  key={color}
                  className="w-8 h-8 rounded-full border-2 border-gray-300"
                  style={{ backgroundColor: color }}
                />
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );

  const renderDataSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Database className="w-5 h-5 mr-2" />
            Gestión de Datos
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="p-4 bg-blue-50 rounded-lg">
              <h4 className="font-medium text-blue-900 mb-2">Exportar Datos</h4>
              <p className="text-sm text-blue-700 mb-4">
                Descarga una copia de seguridad de todos tus datos
              </p>
              <Button 
                variant="outline" 
                size="sm" 
                leftIcon={<Download className="w-4 h-4" />}
                onClick={handleExportData}
              >
                Exportar Backup
              </Button>
            </div>
            
            <div className="p-4 bg-green-50 rounded-lg">
              <h4 className="font-medium text-green-900 mb-2">Importar Datos</h4>
              <p className="text-sm text-green-700 mb-4">
                Restaura datos desde un archivo de respaldo
              </p>
              <Button 
                variant="outline" 
                size="sm" 
                leftIcon={<Upload className="w-4 h-4" />}
                onClick={handleImportData}
              >
                Importar Backup
              </Button>
            </div>
          </div>
          
          <div className="p-4 bg-yellow-50 border border-yellow-200 rounded-lg">
            <div className="flex items-start space-x-3">
              <HardDrive className="w-5 h-5 text-yellow-600 mt-0.5" />
              <div>
                <h4 className="font-medium text-yellow-800">Información de la Base de Datos</h4>
                <p className="text-sm text-yellow-700 mt-1">
                  Base de datos: MySQL - pattymoda_mejorada<br/>
                  Última sincronización: {new Date().toLocaleString('es-PE')}<br/>
                  Estado: Conectado y funcionando
                </p>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );

  const renderPrintingSettings = () => (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Printer className="w-5 h-5 mr-2" />
            Configuración de Impresión
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Impresora Predeterminada
              </label>
              <select className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500">
                <option>Impresora del Sistema</option>
                <option>Impresora Térmica</option>
                <option>PDF Virtual</option>
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Tamaño de Papel
              </label>
              <select className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500">
                <option>A4</option>
                <option>Ticket 80mm</option>
                <option>Ticket 58mm</option>
              </select>
            </div>
          </div>
          
          <div className="space-y-4">
            <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
              <div>
                <h4 className="font-medium text-gray-900">Imprimir automáticamente</h4>
                <p className="text-sm text-gray-500">Imprime boletas automáticamente al completar venta</p>
              </div>
              <input type="checkbox" className="rounded" defaultChecked />
            </div>
            
            <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
              <div>
                <h4 className="font-medium text-gray-900">Incluir logo en boletas</h4>
                <p className="text-sm text-gray-500">Agrega el logo de DPattyModa en las boletas</p>
              </div>
              <input type="checkbox" className="rounded" defaultChecked />
            </div>
          </div>
          
          <div className="pt-4 border-t border-gray-200">
            <Button 
              variant="outline" 
              leftIcon={<FileText className="w-4 h-4" />}
              onClick={handlePrintTest}
            >
              Imprimir Página de Prueba
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
  const renderContent = () => {
    switch (activeTab) {
      case 'business': return renderBusinessSettings();
      case 'profile': return renderProfileSettings();
      case 'taxes': return <TaxSettings />;
      case 'notifications': return renderNotificationSettings();
      case 'security': return renderSecuritySettings();
      case 'appearance': return renderAppearanceSettings();
      case 'data': return renderDataSettings();
      case 'printing': return renderPrintingSettings();
      default: return renderBusinessSettings();
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Configuración</h1>
          <p className="text-gray-600 mt-1">Personaliza tu sistema DPattyModa</p>
        </div>
        <Button leftIcon={<Save className="w-4 h-4" />}>
          Guardar Cambios
        </Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Sidebar de Configuración */}
        <Card className="lg:col-span-1">
          <CardContent className="p-4">
            <nav className="space-y-2">
              {settingsTabs.map((tab) => {
                const Icon = tab.icon;
                return (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id)}
                    className={`w-full flex items-center space-x-3 px-3 py-2 rounded-lg transition-colors ${
                      activeTab === tab.id
                        ? 'bg-gradient-to-r from-yellow-400 to-yellow-500 text-black'
                        : 'text-gray-700 hover:bg-gray-100'
                    }`}
                  >
                    <Icon className="w-5 h-5" />
                    <span className="font-medium">{tab.name}</span>
                  </button>
                );
              })}
            </nav>
          </CardContent>
        </Card>

        {/* Contenido de Configuración */}
        <div className="lg:col-span-3">
          {renderContent()}
        </div>
      </div>
    </div>
  );
}