'use client';

import DashboardLayout from '@/components/layout/DashboardLayout';
import { CheckCircleIcon } from '@heroicons/react/24/outline';

export default function CheckpointsPage() {
  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Checkpoints</h1>
          <p className="mt-1 text-sm text-gray-500">
            Break down your goals into manageable milestones and track your progress.
          </p>
        </div>

        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Goal Milestones</h2>
          </div>
          <div className="p-6">
            <div className="text-center py-8">
              <CheckCircleIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">Checkpoints feature coming soon</h3>
              <p className="mt-1 text-sm text-gray-500">
                We're working on adding checkpoint functionality to help you break down your goals into smaller, manageable tasks.
              </p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
