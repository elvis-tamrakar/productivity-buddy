import { useMutation, useQuery } from '@tanstack/react-query';
import { authAPI } from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import { useRouter } from 'next/navigation';
import toast from 'react-hot-toast';

export const useLogin = () => {
  const { login, setLoading } = useAuthStore();
  const router = useRouter();

  return useMutation({
    mutationFn: authAPI.login,
    onMutate: () => {
      setLoading(true);
    },
    onSuccess: (response) => {
      const { token, user } = response.data;
      login(user, token);
      localStorage.setItem('authToken', token);
      toast.success('Login successful!');
      router.push('/dashboard');
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Login failed';
      toast.error(message);
    },
    onSettled: () => {
      setLoading(false);
    },
  });
};

export const useRegister = () => {
  const { login, setLoading } = useAuthStore();
  const router = useRouter();

  return useMutation({
    mutationFn: authAPI.register,
    onMutate: () => {
      setLoading(true);
    },
    onSuccess: (response) => {
      // For register, we need to login separately since the backend doesn't return a token
      const user = response.data;
      toast.success('Registration successful! Please login.');
      router.push('/login');
    },
    onError: (error: any) => {
      const message = error.response?.data?.message || 'Registration failed';
      toast.error(message);
    },
    onSettled: () => {
      setLoading(false);
    },
  });
};

export const useLogout = () => {
  const { logout } = useAuthStore();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    localStorage.removeItem('authToken');
    toast.success('Logged out successfully');
    router.push('/login');
  };

  return { logout: handleLogout };
};
