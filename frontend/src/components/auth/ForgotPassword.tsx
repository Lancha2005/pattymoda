// Componente para solicitar recuperación de contraseña
import { useState } from 'react';
import { Mail, ArrowLeft, Send, CheckCircle } from 'lucide-react';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Card } from '../ui/Card';

interface ForgotPasswordProps {
  onBackToLogin: () => void;
}

export function ForgotPassword({ onBackToLogin }: ForgotPasswordProps) {
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });

      const data = await response.json();

      if (response.ok) {
        setIsSuccess(true);
      } else {
        setError(data.message || 'Error al enviar solicitud');
      }
    } catch (err: any) {
      setError('Error de conexión. Intenta nuevamente.');
    } finally {
      setIsLoading(false);
    }
  };

  if (isSuccess) {
    return (
      <Card className="p-8 max-w-md mx-auto">
        <div className="text-center space-y-6">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto">
            <CheckCircle className="w-8 h-8 text-green-600" />
          </div>
          
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              ¡Email Enviado!
            </h2>
            <p className="text-gray-600">
              Si el email <strong>{email}</strong> está registrado, recibirás un enlace para restablecer tu contraseña.
            </p>
          </div>

          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p className="text-sm text-blue-800">
              <strong>Nota:</strong> El enlace expirará en 1 hora por seguridad. 
              Revisa también tu carpeta de spam.
            </p>
          </div>

          <div className="space-y-3">
            <Button 
              onClick={() => {
                setIsSuccess(false);
                setEmail('');
              }}
              variant="outline"
              className="w-full"
            >
              Enviar otro enlace
            </Button>
            
            <Button 
              onClick={onBackToLogin}
              variant="ghost"
              className="w-full"
              leftIcon={<ArrowLeft className="w-4 h-4" />}
            >
              Volver al login
            </Button>
          </div>
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-8 max-w-md mx-auto">
      <div className="text-center mb-8">
        <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <Mail className="w-8 h-8 text-blue-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">
          ¿Olvidaste tu contraseña?
        </h2>
        <p className="text-gray-600">
          Ingresa tu email y te enviaremos un enlace para restablecer tu contraseña
        </p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-3">
            <p className="text-red-600 text-sm">{error}</p>
          </div>
        )}

        <Input
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          leftIcon={<Mail className="w-4 h-4 text-gray-400" />}
          placeholder="tu@email.com"
          required
        />

        <Button
          type="submit"
          className="w-full"
          isLoading={isLoading}
          leftIcon={!isLoading ? <Send className="w-4 h-4" /> : undefined}
        >
          {isLoading ? 'Enviando...' : 'Enviar enlace de recuperación'}
        </Button>

        <Button
          type="button"
          variant="ghost"
          className="w-full"
          onClick={onBackToLogin}
          leftIcon={<ArrowLeft className="w-4 h-4" />}
        >
          Volver al login
        </Button>
      </form>

      <div className="mt-6 text-center">
        <p className="text-xs text-gray-500">
          ¿Problemas? Contacta a soporte: info@dpattymoda.com
        </p>
      </div>
    </Card>
  );
}