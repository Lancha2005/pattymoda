import { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Tag, ChevronRight, Folder, FolderOpen } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Modal } from '../ui/Modal';
import { CategoryService } from '../../services/categoryService';

interface Category {
  id: string;
  nombre: string;
  descripcion: string;
  categoriaPadreId?: string;
  activo: boolean;
  orden?: number;
  fechaCreacion: string;
}

export function CategoryList() {
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [expandedCategories, setExpandedCategories] = useState<Set<string>>(new Set());
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await CategoryService.getAllCategories();
      setCategories(response.data || []);
    } catch (err: any) {
      setError("Error al cargar categorías: " + (err.message || "Error desconocido"));
      console.error("Error loading categories:", err);
    } finally {
      setLoading(false);
    }
  };

  // Organizar categorías en estructura jerárquica
  const organizeCategories = (categories: Category[]) => {
    const categoryMap = new Map();
    const rootCategories: any[] = [];

    // Crear mapa de categorías
    categories.forEach(cat => {
      categoryMap.set(cat.id, { ...cat, children: [] });
    });

    // Organizar jerarquía
    categories.forEach(cat => {
      const category = categoryMap.get(cat.id);
      if (cat.categoriaPadreId) {
        const parent = categoryMap.get(cat.categoriaPadreId);
        if (parent) {
          parent.children.push(category);
        }
      } else {
        rootCategories.push(category);
      }
    });

    return rootCategories;
  };

  const organizedCategories = organizeCategories(categories);
  const filteredCategories = organizedCategories.filter(category =>
    category.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    category.descripcion?.toLowerCase().includes(searchTerm.toLowerCase())
  );
  const toggleExpanded = (categoryId: string) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(categoryId)) {
      newExpanded.delete(categoryId);
    } else {
      newExpanded.add(categoryId);
    }
    setExpandedCategories(newExpanded);
  };

  const handleEditCategory = (category: Category) => {
    setSelectedCategory(category);
    setIsModalOpen(true);
  };

  const handleNewCategory = () => {
    setSelectedCategory(null);
    setIsModalOpen(true);
  };

  const handleDeleteCategory = async (categoryId: string) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar esta categoría?")) {
      try {
        await CategoryService.deleteCategory(categoryId);
        await loadCategories();
      } catch (err: any) {
        alert("Error al eliminar categoría: " + (err.message || "Error desconocido"));
      }
    }
  };

  const handleSaveCategory = async (categoryData: any) => {
    try {
      if (selectedCategory) {
        await CategoryService.updateCategory(selectedCategory.id, categoryData);
      } else {
        await CategoryService.createCategory(categoryData);
      }
      setIsModalOpen(false);
      await loadCategories();
    } catch (err: any) {
      alert("Error al guardar categoría: " + (err.message || "Error desconocido"));
    }
  };
  const renderCategory = (category: Category, level: number = 0) => (
    <div key={category.id} className="space-y-2">
      <div 
        className={`flex items-center justify-between p-4 bg-white rounded-lg border hover:shadow-md transition-all duration-200 ${
          level > 0 ? 'ml-8 border-l-4 border-l-yellow-400' : 'shadow-sm'
        }`}
      >
        <div className="flex items-center space-x-3">
          {(category as any).children && (category as any).children.length > 0 && (
            <button
              onClick={() => toggleExpanded(category.id)}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <ChevronRight 
                className={`w-4 h-4 transition-transform ${
                  expandedCategories.has(category.id) ? 'rotate-90' : ''
                }`} 
              />
            </button>
          )}
          
          <div className="p-2 bg-yellow-100 rounded-lg">
            {expandedCategories.has(category.id) ? (
              <FolderOpen className="w-5 h-5 text-yellow-600" />
            ) : (
              <Folder className="w-5 h-5 text-yellow-600" />
            )}
          </div>
          
          <div>
            <h3 className="font-semibold text-gray-900">{category.nombre}</h3>
            <p className="text-sm text-gray-500">{category.descripcion}</p>
            <p className="text-xs text-gray-400">
              Creada: {new Date(category.fechaCreacion).toLocaleDateString('es-PE')}
            </p>
          </div>
        </div>

        <div className="flex items-center space-x-4">
          <div className={`px-2 py-1 rounded-full text-xs font-medium ${
            category.activo ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
          }`}>
            {category.activo ? 'Activa' : 'Inactiva'}
          </div>
          
          <div className="flex space-x-1">
            <Button
              variant="ghost"
              size="sm"
              onClick={() => handleEditCategory(category)}
            >
              <Edit className="w-4 h-4" />
            </Button>
            <Button 
              variant="ghost" 
              size="sm"
              onClick={() => handleDeleteCategory(category.id)}
            >
              <Trash2 className="w-4 h-4" />
            </Button>
          </div>
        </div>
      </div>

      {(category as any).children && expandedCategories.has(category.id) && (
        <div className="space-y-2">
          {(category as any).children.map((child: any) => renderCategory(child, level + 1))}
        </div>
      )}
    </div>
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500"></div>
        <span className="ml-3 text-gray-600">Cargando categorías...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-12">
        <div className="text-red-500 mb-4">{error}</div>
        <Button onClick={loadCategories}>Reintentar</Button>
      </div>
    );
  }
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Categorías</h1>
          <p className="text-gray-600 mt-1">Organiza tu inventario por categorías</p>
        </div>
        <Button onClick={handleNewCategory} leftIcon={<Plus className="w-4 h-4" />}>
          Nueva Categoría
        </Button>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Tag className="w-6 h-6 text-blue-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Total Categorías</p>
              <p className="text-2xl font-bold text-gray-900">{categories.length}</p>
            </div>
          </div>
        </Card>
        
        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <Folder className="w-6 h-6 text-green-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Categorías Activas</p>
              <p className="text-2xl font-bold text-gray-900">
                {categories.filter(c => c.activo).length}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <Tag className="w-6 h-6 text-yellow-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Subcategorías</p>
              <p className="text-2xl font-bold text-gray-900">
                {categories.filter(c => c.categoriaPadreId).length}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-purple-100 rounded-lg">
              <Tag className="w-6 h-6 text-purple-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">Principales</p>
              <p className="text-2xl font-bold text-gray-900">
                {categories.filter(c => !c.categoriaPadreId).length}
              </p>
            </div>
          </div>
        </Card>
      </div>

      {/* Búsqueda */}
      <Card>
        <CardContent className="p-4">
          <Input
            placeholder="Buscar categorías..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            leftIcon={<Search className="w-4 h-4 text-gray-400" />}
          />
        </CardContent>
      </Card>

      {/* Lista de Categorías */}
      <div className="space-y-4">
        {filteredCategories.map(category => renderCategory(category))}
      </div>

      {filteredCategories.length === 0 && !loading && (
        <Card>
          <CardContent className="text-center py-12">
            <Tag className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              No se encontraron categorías
            </h3>
            <p className="text-gray-500 mb-4">
              {searchTerm ? "Prueba con otros términos de búsqueda" : "Comienza creando tu primera categoría"}
            </p>
            <Button onClick={handleNewCategory}>
              {searchTerm ? "Limpiar búsqueda" : "Crear Primera Categoría"}
            </Button>
          </CardContent>
        </Card>
      )}
      {/* Modal de Categoría */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={selectedCategory ? 'Editar Categoría' : 'Nueva Categoría'}
        size="md"
      >
        <div className="space-y-4">
          <Input 
            label="Nombre de la Categoría" 
            defaultValue={selectedCategory?.nombre}
            id="categoryName"
          />
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Descripción
            </label>
            <textarea
              defaultValue={selectedCategory?.descripcion}
              id="categoryDescription"
              rows={3}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
              placeholder="Descripción de la categoría..."
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Categoría Padre
            </label>
            <select 
              id="categoryParent"
              defaultValue={selectedCategory?.categoriaPadreId || ''}
              className="block w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
            >
              <option value="">Sin categoría padre</option>
              {categories.filter(cat => !cat.categoriaPadreId && cat.id !== selectedCategory?.id).map(cat => (
                <option key={cat.id} value={cat.id}>{cat.nombre}</option>
              ))}
            </select>
          </div>
          
          <div className="flex items-center">
            <input
              type="checkbox"
              id="categoryActive"
              defaultChecked={selectedCategory?.activo ?? true}
              className="rounded border-gray-300"
            />
            <label htmlFor="categoryActive" className="ml-2 text-sm text-gray-700">
              Categoría activa
            </label>
          </div>
          
          <div className="flex justify-end space-x-3 pt-6 border-t">
            <Button variant="ghost" onClick={() => setIsModalOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={() => {
              const formData = {
                nombre: (document.getElementById('categoryName') as HTMLInputElement)?.value,
                descripcion: (document.getElementById('categoryDescription') as HTMLTextAreaElement)?.value,
                categoriaPadreId: (document.getElementById('categoryParent') as HTMLSelectElement)?.value || null,
                activo: (document.getElementById('categoryActive') as HTMLInputElement)?.checked,
              };
              handleSaveCategory(formData);
            }}>
              {selectedCategory ? 'Actualizar Categoría' : 'Crear Categoría'}
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}