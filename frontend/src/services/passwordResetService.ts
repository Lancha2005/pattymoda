// Servicio para recuperación de contraseña
import { apiService } from './api';
import { ApiResponse } from '../types';

interface ForgotPasswordRequest {
  email: string;
}

interface ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

interface TokenValidationResponse {
  valid: boolean;
  message: string;
}

export class PasswordResetService {
  static async requestPasswordReset(email: string): Promise<ApiResponse<string>> {
    return apiService.post<string>('/auth/forgot-password', { email });
  }

  static async resetPassword(data: ResetPasswordRequest): Promise<ApiResponse<string>> {
    return apiService.post<string>('/auth/reset-password', data);
  }

  static async validateResetToken(token: string): Promise<ApiResponse<TokenValidationResponse>> {
    return apiService.get<TokenValidationResponse>(`/auth/validate-reset-token?token=${token}`);
  }
}