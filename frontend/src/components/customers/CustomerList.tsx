import { useState, useEffect } from "react";
import {
  Plus,
  Search,
  Edit,
  Trash2,
  Users,
  Phone,
  Mail,
  MapPin,
  Eye,
  Star,
  Calendar,
  CreditCard
} from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "../ui/Card";
import { Button } from "../ui/Button";
import { Input } from "../ui/Input";
import { Modal } from "../ui/Modal";
import { CustomerService } from "../../services/customerService";

interface Customer {
  id: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  direccion: string;
  distrito: string;
  ciudad: string;
  totalCompras: number;
  ultimaCompra: string;
  activo: boolean;
  fechaCreacion: string;
  tipoCliente?: string;
  metodoPagoPreferido?: string;
  cantidadCompras?: number;
}

export function CustomerList() {
  const [searchTerm, setSearchTerm] = useState("");
  const [filterType, setFilterType] = useState("all");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isViewModalOpen, setIsViewModalOpen] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Cargar clientes desde el backend
  useEffect(() => {
    loadCustomers();
  }, []);

  const loadCustomers = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await CustomerService.getAllCustomers();
      setCustomers(response.data || []);
    } catch (err: any) {
      setError("Error al cargar clientes: " + (err.message || "Error desconocido"));
      console.error("Error loading customers:", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredCustomers = customers.filter(
    (customer) => {
      const matchesSearch = `${customer.nombre} ${customer.apellido}`
        .toLowerCase()
        .includes(searchTerm.toLowerCase()) ||
      customer.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      customer.telefono.includes(searchTerm);
    
    const customerType = getCustomerType(customer);
    const matchesFilter = filterType === "all" || 
      (filterType === "vip" && customerType === "VIP") ||
      (filterType === "regular" && customerType === "Regular") ||
      (filterType === "new" && customerType === "Nuevo") ||
      (filterType === "inactive" && !customer.activo);
    
    return matchesSearch && matchesFilter;
    }
  );
  const getCustomerType = (customer: Customer) => {
    if (customer.totalCompras >= 2000) return "VIP";
    if (customer.totalCompras >= 500) return "Regular";
    return "Nuevo";
  };

  const getCustomerTypeColor = (type: string) => {
    switch (type) {
      case "VIP": return "bg-purple-100 text-purple-800";
      case "Regular": return "bg-blue-100 text-blue-800";
      case "Nuevo": return "bg-green-100 text-green-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  const handleEditCustomer = (customer: Customer) => {
    setSelectedCustomer(customer);
    setIsModalOpen(true);
  };

  const handleViewCustomer = (customer: Customer) => {
    setSelectedCustomer(customer);
    setIsViewModalOpen(true);
  };
  const handleNewCustomer = () => {
    setSelectedCustomer(null);
    setIsModalOpen(true);
  };

  const handleDeleteCustomer = async (customerId: string) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este cliente?")) {
      try {
        await CustomerService.deleteCustomer(customerId);
        await loadCustomers(); // Recargar la lista
      } catch (err: any) {
        alert("Error al eliminar cliente: " + (err.message || "Error desconocido"));
      }
    }
  };

  const handleSaveCustomer = async (customerData: any) => {
    try {
      if (selectedCustomer) {
        await CustomerService.updateCustomer(selectedCustomer.id, customerData);
      } else {
        await CustomerService.createCustomer(customerData);
      }
      setIsModalOpen(false);
      await loadCustomers(); // Recargar la lista
    } catch (err: any) {
      alert("Error al guardar cliente: " + (err.message || "Error desconocido"));
    }
  };

  // Calcular estadísticas
  const vipCustomers = customers.filter(c => c.totalCompras >= 2000);
  const regularCustomers = customers.filter(c => c.totalCompras >= 500 && c.totalCompras < 2000);
  const newCustomers = customers.filter(c => c.totalCompras < 500);
  const averageSpending = customers.length > 0 
    ? customers.reduce((acc, c) => acc + c.totalCompras, 0) / customers.length 
    : 0;
  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500"></div>
        <span className="ml-3 text-gray-600">Cargando clientes...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-12">
        <div className="text-red-500 mb-4">{error}</div>
        <Button onClick={loadCustomers}>Reintentar</Button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Clientes</h1>
          <p className="text-gray-600 mt-1">Gestiona tu base de clientes</p>
        </div>
        <Button
          onClick={handleNewCustomer}
          leftIcon={<Plus className="w-4 h-4" />}
        >
          Nuevo Cliente
        </Button>
      </div>

      {/* Estadísticas Rápidas */}
      <div className="grid grid-cols-1 md:grid-cols-5 gap-6">
        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Users className="w-6 h-6 text-blue-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">
                Total Clientes
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {customers.length}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-purple-100 rounded-lg">
              <Star className="w-6 h-6 text-purple-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">
                Clientes VIP
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {vipCustomers.length}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <Users className="w-6 h-6 text-green-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">
                Clientes Regulares
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {regularCustomers.length}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-yellow-100 rounded-lg">
              <CreditCard className="w-6 h-6 text-yellow-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">
                Promedio Compras
              </p>
              <p className="text-2xl font-bold text-gray-900">
                S/ {averageSpending.toFixed(0)}
              </p>
            </div>
          </div>
        </Card>

        <Card className="p-4">
          <div className="flex items-center">
            <div className="p-2 bg-indigo-100 rounded-lg">
              <Calendar className="w-6 h-6 text-indigo-600" />
            </div>
            <div className="ml-4">
              <p className="text-sm font-medium text-gray-500">
                Clientes Nuevos
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {newCustomers.length}
              </p>
            </div>
          </div>
        </Card>
      </div>

      {/* Búsqueda */}
      <Card>
        <CardContent className="p-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="md:col-span-2">
              <Input
                placeholder="Buscar clientes por nombre, email o teléfono..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                leftIcon={<Search className="w-4 h-4 text-gray-400" />}
              />
            </div>
            
            <select
              value={filterType}
              onChange={(e) => setFilterType(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-yellow-500 focus:border-yellow-500"
            >
              <option value="all">Todos los clientes</option>
              <option value="vip">Clientes VIP</option>
              <option value="regular">Clientes Regulares</option>
              <option value="new">Clientes Nuevos</option>
              <option value="inactive">Inactivos</option>
            </select>
          </div>
        </CardContent>
      </Card>

      {/* Lista de Clientes */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {filteredCustomers.map((customer) => (
          <Card
            key={customer.id}
            className="hover:shadow-lg transition-shadow duration-200"
          >
            <CardContent className="p-6">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-3">
                  <div className="w-12 h-12 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-full flex items-center justify-center text-black font-bold">
                    {customer.nombre.charAt(0)}
                    {customer.apellido.charAt(0)}
                  </div>
                  <div>
                    <h3 className="font-semibold text-gray-900">
                      {customer.nombre} {customer.apellido}
                    </h3>
                    <p className="text-sm text-gray-500">
                      Cliente desde{" "}
                      {new Date(customer.fechaCreacion).getFullYear()}
                    </p>
                  </div>
                </div>
                <div className="flex space-x-1">
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleViewCustomer(customer)}
                  >
                    <Eye className="w-4 h-4" />
                  </Button>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleEditCustomer(customer)}
                  >
                    <Edit className="w-4 h-4" />
                  </Button>
                  <Button 
                    variant="ghost" 
                    size="sm"
                    onClick={() => handleDeleteCustomer(customer.id)}
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </div>

              <div className="space-y-3">
                <div className="flex items-center space-x-2 text-sm text-gray-600">
                  <Mail className="w-4 h-4" />
                  <span>{customer.email}</span>
                </div>

                <div className="flex items-center space-x-2 text-sm text-gray-600">
                  <Phone className="w-4 h-4" />
                  <span>{customer.telefono}</span>
                </div>

                <div className="flex items-center space-x-2 text-sm text-gray-600">
                  <MapPin className="w-4 h-4" />
                  <span className="line-clamp-1">
                    {customer.direccion}, {customer.distrito}, {customer.ciudad}
                  </span>
                </div>
                
                {customer.metodoPagoPreferido && (
                  <div className="flex items-center space-x-2 text-sm text-gray-600">
                    <CreditCard className="w-4 h-4" />
                    <span>Prefiere: {customer.metodoPagoPreferido}</span>
                  </div>
                )}
              </div>

              <div className="mt-4 pt-4 border-t border-gray-100">
                <div className="flex justify-between items-center">
                  <div>
                    <p className="text-sm text-gray-500">Total Compras</p>
                    <p className="font-bold text-green-600">
                      S/ {customer.totalCompras.toLocaleString()}
                    </p>
                    {customer.cantidadCompras && (
                      <p className="text-xs text-gray-500">
                        {customer.cantidadCompras} compras
                      </p>
                    )}
                  </div>
                  <div className="text-right">
                    <p className="text-sm text-gray-500">Última Compra</p>
                    <p className="text-sm font-medium text-gray-900">
                      {customer.ultimaCompra ? 
                        new Date(customer.ultimaCompra).toLocaleDateString("es-PE") :
                        "Sin compras"
                      }
                    </p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredCustomers.length === 0 && !loading && (
        <Card>
          <CardContent className="text-center py-12">
            <Users className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">
              No se encontraron clientes
            </h3>
            <p className="text-gray-500 mb-4">
              {searchTerm ? "Prueba con otros términos de búsqueda" : "Comienza agregando tu primer cliente"}
            </p>
            <Button onClick={handleNewCustomer}>
              {searchTerm ? "Limpiar búsqueda" : "Agregar Primer Cliente"}
            </Button>
          </CardContent>
        </Card>
      )}

      {/* Modal de Cliente */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={selectedCustomer ? "Editar Cliente" : "Nuevo Cliente"}
        size="lg"
      >
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input 
              label="Nombre" 
              defaultValue={selectedCustomer?.nombre}
              id="nombre"
            />
            <Input 
              label="Apellido" 
              defaultValue={selectedCustomer?.apellido}
              id="apellido"
            />
          </div>
          <Input
            label="Email"
            type="email"
            defaultValue={selectedCustomer?.email}
            id="email"
          />
          <Input 
            label="Teléfono" 
            defaultValue={selectedCustomer?.telefono}
            id="telefono"
          />
          <Input 
            label="Dirección" 
            defaultValue={selectedCustomer?.direccion}
            id="direccion"
          />
          <div className="grid grid-cols-2 gap-4">
            <Input 
              label="Distrito" 
              defaultValue={selectedCustomer?.distrito}
              id="distrito"
            />
            <Input 
              label="Ciudad" 
              defaultValue={selectedCustomer?.ciudad || "Pampa Hermosa"}
              id="ciudad"
            />
          </div>

          <div className="flex justify-end space-x-3 pt-6 border-t">
            <Button variant="ghost" onClick={() => setIsModalOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={() => {
              const formData = {
                nombre: (document.getElementById('nombre') as HTMLInputElement)?.value,
                apellido: (document.getElementById('apellido') as HTMLInputElement)?.value,
                email: (document.getElementById('email') as HTMLInputElement)?.value,
                telefono: (document.getElementById('telefono') as HTMLInputElement)?.value,
                direccion: (document.getElementById('direccion') as HTMLInputElement)?.value,
                distrito: (document.getElementById('distrito') as HTMLInputElement)?.value,
                ciudad: (document.getElementById('ciudad') as HTMLInputElement)?.value,
              };
              handleSaveCustomer(formData);
            }}>
              {selectedCustomer ? "Actualizar Cliente" : "Crear Cliente"}
            </Button>
          </div>
        </div>
      </Modal>
      {/* Modal de Vista de Cliente */}
      <Modal
        isOpen={isViewModalOpen}
        onClose={() => setIsViewModalOpen(false)}
        title="Detalles del Cliente"
        size="lg"
      >
        {selectedCustomer && (
          <div className="space-y-6">
            <div className="flex items-center space-x-4">
              <div className="w-16 h-16 bg-gradient-to-r from-yellow-400 to-yellow-500 rounded-full flex items-center justify-center text-black font-bold text-xl">
                {selectedCustomer.nombre.charAt(0)}
                {selectedCustomer.apellido.charAt(0)}
              </div>
              <div>
                <h3 className="text-2xl font-bold text-gray-900">
                  {selectedCustomer.nombre} {selectedCustomer.apellido}
                </h3>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                  getCustomerTypeColor(getCustomerType(selectedCustomer))
                }`}>
                  Cliente {getCustomerType(selectedCustomer)}
                </span>
              </div>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-4">
                <h4 className="font-semibold text-gray-900">Información de Contacto</h4>
                <div className="space-y-2 text-sm">
                  <div className="flex items-center space-x-2">
                    <Mail className="w-4 h-4 text-gray-400" />
                    <span>{selectedCustomer.email}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Phone className="w-4 h-4 text-gray-400" />
                    <span>{selectedCustomer.telefono}</span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <MapPin className="w-4 h-4 text-gray-400" />
                    <span>{selectedCustomer.direccion}, {selectedCustomer.distrito}, {selectedCustomer.ciudad}</span>
                  </div>
                </div>
              </div>
              
              <div className="space-y-4">
                <h4 className="font-semibold text-gray-900">Estadísticas de Compras</h4>
                <div className="space-y-3">
                  <div className="flex justify-between">
                    <span className="text-gray-600">Total Compras:</span>
                    <span className="font-bold text-green-600">S/ {selectedCustomer.totalCompras.toLocaleString()}</span>
                  </div>
                  {selectedCustomer.cantidadCompras && (
                    <div className="flex justify-between">
                      <span className="text-gray-600">Número de Compras:</span>
                      <span className="font-medium">{selectedCustomer.cantidadCompras}</span>
                    </div>
                  )}
                  <div className="flex justify-between">
                    <span className="text-gray-600">Última Compra:</span>
                    <span className="font-medium">
                      {selectedCustomer.ultimaCompra ? 
                        new Date(selectedCustomer.ultimaCompra).toLocaleDateString("es-PE") :
                        "Sin compras"
                      }
                    </span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-gray-600">Cliente desde:</span>
                    <span className="font-medium">
                      {new Date(selectedCustomer.fechaCreacion).toLocaleDateString("es-PE")}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <div className="flex justify-end space-x-3 pt-6 border-t">
              <Button variant="ghost" onClick={() => setIsViewModalOpen(false)}>
                Cerrar
              </Button>
              <Button onClick={() => {
                setIsViewModalOpen(false);
                handleEditCustomer(selectedCustomer);
              }}>
                Editar Cliente
              </Button>
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
}