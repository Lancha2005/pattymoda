// Componente para configuración de impuestos
import { useState, useEffect } from 'react';
import { Save, Plus, Edit, Trash2, Calculator, ToggleLeft, ToggleRight, AlertCircle } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Modal } from '../ui/Modal';
import { TaxService } from '../../services/taxService';

interface TaxConfig {
  id: string;
  nombre: string;
  porcentaje: number;
  activo: boolean;
  aplicarPorDefecto: boolean;
  descripcion?: string;
}

export function TaxSettings() {
  const [taxes, setTaxes] = useState<TaxConfig[]>([]);
  const [igvConfig, setIgvConfig] = useState<TaxConfig | null>(null);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedTax, setSelectedTax] = useState<TaxConfig | null>(null);
  const [igvPercentage, setIgvPercentage] = useState(18);
  const [igvActive, setIgvActive] = useState(true);

  useEffect(() => {
    loadTaxes();
    loadIGVConfig();
  }, []);

  const loadTaxes = async () => {
    try {
      const response = await TaxService.getAllTaxes();
      setTaxes(response.data || []);
    } catch (error) {
      console.error('Error loading taxes:', error);
    }
  };

  const loadIGVConfig = async () => {
    try {
      const response = await TaxService.getIGVConfig();
      if (response.data) {
        setIgvConfig(response.data);
        setIgvPercentage(response.data.porcentaje);
        setIgvActive(response.data.activo);
      }
    } catch (error) {
      console.error('Error loading IGV config:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleIGV = async () => {
    try {
      const newState = !igvActive;
      await TaxService.toggleIGV(newState);
      setIgvActive(newState);
      await loadIGVConfig();
    } catch (error) {
      console.error('Error toggling IGV:', error);
    }
  };

  const handleUpdateIGVPercentage = async () => {
    try {
      await TaxService.updateIGVPercentage(igvPercentage);
      await loadIGVConfig();
    } catch (error) {
      console.error('Error updating IGV percentage:', error);
    }
  };

  const handleSaveTax = async (taxData: any) => {
    try {
      if (selectedTax) {
        await TaxService.updateTax(selectedTax.id, taxData);
      } else {
        await TaxService.createTax(taxData);
      }
      setIsModalOpen(false);
      await loadTaxes();
    } catch (error) {
      console.error('Error saving tax:', error);
    }
  };

  const handleDeleteTax = async (id: string) => {
    if (window.confirm('¿Estás seguro de que deseas eliminar este impuesto?')) {
      try {
        await TaxService.deleteTax(id);
        await loadTaxes();
      } catch (error) {
        console.error('Error deleting tax:', error);
      }
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500"></div>
        <span className="ml-3 text-gray-600">Cargando configuración de impuestos...</span>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Configuración de Impuestos</h2>
          <p className="text-gray-600 mt-1">Gestiona los impuestos aplicables a las ventas</p>
        </div>
        <Button
          onClick={() => {
            setSelectedTax(null);
            setIsModalOpen(true);
          }}
          leftIcon={<Plus className="w-4 h-4" />}
        >
          Nuevo Impuesto
        </Button>
      </div>

      {/* Configuración IGV */}
      <Card className="border-l-4 border-l-blue-500">
        <CardHeader>
          <CardTitle className="flex items-center text-blue-800">
            <Calculator className="w-5 h-5 mr-2" />
            Configuración del IGV (Impuesto General a las Ventas)
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-6">
            {/* Estado del IGV */}
            <div className="flex items-center justify-between p-4 bg-blue-50 rounded-lg">
              <div>
                <h4 className="font-medium text-blue-900">Estado del IGV</h4>
                <p className="text-sm text-blue-700">
                  {igvActive ? 'El IGV está activo y se aplicará a las ventas' : 'El IGV está desactivado'}
                </p>
              </div>
              <button
                onClick={handleToggleIGV}
                className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors ${
                  igvActive 
                    ? 'bg-green-100 text-green-800 hover:bg-green-200' 
                    : 'bg-red-100 text-red-800 hover:bg-red-200'
                }`}
              >
                {igvActive ? (
                  <>
                    <ToggleRight className="w-5 h-5" />
                    <span>Activado</span>
                  </>
                ) : (
                  <>
                    <ToggleLeft className="w-5 h-5" />
                    <span>Desactivado</span>
                  </>
                )}
              </button>
            </div>

            {/* Porcentaje del IGV */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Porcentaje del IGV (%)
                </label>
                <div className="flex space-x-2">
                  <Input
                    type="number"
                    step="0.01"
                    min="0"
                    max="100"
                    value={igvPercentage}
                    onChange={(e) => setIgvPercentage(Number(e.target.value))}
                    className="flex-1"
                  />
                  <Button onClick={handleUpdateIGVPercentage} size="sm">
                    <Save className="w-4 h-4" />
                  </Button>
                </div>
                <p className="text-xs text-gray-500 mt-1">
                  Porcentaje estándar en Perú: 18%. En la selva puede ser 0% o diferente.
                </p>
              </div>

              <div className="p-4 bg-yellow-50 rounded-lg">
                <div className="flex items-start space-x-2">
                  <AlertCircle className="w-5 h-5 text-yellow-600 mt-0.5" />
                  <div>
                    <h5 className="font-medium text-yellow-800">Información Importante</h5>
                    <p className="text-sm text-yellow-700 mt-1">
                      En la región de la selva peruana, muchas veces el IGV puede estar exonerado o tener 
                      un porcentaje diferente. Configura según tu ubicación y normativa local.
                    </p>
                  </div>
                </div>
              </div>
            </div>

            {/* Vista previa del cálculo */}
            {igvActive && (
              <div className="p-4 bg-gray-50 rounded-lg">
                <h5 className="font-medium text-gray-900 mb-2">Vista Previa del Cálculo</h5>
                <div className="grid grid-cols-3 gap-4 text-sm">
                  <div>
                    <span className="text-gray-600">Subtotal:</span>
                    <p className="font-bold">S/ 100.00</p>
                  </div>
                  <div>
                    <span className="text-gray-600">IGV ({igvPercentage}%):</span>
                    <p className="font-bold text-blue-600">S/ {(100 * igvPercentage / 100).toFixed(2)}</p>
                  </div>
                  <div>
                    <span className="text-gray-600">Total:</span>
                    <p className="font-bold text-green-600">S/ {(100 + (100 * igvPercentage / 100)).toFixed(2)}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </CardContent>
      </Card>

      {/* Lista de Impuestos */}
      <Card>
        <CardHeader>
          <CardTitle>Otros Impuestos Configurados</CardTitle>
        </CardHeader>
        <CardContent>
          {taxes.length === 0 ? (
            <div className="text-center py-8 text-gray-500">
              <Calculator className="w-12 h-12 mx-auto mb-4 opacity-50" />
              <p>No hay impuestos adicionales configurados</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nombre
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Porcentaje
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Por Defecto
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {taxes.map((tax) => (
                    <tr key={tax.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div>
                          <div className="text-sm font-medium text-gray-900">{tax.nombre}</div>
                          {tax.descripcion && (
                            <div className="text-sm text-gray-500">{tax.descripcion}</div>
                          )}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className="text-sm font-bold text-blue-600">{tax.porcentaje}%</span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                          tax.activo 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {tax.activo ? 'Activo' : 'Inactivo'}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {tax.aplicarPorDefecto && (
                          <span className="px-2 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs font-medium">
                            Por Defecto
                          </span>
                        )}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                        <div className="flex space-x-2">
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => {
                              setSelectedTax(tax);
                              setIsModalOpen(true);
                            }}
                          >
                            <Edit className="w-4 h-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => handleDeleteTax(tax.id)}
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Modal para crear/editar impuesto */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={selectedTax ? 'Editar Impuesto' : 'Nuevo Impuesto'}
        size="md"
      >
        <div className="space-y-4">
          <Input
            label="Nombre del Impuesto"
            defaultValue={selectedTax?.nombre}
            id="nombre"
            placeholder="Ej: ISC, Impuesto Municipal"
          />
          
          <Input
            label="Porcentaje (%)"
            type="number"
            step="0.01"
            min="0"
            max="100"
            defaultValue={selectedTax?.porcentaje}
            id="porcentaje"
            placeholder="0.00"
          />
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descripción
            </label>
            <textarea
              id="descripcion"
              defaultValue={selectedTax?.descripcion}
              rows={3}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
              placeholder="Descripción del impuesto..."
            />
          </div>
          
          <div className="flex items-center space-x-4">
            <label className="flex items-center">
              <input
                type="checkbox"
                id="activo"
                defaultChecked={selectedTax?.activo ?? true}
                className="rounded border-gray-300"
              />
              <span className="ml-2 text-sm text-gray-700">Activo</span>
            </label>
            
            <label className="flex items-center">
              <input
                type="checkbox"
                id="aplicarPorDefecto"
                defaultChecked={selectedTax?.aplicarPorDefecto ?? false}
                className="rounded border-gray-300"
              />
              <span className="ml-2 text-sm text-gray-700">Aplicar por defecto</span>
            </label>
          </div>
          
          <div className="flex justify-end space-x-3 pt-6 border-t">
            <Button variant="ghost" onClick={() => setIsModalOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={() => {
              const formData = {
                nombre: (document.getElementById('nombre') as HTMLInputElement)?.value,
                porcentaje: Number((document.getElementById('porcentaje') as HTMLInputElement)?.value),
                descripcion: (document.getElementById('descripcion') as HTMLTextAreaElement)?.value,
                activo: (document.getElementById('activo') as HTMLInputElement)?.checked,
                aplicarPorDefecto: (document.getElementById('aplicarPorDefecto') as HTMLInputElement)?.checked,
              };
              handleSaveTax(formData);
            }}>
              {selectedTax ? 'Actualizar Impuesto' : 'Crear Impuesto'}
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}