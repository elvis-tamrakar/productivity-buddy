import axios from 'axios';

// API base URL - updated to match backend (no /api prefix)
const API_BASE_URL = 'http://localhost:8080';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Types - updated to match backend entities
export interface User {
  id: number;
  username: string;
  email: string;
  goals?: Goal[];
}

export interface Goal {
  id: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  status: 'ACTIVE' | 'COMPLETED' | 'PAUSED' | 'CANCELLED';
  progress: number;
  users?: User;
  checkpoints?: Checkpoint[];
}

export interface Checkpoint {
  id: number;
  title: string;
  description: string;
  goalId?: number;
  goal?: Goal;
  dueDate: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'OVERDUE';
  completedDate?: string;
}

export interface BuddyRequest {
  id: number;
  requesterId?: number;
  receiverId?: number;
  requester?: User;
  receiver?: User;
  date: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

// Auth API - updated to match backend endpoints
export const authAPI = {
  login: (data: LoginRequest) => 
    api.post<AuthResponse>('/users/login', data),
  
  register: (data: RegisterRequest) => 
    api.post<User>('/users/register', data),
};

// User API - updated to match backend endpoints
export const userAPI = {
  getProfile: (id: number) => 
    api.get<User>(`/users/${id}`),
  
  updateProfile: (id: number, data: Partial<User>) => 
    api.put<User>(`/users/${id}`, data),
  
  deleteProfile: (id: number) => 
    api.delete(`/users/${id}`),
};

// Goals API - updated to match backend endpoints
export const goalsAPI = {
  getGoals: (userId: number) => 
    api.get<Goal[]>(`/goals/user/${userId}`),
  
  createGoal: (userId: number, data: Omit<Goal, 'id' | 'users' | 'checkpoints'>) => 
    api.post<Goal>(`/goals/${userId}`, data),
  
  updateGoal: (id: number, data: Partial<Goal>) => 
    api.put<Goal>(`/goals/${id}`, data),
  
  deleteGoal: (id: number) => 
    api.delete(`/goals/${id}`),
  
  completeGoal: (id: number) => 
    api.post<Goal>(`/goals/${id}/complete`),
};

// Checkpoints API - updated to match backend endpoints
export const checkpointsAPI = {
  getCheckpoints: () => 
    api.get<Checkpoint[]>('/checkpoints'),
  
  createCheckpoint: (data: Omit<Checkpoint, 'id' | 'goal' | 'completedDate'>) => 
    api.post<Checkpoint>('/checkpoints', data),
  
  updateCheckpoint: (id: number, data: Partial<Checkpoint>) => 
    api.put<Checkpoint>(`/checkpoints/${id}`, data),
  
  deleteCheckpoint: (id: number) => 
    api.delete(`/checkpoints/${id}`),
};

// Buddy Requests API - updated to match backend endpoints
export const buddyRequestsAPI = {
  getBuddyRequests: () => 
    api.get<BuddyRequest[]>('/buddies'),
  
  createBuddyRequest: (data: Omit<BuddyRequest, 'id' | 'requester' | 'receiver'>) => 
    api.post<BuddyRequest>('/buddies', data),
  
  updateBuddyRequest: (id: number, data: Partial<BuddyRequest>) => 
    api.put<BuddyRequest>(`/buddies/${id}`, data),
};

export default api;
