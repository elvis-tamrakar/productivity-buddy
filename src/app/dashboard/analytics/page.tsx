'use client';

import DashboardLayout from '@/components/layout/DashboardLayout';
import { ChartBarIcon } from '@heroicons/react/24/outline';

export default function AnalyticsPage() {
  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Analytics</h1>
          <p className="mt-1 text-sm text-gray-500">
            Detailed insights and progress tracking to help you stay motivated.
          </p>
        </div>

        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Progress Insights</h2>
          </div>
          <div className="p-6">
            <div className="text-center py-8">
              <ChartBarIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">Analytics feature coming soon</h3>
              <p className="mt-1 text-sm text-gray-500">
                We're working on adding comprehensive analytics and insights to help you track your progress over time.
              </p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
