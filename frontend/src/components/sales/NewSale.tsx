import { useState, useEffect } from 'react';
import { Plus, Search, Minus, ShoppingCart, User, CreditCard, Calculator, Receipt, Package } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Modal } from '../ui/Modal';
import { TaxService } from '../../services/taxService';
import { ProductService } from '../../services/productService';
import { CustomerService } from '../../services/customerService';

interface Product {
  id: string;
  nombre: string;
  sku: string;
  price: number;
  stock: number;
  imagen: string;
  categoria: {
    nombre: string;
  };
}

interface Customer {
  id: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
}
interface SaleItem {
  product: Product;
  quantity: number;
  size: string;
  color: string;
}

interface PaymentMethod {
  type: string;
  amount: number;
  reference?: string;
}

export function NewSale() {
  const [searchTerm, setSearchTerm] = useState('');
  const [customerSearchTerm, setCustomerSearchTerm] = useState('');
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([
    { type: 'cash', amount: 0 }
  ]);
  const [discount, setDiscount] = useState(0);
  const [isCustomerModalOpen, setIsCustomerModalOpen] = useState(false);
  const [igvConfig, setIgvConfig] = useState<any>(null);
  const [applyTax, setApplyTax] = useState(true);
  const [products, setProducts] = useState<Product[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);

  // Cargar datos al inicializar
  useEffect(() => {
    loadIGVConfig();
    loadProducts();
    loadCustomers();
  }, []);

  const loadIGVConfig = async () => {
    try {
      const response = await TaxService.getIGVConfig();
      setIgvConfig(response.data);
      setApplyTax(response.data?.activo || false);
    } catch (error) {
      console.error('Error loading IGV config:', error);
    }
  };

  const loadProducts = async () => {
    try {
      const response = await ProductService.getAllProducts();
      const productsData = response.data || [];
      // Mapear los datos del backend al formato esperado
      const mappedProducts = productsData.map((p: any) => ({
        id: p.id,
        nombre: p.nombre,
        sku: p.sku,
        price: p.precio,
        stock: p.stock,
        imagen: p.imagen || 'https://images.pexels.com/photos/1536619/pexels-photo-1536619.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=2',
        categoria: p.categoria
      }));
      setProducts(mappedProducts);
    } catch (error) {
      console.error('Error loading products:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadCustomers = async () => {
    try {
      const response = await CustomerService.getAllCustomers();
      const customersData = response.data || [];
      // Mapear los datos del backend al formato esperado
      const mappedCustomers = customersData.map((c: any) => ({
        id: c.id,
        nombre: c.nombre,
        apellido: c.apellido,
        email: c.email,
        telefono: c.telefono
      }));
      setCustomers(mappedCustomers);
    } catch (error) {
      console.error('Error loading customers:', error);
    }
  };
  const filteredProducts = products.filter(product =>
    product.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.sku.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.categoria.nombre.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredCustomers = customers.filter(customer =>
    `${customer.nombre} ${customer.apellido}`.toLowerCase().includes(customerSearchTerm.toLowerCase()) ||
    customer.email.toLowerCase().includes(customerSearchTerm.toLowerCase()) ||
    customer.telefono.includes(customerSearchTerm)
  );
  const addToSale = (product: Product) => {
    if (product.stock <= 0) {
      alert('Este producto no tiene stock disponible');
      return;
    }
    
    const existingItem = saleItems.find(item => item.product.id === product.id);
    if (existingItem) {
      if (existingItem.quantity >= product.stock) {
        alert('No hay suficiente stock para agregar más unidades');
        return;
      }
      setSaleItems(saleItems.map(item =>
        item.product.id === product.id
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ));
    } else {
      setSaleItems([...saleItems, {
        product,
        quantity: 1,
        size: 'M',
        color: 'Negro'
      }]);
    }
  };

  const updateQuantity = (productId: string, newQuantity: number) => {
    if (newQuantity === 0) {
      setSaleItems(saleItems.filter(item => item.product.id !== productId));
    } else {
      const product = products.find(p => p.id === productId);
      if (product && newQuantity > product.stock) {
        alert('No hay suficiente stock disponible');
        return;
      }
      setSaleItems(saleItems.map(item =>
        item.product.id === productId
          ? { ...item, quantity: newQuantity }
          : item
      ));
    }
  };

  const addPaymentMethod = () => {
    setPaymentMethods([...paymentMethods, { type: 'cash', amount: 0 }]);
  };

  const updatePaymentMethod = (index: number, field: string, value: any) => {
    const updated = paymentMethods.map((method, i) => 
      i === index ? { ...method, [field]: value } : method
    );
    setPaymentMethods(updated);
  };

  const removePaymentMethod = (index: number) => {
    if (paymentMethods.length > 1) {
      setPaymentMethods(paymentMethods.filter((_, i) => i !== index));
    }
  };

  const subtotal = saleItems.reduce((acc, item) => acc + (item.product.price * item.quantity), 0);
  const discountAmount = (subtotal * discount) / 100;
  const afterDiscount = subtotal - discountAmount;
  const taxRate = (igvConfig?.activo && applyTax) ? (igvConfig.porcentaje / 100) : 0;
  const tax = afterDiscount * taxRate;
  const total = afterDiscount + tax;
  const totalPaid = paymentMethods.reduce((acc, method) => acc + method.amount, 0);
  const change = totalPaid - total;

  const paymentMethodOptions = [
    { value: 'cash', label: '💵 Efectivo', icon: '💵' },
    { value: 'card', label: '💳 Tarjeta', icon: '💳' },
    { value: 'yape', label: '📱 Yape', icon: '📱' },
    { value: 'plin', label: '📲 Plin', icon: '📲' },
    { value: 'transfer', label: '🏦 Transferencia', icon: '🏦' },
  ];

  const handleCompleteSale = () => {
    if (!selectedCustomer) {
      alert('Debe seleccionar un cliente');
      return;
    }
    
    if (saleItems.length === 0) {
      alert('Debe agregar al menos un producto');
      return;
    }
    
    if (totalPaid < total) {
      alert('El monto pagado es insuficiente');
      return;
    }

    console.log('Venta completada:', {
      customer: selectedCustomer,
      items: saleItems,
      paymentMethods,
      subtotal,
      discount: discountAmount,
      tax,
      total,
      totalPaid,
      change
    });
    
    // Limpiar formulario
    setSaleItems([]);
    setSelectedCustomer(null);
    setCustomerSearchTerm('');
    setPaymentMethods([{ type: 'cash', amount: 0 }]);
    setDiscount(0);
    
    alert('Venta registrada exitosamente');
  };

  const handleCreateCustomer = async () => {
    const customerData = {
      nombre: (document.getElementById('newCustomerName') as HTMLInputElement)?.value,
      apellido: (document.getElementById('newCustomerLastName') as HTMLInputElement)?.value,
      email: (document.getElementById('newCustomerEmail') as HTMLInputElement)?.value,
      telefono: (document.getElementById('newCustomerPhone') as HTMLInputElement)?.value,
      direccion: (document.getElementById('newCustomerAddress') as HTMLInputElement)?.value,
      distrito: 'Pampa Hermosa',
      ciudad: 'Pampa Hermosa'
    };
    
    try {
      const response = await CustomerService.createCustomer(customerData);
      const newCustomer = response.data || response;
      setSelectedCustomer(newCustomer);
      setCustomers([...customers, newCustomer]);
      setIsCustomerModalOpen(false);
      alert('Cliente creado exitosamente');
    } catch (error: any) {
      alert('Error al crear cliente: ' + (error.message || 'Error desconocido'));
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500"></div>
        <span className="ml-3 text-gray-600">Cargando datos...</span>
      </div>
    );
  }
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold bg-gradient-to-r from-green-600 to-blue-600 bg-clip-text text-transparent">
            🛒 Nueva Venta
          </h1>
          <p className="text-gray-600 mt-1">Procesa una nueva transacción de DPattyModa</p>
        </div>
        <div className="flex space-x-3">
          <Button variant="outline" leftIcon={<Receipt className="w-4 h-4" />}>
            📋 Borrador
          </Button>
          <Button variant="outline" leftIcon={<Calculator className="w-4 h-4" />}>
            🧮 Calculadora
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Productos */}
        <div className="lg:col-span-2 space-y-6">
          <Card className="bg-gradient-to-br from-blue-50 to-indigo-50 border-blue-200">
            <CardHeader>
              <CardTitle className="flex items-center text-blue-800">
                <ShoppingCart className="w-5 h-5 mr-2" />
                🛍️ Seleccionar Productos
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <Input
                  placeholder="🔍 Buscar productos por nombre, código..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  leftIcon={<Search className="w-4 h-4 text-gray-400" />}
                  className="bg-white border-blue-300 focus:ring-blue-500 focus:border-blue-500"
                />
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 max-h-96 overflow-y-auto">
                  {filteredProducts.map((product) => (
                    <div
                      key={product.id}
                      className="flex items-center space-x-3 p-4 bg-white border-2 border-gray-200 rounded-xl hover:border-blue-400 hover:shadow-lg cursor-pointer transition-all duration-200"
                      onClick={() => addToSale(product)}
                    >
                      <img
                        src={product.imagen}
                        alt={product.nombre}
                        className="w-16 h-16 object-cover rounded-lg shadow-md"
                      />
                      <div className="flex-1">
                        <h4 className="font-bold text-gray-900">{product.nombre}</h4>
                        <p className="text-xs text-gray-500">{product.sku} • {product.categoria.nombre}</p>
                        <p className="text-sm text-gray-500">📦 Stock: {product.stock}</p>
                        <p className="text-lg font-bold text-green-600">S/ {product.price}</p>
                      </div>
                      <Button 
                        size="sm" 
                        className="bg-gradient-to-r from-green-500 to-blue-500 hover:from-green-600 hover:to-blue-600"
                        disabled={product.stock <= 0}
                      >
                        <Plus className="w-4 h-4" />
                      </Button>
                    </div>
                  ))}
                   
                   {filteredProducts.length === 0 && (
                     <div className="col-span-2 text-center py-8 text-gray-500">
                       <Package className="w-12 h-12 mx-auto mb-2 opacity-50" />
                       <p>No se encontraron productos</p>
                     </div>
                   )}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Panel Derecho */}
        <div className="space-y-6">
          {/* Cliente */}
          <Card className="bg-gradient-to-br from-purple-50 to-pink-50 border-purple-200">
            <CardHeader>
              <CardTitle className="flex items-center text-purple-800">
                <User className="w-5 h-5 mr-2" />
                👤 Cliente
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {selectedCustomer ? (
                  <div className="p-3 bg-white rounded-lg border border-purple-300">
                    <div className="flex items-center justify-between">
                      <div>
                        <h4 className="font-medium text-gray-900">
                          {selectedCustomer.nombre} {selectedCustomer.apellido}
                        </h4>
                        <p className="text-sm text-gray-500">{selectedCustomer.email}</p>
                        <p className="text-sm text-gray-500">{selectedCustomer.telefono}</p>
                      </div>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => {
                          setSelectedCustomer(null);
                          setCustomerSearchTerm('');
                        }}
                      >
                        ✕
                      </Button>
                    </div>
                  </div>
                ) : (
                  <>
                    <Input
                      placeholder="🔍 Buscar cliente..."
                      value={customerSearchTerm}
                      onChange={(e) => setCustomerSearchTerm(e.target.value)}
                      className="bg-white border-purple-300 focus:ring-purple-500 focus:border-purple-500"
                    />
                    
                    {customerSearchTerm && filteredCustomers.length > 0 && (
                      <div className="max-h-40 overflow-y-auto bg-white border border-purple-300 rounded-lg">
                        {filteredCustomers.slice(0, 5).map((customer) => (
                          <div
                            key={customer.id}
                            className="p-2 hover:bg-purple-50 cursor-pointer border-b last:border-b-0"
                            onClick={() => {
                              setSelectedCustomer(customer);
                              setCustomerSearchTerm('');
                            }}
                          >
                            <p className="font-medium text-gray-900">
                              {customer.nombre} {customer.apellido}
                            </p>
                            <p className="text-sm text-gray-500">{customer.email}</p>
                          </div>
                        ))}
                      </div>
                    )}
                  </>
                )}
                
                <Button
                  variant="outline"
                  size="sm"
                  className="w-full border-purple-300 text-purple-700 hover:bg-purple-100"
                  onClick={() => setIsCustomerModalOpen(true)}
                >
                  ➕ Nuevo Cliente
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Carrito */}
          <Card className="bg-gradient-to-br from-green-50 to-emerald-50 border-green-200">
            <CardHeader>
              <CardTitle className="flex items-center text-green-800">
                <ShoppingCart className="w-5 h-5 mr-2" />
                🛒 Carrito ({saleItems.length})
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3 max-h-64 overflow-y-auto">
                {saleItems.map((item) => (
                  <div key={item.product.id} className="flex items-center space-x-2 p-3 bg-white rounded-lg shadow-sm border border-green-200">
                    <img
                      src={item.product.imagen}
                      alt={item.product.nombre}
                      className="w-10 h-10 object-cover rounded-lg"
                    />
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-bold text-gray-900 truncate">
                        {item.product.nombre}
                      </p>
                      <p className="text-xs text-gray-500">
                        S/ {item.product.price} c/u
                      </p>
                    </div>
                    <div className="flex items-center space-x-1">
                      <Button
                        size="sm"
                        variant="ghost"
                        onClick={() => updateQuantity(item.product.id, item.quantity - 1)}
                        className="w-8 h-8 p-0"
                      >
                        <Minus className="w-3 h-3" />
                      </Button>
                      <span className="text-sm font-bold w-8 text-center bg-gray-100 rounded px-2 py-1">
                        {item.quantity}
                      </span>
                      <Button
                        size="sm"
                        variant="ghost"
                        onClick={() => updateQuantity(item.product.id, item.quantity + 1)}
                        className="w-8 h-8 p-0"
                        disabled={item.quantity >= item.product.stock}
                      >
                        <Plus className="w-3 h-3" />
                      </Button>
                    </div>
                  </div>
                ))}
                
                {saleItems.length === 0 && (
                  <div className="text-center py-8 text-gray-500">
                    <ShoppingCart className="w-12 h-12 mx-auto mb-2 opacity-50" />
                    <p className="text-sm">🛒 Carrito vacío</p>
                    <p className="text-xs">Agrega productos para comenzar</p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>

          {/* Descuento */}
          <Card className="bg-gradient-to-br from-yellow-50 to-orange-50 border-yellow-200">
            <CardHeader>
              <CardTitle className="flex items-center text-yellow-800">
                🏷️ Descuento
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center space-x-2">
                <Input
                  type="number"
                  placeholder="0"
                  value={discount}
                  onChange={(e) => setDiscount(Number(e.target.value))}
                  className="bg-white border-yellow-300 focus:ring-yellow-500 focus:border-yellow-500"
                />
                <span className="text-yellow-800 font-bold">%</span>
              </div>
              {discount > 0 && (
                <p className="text-sm text-yellow-700 mt-2">
                  💰 Descuento: S/ {discountAmount.toFixed(2)}
                </p>
              )}
            </CardContent>
          </Card>

          {/* Configuración de Impuestos */}
          {igvConfig && (
            <Card className="bg-gradient-to-br from-indigo-50 to-purple-50 border-indigo-200">
              <CardHeader>
                <CardTitle className="flex items-center text-indigo-800">
                  <Calculator className="w-5 h-5 mr-2" />
                  🧮 Configuración de Impuestos
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex items-center justify-between p-3 bg-white rounded-lg border border-indigo-200">
                    <div>
                      <h4 className="font-medium text-indigo-900">
                        {igvConfig.nombre} ({igvConfig.porcentaje}%)
                      </h4>
                      <p className="text-sm text-indigo-700">
                        {igvConfig.activo ? 'Disponible para aplicar' : 'Desactivado en configuración'}
                      </p>
                    </div>
                    {igvConfig.activo && (
                      <label className="flex items-center">
                        <input
                          type="checkbox"
                          checked={applyTax}
                          onChange={(e) => setApplyTax(e.target.checked)}
                          className="rounded border-indigo-300 text-indigo-600 focus:ring-indigo-500"
                        />
                        <span className="ml-2 text-sm font-medium text-indigo-900">
                          Aplicar a esta venta
                        </span>
                      </label>
                    )}
                  </div>
                  
                  {!igvConfig.activo && (
                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-3">
                      <p className="text-sm text-yellow-800">
                        <strong>Nota:</strong> El IGV está desactivado en la configuración del sistema. 
                        Puedes activarlo desde Configuración → Impuestos.
                      </p>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>
          )}

          {/* Resumen */}
          <Card className="bg-gradient-to-br from-gray-50 to-slate-100 border-gray-300">
            <CardHeader>
              <CardTitle className="text-gray-800">📊 Resumen de Venta</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex justify-between">
                  <span>Subtotal:</span>
                  <span className="font-bold">S/ {subtotal.toFixed(2)}</span>
                </div>
                {discount > 0 && (
                  <div className="flex justify-between text-yellow-600">
                    <span>Descuento ({discount}%):</span>
                    <span className="font-bold">-S/ {discountAmount.toFixed(2)}</span>
                  </div>
                )}
                <div className="flex justify-between">
                  <span>{igvConfig?.nombre || 'IGV'} ({(taxRate * 100).toFixed(1)}%):</span>
                  <span className="font-bold">S/ {tax.toFixed(2)}</span>
                </div>
                <div className="border-t pt-3">
                  <div className="flex justify-between font-bold text-xl text-green-600">
                    <span>💰 Total:</span>
                    <span>S/ {total.toFixed(2)}</span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Métodos de Pago Combinados */}
          <Card className="bg-gradient-to-br from-indigo-50 to-blue-50 border-indigo-200">
            <CardHeader>
              <CardTitle className="flex items-center justify-between text-indigo-800">
                <div className="flex items-center">
                  <CreditCard className="w-5 h-5 mr-2" />
                  💳 Métodos de Pago
                </div>
                <Button
                  size="sm"
                  variant="outline"
                  onClick={addPaymentMethod}
                  className="border-indigo-300 text-indigo-700 hover:bg-indigo-100"
                >
                  <Plus className="w-4 h-4" />
                </Button>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {paymentMethods.map((method, index) => (
                  <div key={index} className="p-3 bg-white rounded-lg border border-indigo-200">
                    <div className="flex items-center justify-between mb-2">
                      <select
                        value={method.type}
                        onChange={(e) => updatePaymentMethod(index, 'type', e.target.value)}
                        className="flex-1 px-2 py-1 border border-indigo-300 rounded focus:ring-indigo-500 focus:border-indigo-500 text-sm"
                      >
                        {paymentMethodOptions.map((option) => (
                          <option key={option.value} value={option.value}>
                            {option.label}
                          </option>
                        ))}
                      </select>
                      {paymentMethods.length > 1 && (
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => removePaymentMethod(index)}
                          className="ml-2 text-red-500 hover:text-red-700"
                        >
                          <Minus className="w-4 h-4" />
                        </Button>
                      )}
                    </div>
                    <Input
                      type="number"
                      step="0.01"
                      placeholder="0.00"
                      value={method.amount}
                      onChange={(e) => updatePaymentMethod(index, 'amount', Number(e.target.value))}
                      className="border-indigo-300 focus:ring-indigo-500 focus:border-indigo-500"
                    />
                    {['yape', 'plin', 'transfer'].includes(method.type) && (
                      <Input
                        placeholder="Número de referencia"
                        value={method.reference || ''}
                        onChange={(e) => updatePaymentMethod(index, 'reference', e.target.value)}
                        className="mt-2 border-indigo-300 focus:ring-indigo-500 focus:border-indigo-500"
                      />
                    )}
                  </div>
                ))}
                
                <div className="bg-white p-3 rounded-lg border-2 border-indigo-300">
                  <div className="flex justify-between mb-2">
                    <span className="font-medium">💰 Total a Pagar:</span>
                    <span className="font-bold text-green-600">S/ {total.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between mb-2">
                    <span className="font-medium">💵 Total Pagado:</span>
                    <span className="font-bold text-blue-600">S/ {totalPaid.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="font-medium">🔄 Vuelto:</span>
                    <span className={`font-bold ${change >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                      S/ {change.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Botones de Acción */}
          <div className="space-y-3">
            <Button
              className="w-full bg-gradient-to-r from-green-500 to-blue-500 hover:from-green-600 hover:to-blue-600"
              size="lg"
              onClick={handleCompleteSale}
              disabled={saleItems.length === 0 || totalPaid < total || !selectedCustomer}
            >
              ✅ Completar Venta
            </Button>
            <Button variant="outline" className="w-full border-gray-300 text-gray-700 hover:bg-gray-100">
              💾 Guardar Borrador
            </Button>
          </div>
        </div>
      </div>

      {/* Modal Nuevo Cliente */}
      <Modal
        isOpen={isCustomerModalOpen}
        onClose={() => setIsCustomerModalOpen(false)}
        title="👤 Nuevo Cliente"
        size="md"
      >
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input label="Nombre" placeholder="Nombre del cliente" id="newCustomerName" />
            <Input label="Apellido" placeholder="Apellido del cliente" id="newCustomerLastName" />
          </div>
          <Input label="Email" type="email" placeholder="cliente@email.com" id="newCustomerEmail" />
          <Input label="Teléfono" placeholder="+51 999 999 999" id="newCustomerPhone" />
          <Input label="Dirección" placeholder="Dirección completa" id="newCustomerAddress" />
          
          <div className="flex justify-end space-x-3 pt-6 border-t">
            <Button variant="ghost" onClick={() => setIsCustomerModalOpen(false)}>
              Cancelar
            </Button>
            <Button onClick={handleCreateCustomer} className="bg-gradient-to-r from-purple-500 to-pink-500">
              ✅ Crear Cliente
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}