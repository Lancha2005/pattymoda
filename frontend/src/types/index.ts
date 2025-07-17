// Interfaces y tipos actualizados para el sistema DPattyModa
export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'SUPER_ADMIN' | 'ADMIN' | 'MANAGER' | 'VENDEDOR' | 'CAJERO' | 'INVENTARIO';
  avatar?: string;
  phone?: string;
  address?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Product {
  id: string;
  nombre: string;
  descripcion: string;
  precio: number;
  costo: number;
  sku: string;
  categoria: Category;
  marca: string;
  tallas: Size[];
  colores: Color[];
  imagenes: string[];
  stock: number;
  stockMinimo: number;
  activo: boolean;
  fechaCreacion: Date;
  fechaActualizacion: Date;
}

export interface Category {
  id: string;
  nombre: string;
  descripcion?: string;
  categoriaPadreId?: string;
  activo: boolean;
  fechaCreacion: string;
}

export interface Size {
  id: string;
  talla: string;
  stockTalla: number;
}

export interface Color {
  id: string;
  color: string;
  codigoHex: string;
  stockColor: number;
}

export interface Customer {
  id: string;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  direccion: string;
  distrito: string;
  ciudad: string;
  totalCompras: number;
  ultimaCompra?: Date;
  metodoPagoPreferido: string;
  fechaCreacion: Date;
  activo: boolean;
}

export interface Sale {
  id: string;
  customer: Customer;
  items: SaleItem[];
  subtotal: number;
  impuesto: number;
  descuento: number;
  total: number;
  paymentMethod: 'EFECTIVO' | 'TARJETA' | 'TRANSFERENCIA' | 'YAPE' | 'PLIN';
  status: 'PENDIENTE' | 'PAGADA' | 'ANULADA';
  notes?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface SaleItem {
  id: string;
  product: Product;
  quantity: number;
  talla: string;
  color: string;
  precioUnitario: number;
  subtotal: number;
}

export interface DashboardStats {
  totalSales: number;
  totalProducts: number;
  totalCustomers: number;
  monthlyRevenue: number;
  dailySales: number;
  lowStockProducts: number;
  topSellingProducts: Product[];
  recentSales: Sale[];
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: Date;
}

export interface StoreConfig {
  id: string;
  nombre: string;
  direccion: string;
  logo?: string;
  telefono?: string;
  email?: string;
  sitioWeb?: string;
  facebook?: string;
  instagram?: string;
  horario?: string;
}

export interface SalesReport {
  totalSales: number;
  totalRevenue: number;
  salesByDate: Array<{ date: string; total: number }>;
  salesByProduct: Array<{ product: string; total: number }>;
  salesByCustomer: Array<{ customer: string; total: number }>;
}

export interface ProductReport {
  topSellingProducts: Product[];
  lowStockProducts: Product[];
  productStats: Array<{ product: string; sold: number }>;
}

export interface CustomerReport {
  topCustomers: Customer[];
  customerStats: Array<{ customer: string; purchases: number }>;
}

export interface Invoice {
  id: string;
  saleId: string;
  type: 'BOLETA' | 'FACTURA';
  serie: string;
  numero: string;
  fechaEmision: Date;
  cliente: Customer;
  items: SaleItem[];
  subtotal: number;
  igv: number;
  total: number;
  qrCode: string;
  estado: 'EMITIDA' | 'ANULADA';
}

export interface PaymentMethod {
  type: 'EFECTIVO' | 'TARJETA' | 'YAPE' | 'PLIN' | 'TRANSFERENCIA';
  amount: number;
  reference?: string;
  cardType?: string;
  cardLast4?: string;
}