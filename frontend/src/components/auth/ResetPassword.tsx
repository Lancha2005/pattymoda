// Componente para resetear contraseña
import { useState, useEffect } from 'react';
import { Lock, Eye, EyeOff, CheckCircle, AlertCircle } from 'lucide-react';
import { Button } from '../ui/Button';
import { Input } from '../ui/Input';
import { Card } from '../ui/Card';

interface ResetPasswordProps {
  token: string;
  onSuccess: () => void;
}

export function ResetPassword({ token, onSuccess }: ResetPasswordProps) {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isValidating, setIsValidating] = useState(true);
  const [isValidToken, setIsValidToken] = useState(false);
  const [error, setError] = useState('');
  const [isSuccess, setIsSuccess] = useState(false);

  useEffect(() => {
    validateToken();
  }, [token]);

  const validateToken = async () => {
    setIsValidating(true);
    try {
      const response = await fetch(`http://localhost:8080/api/auth/validate-reset-token?token=${token}`);
      const data = await response.json();
      
      if (response.ok && data.valid) {
        setIsValidToken(true);
      } else {
        setError('El enlace de recuperación es inválido o ha expirado');
        setIsValidToken(false);
      }
    } catch (err) {
      setError('Error al validar el enlace');
      setIsValidToken(false);
    } finally {
      setIsValidating(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (newPassword !== confirmPassword) {
      setError('Las contraseñas no coinciden');
      return;
    }

    if (newPassword.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/reset-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          token,
          newPassword,
          confirmPassword,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        setIsSuccess(true);
        setTimeout(() => {
          onSuccess();
        }, 3000);
      } else {
        setError(data.message || 'Error al cambiar contraseña');
      }
    } catch (err: any) {
      setError('Error de conexión. Intenta nuevamente.');
    } finally {
      setIsLoading(false);
    }
  };

  const getPasswordStrength = (password: string) => {
    if (password.length === 0) return { strength: 0, text: '', color: '' };
    if (password.length < 6) return { strength: 1, text: 'Muy débil', color: 'text-red-500' };
    if (password.length < 8) return { strength: 2, text: 'Débil', color: 'text-orange-500' };
    if (password.length < 12) return { strength: 3, text: 'Buena', color: 'text-yellow-500' };
    return { strength: 4, text: 'Fuerte', color: 'text-green-500' };
  };

  const passwordStrength = getPasswordStrength(newPassword);

  if (isValidating) {
    return (
      <Card className="p-8 max-w-md mx-auto">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-500 mx-auto mb-4"></div>
          <p className="text-gray-600">Validando enlace...</p>
        </div>
      </Card>
    );
  }

  if (!isValidToken) {
    return (
      <Card className="p-8 max-w-md mx-auto">
        <div className="text-center space-y-6">
          <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto">
            <AlertCircle className="w-8 h-8 text-red-600" />
          </div>
          
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              Enlace Inválido
            </h2>
            <p className="text-gray-600 mb-4">{error}</p>
          </div>

          <Button 
            onClick={() => window.location.href = '/'}
            className="w-full"
          >
            Volver al inicio
          </Button>
        </div>
      </Card>
    );
  }

  if (isSuccess) {
    return (
      <Card className="p-8 max-w-md mx-auto">
        <div className="text-center space-y-6">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto">
            <CheckCircle className="w-8 h-8 text-green-600" />
          </div>
          
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">
              ¡Contraseña Cambiada!
            </h2>
            <p className="text-gray-600">
              Tu contraseña ha sido actualizada exitosamente. 
              Serás redirigido al login en unos segundos.
            </p>
          </div>

          <div className="w-full bg-gray-200 rounded-full h-2">
            <div className="bg-green-500 h-2 rounded-full animate-pulse" style={{ width: '100%' }}></div>
          </div>
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-8 max-w-md mx-auto">
      <div className="text-center mb-8">
        <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <Lock className="w-8 h-8 text-green-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900 mb-2">
          Nueva Contraseña
        </h2>
        <p className="text-gray-600">
          Ingresa tu nueva contraseña para DPattyModa
        </p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-3">
            <p className="text-red-600 text-sm">{error}</p>
          </div>
        )}

        <div>
          <Input
            label="Nueva Contraseña"
            type={showPassword ? 'text' : 'password'}
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            leftIcon={<Lock className="w-4 h-4 text-gray-400" />}
            rightIcon={
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="text-gray-400 hover:text-gray-600"
              >
                {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
              </button>
            }
            placeholder="Mínimo 6 caracteres"
            required
          />
          
          {newPassword && (
            <div className="mt-2">
              <div className="flex items-center justify-between text-sm">
                <span className={passwordStrength.color}>
                  Seguridad: {passwordStrength.text}
                </span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-1 mt-1">
                <div 
                  className={`h-1 rounded-full transition-all duration-300 ${
                    passwordStrength.strength === 1 ? 'bg-red-500 w-1/4' :
                    passwordStrength.strength === 2 ? 'bg-orange-500 w-2/4' :
                    passwordStrength.strength === 3 ? 'bg-yellow-500 w-3/4' :
                    passwordStrength.strength === 4 ? 'bg-green-500 w-full' : 'w-0'
                  }`}
                ></div>
              </div>
            </div>
          )}
        </div>

        <Input
          label="Confirmar Contraseña"
          type={showConfirmPassword ? 'text' : 'password'}
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          leftIcon={<Lock className="w-4 h-4 text-gray-400" />}
          rightIcon={
            <button
              type="button"
              onClick={() => setShowConfirmPassword(!showConfirmPassword)}
              className="text-gray-400 hover:text-gray-600"
            >
              {showConfirmPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
            </button>
          }
          placeholder="Repite la contraseña"
          required
        />

        {confirmPassword && newPassword !== confirmPassword && (
          <p className="text-red-500 text-sm">Las contraseñas no coinciden</p>
        )}

        <Button
          type="submit"
          className="w-full"
          isLoading={isLoading}
          disabled={!newPassword || !confirmPassword || newPassword !== confirmPassword}
        >
          {isLoading ? 'Cambiando contraseña...' : 'Cambiar contraseña'}
        </Button>
      </form>

      <div className="mt-6 text-center">
        <p className="text-xs text-gray-500">
          Tu contraseña será encriptada y almacenada de forma segura
        </p>
      </div>
    </Card>
  );
}