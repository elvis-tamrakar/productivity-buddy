'use client';

import { useState } from 'react';
import { authAPI } from '@/lib/api';
import toast from 'react-hot-toast';

export default function TestConnection() {
  const [isLoading, setIsLoading] = useState(false);
  const [testResult, setTestResult] = useState<string>('');

  const testConnection = async () => {
    setIsLoading(true);
    setTestResult('');
    
    try {
      // Test registration
      const registerResponse = await authAPI.register({
        username: 'testuser',
        email: 'test@example.com',
        password: 'password123'
      });
      
      setTestResult('✅ Registration successful! User ID: ' + registerResponse.data.id);
      toast.success('Backend connection successful!');
      
    } catch (error: any) {
      const message = error.response?.data?.message || error.message || 'Unknown error';
      setTestResult('❌ Connection failed: ' + message);
      toast.error('Backend connection failed: ' + message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <h2 className="text-lg font-medium text-gray-900 mb-4">Backend Connection Test</h2>
      
      <button
        onClick={testConnection}
        disabled={isLoading}
        className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
      >
        {isLoading ? 'Testing...' : 'Test Connection'}
      </button>
      
      {testResult && (
        <div className="mt-4 p-3 bg-gray-50 rounded-md">
          <p className="text-sm font-mono">{testResult}</p>
        </div>
      )}
      
      <div className="mt-4 text-xs text-gray-500">
        <p>This will test the connection to your Spring Boot backend at http://localhost:8080</p>
        <p>Make sure your backend is running and PostgreSQL is available.</p>
      </div>
    </div>
  );
}
