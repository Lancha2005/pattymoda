// Formulario de productos
import { useState } from 'react';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Card } from '../ui/Card';
import { Upload, X } from 'lucide-react';

interface ProductFormProps {
  product?: any;
  onSubmit: (data: any) => void;
  onCancel: () => void;
}

export function ProductForm({ product, onSubmit, onCancel }: ProductFormProps) {
  const [formData, setFormData] = useState({
    nombre: product?.nombre || '',
    sku: product?.sku || '',
    descripcion: product?.descripcion || '',
    categoria: product?.categoria || { id: '' },
    marca: product?.marca || '',
    precio: product?.precio || '',
    costo: product?.costo || '',
    stock: product?.stock || '',
    stockMinimo: product?.stockMinimo || '',
    imagen: product?.imagen || '',
    activo: product?.activo ?? true,
  });

  // Categorías que coinciden con tu base de datos
  const categories = [
    { id: 1, nombre: 'Ropa Femenina' },
    { id: 2, nombre: 'Ropa Masculina' },
    { id: 3, nombre: 'Accesorios' },
    { id: 4, nombre: 'Calzado' },
    { id: 5, nombre: 'Blusas' },
    { id: 6, nombre: 'Vestidos' },
    { id: 7, nombre: 'Pantalones Femeninos' },
    { id: 8, nombre: 'Faldas' },
    { id: 9, nombre: 'Camisas Masculinas' },
    { id: 10, nombre: 'Pantalones Masculinos' },
    { id: 11, nombre: 'Polos' },
    { id: 12, nombre: 'Bolsos' },
    { id: 13, nombre: 'Cinturones' },
    { id: 14, nombre: 'Joyas' },
    { id: 15, nombre: 'Zapatos Formales' },
    { id: 16, nombre: 'Zapatillas' },
    { id: 17, nombre: 'Sandalias' },
  ];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Información Básica */}
        <Card className="p-4">
          <h3 className="font-semibold text-gray-900 mb-4">Información Básica</h3>
          <div className="space-y-4">
            <Input
              label="Nombre del Producto"
              value={formData.nombre}
              onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
              required
            />
            
            <Input
              label="SKU"
              value={formData.sku}
              onChange={(e) => setFormData({ ...formData, sku: e.target.value })}
              required
            />
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Categoría
              </label>
              <select
                value={formData.categoria.id}
                onChange={(e) => setFormData({ ...formData, categoria: { id: e.target.value } })}
                className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
                required
              >
                <option value="">Seleccionar categoría</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>{cat.nombre}</option>
                ))}
              </select>
              </select>
            </div>
            
            <Input
              label="Marca"
              value={formData.marca}
              onChange={(e) => setFormData({ ...formData, marca: e.target.value })}
            />
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Descripción
              </label>
              <textarea
                value={formData.descripcion}
                onChange={(e) => setFormData({ ...formData, descripcion: e.target.value })}
                rows={3}
                className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
                placeholder="Descripción del producto..."
              />
            </div>
          </div>
        </Card>

        {/* Precios e Inventario */}
        <Card className="p-4">
          <h3 className="font-semibold text-gray-900 mb-4">Precios e Inventario</h3>
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <Input
                label="Precio de Venta (S/)"
                type="number"
                step="0.01"
                value={formData.precio}
                onChange={(e) => setFormData({ ...formData, precio: e.target.value })}
                required
              />
              
              <Input
                label="Costo (S/)"
                type="number"
                step="0.01"
                value={formData.costo}
                onChange={(e) => setFormData({ ...formData, costo: e.target.value })}
                required
              />
            </div>
            
            <div className="grid grid-cols-2 gap-4">
              <Input
                label="Stock Actual"
                type="number"
                value={formData.stock}
                onChange={(e) => setFormData({ ...formData, stock: e.target.value })}
                required
              />
              
              <Input
                label="Stock Mínimo"
                type="number"
                value={formData.stockMinimo}
                onChange={(e) => setFormData({ ...formData, stockMinimo: e.target.value })}
                required
              />
            </div>
            
            {formData.precio && formData.costo && (
              <div className="bg-green-50 p-3 rounded-lg">
                <p className="text-sm text-green-800">
                  <span className="font-medium">Margen:</span> S/ {(parseFloat(formData.precio) - parseFloat(formData.costo)).toFixed(2)} 
                  ({(((parseFloat(formData.precio) - parseFloat(formData.costo)) / parseFloat(formData.precio)) * 100).toFixed(1)}%)
                </p>
              </div>
            )}
          </div>
        </Card>
      </div>

      {/* Imagen del Producto */}
      <Card className="p-4">
        <h3 className="font-semibold text-gray-900 mb-4">Imagen del Producto</h3>
        <div className="space-y-4">
          <Input
            label="URL de la Imagen"
            value={formData.imagen}
            onChange={(e) => setFormData({ ...formData, imagen: e.target.value })}
            placeholder="https://ejemplo.com/imagen.jpg"
          />
          
          {formData.imagen && (
            <div className="mt-4">
              <p className="text-sm font-medium text-gray-700 mb-2">Vista previa:</p>
              <img
                src={formData.imagen}
                alt="Vista previa"
                className="w-32 h-32 object-cover rounded-lg border border-gray-300"
                onError={(e) => {
                  (e.target as HTMLImageElement).src = 'https://images.pexels.com/photos/1536619/pexels-photo-1536619.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=2';
                }}
              />
            </div>
          )}
          
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p className="text-sm text-blue-800">
              <strong>Sugerencia:</strong> Puedes usar imágenes de Pexels o subir tus propias imágenes a un servicio de hosting.
            </p>
          </div>
        </div>
      </Card>

      {/* Estado del Producto */}
      <Card className="p-4">
        <h3 className="font-semibold text-gray-900 mb-4">Estado del Producto</h3>
        <div className="flex items-center">
          <input
            type="checkbox"
            id="activo"
            checked={formData.activo}
            onChange={(e) => setFormData({ ...formData, activo: e.target.checked })}
            className="rounded border-gray-300"
          />
          <label htmlFor="activo" className="ml-2 text-sm text-gray-700">
            Producto activo (visible en el sistema)
          </label>
        </div>
      </Card>

      {/* Botones */}
      <div className="flex justify-end space-x-3 pt-6 border-t">
        <Button variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit">
          {product ? 'Actualizar Producto' : 'Crear Producto'}
        </Button>
      </div>
    </form>
  );
}