'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { goalsAPI } from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import DashboardLayout from '@/components/layout/DashboardLayout';
import GoalCard from '@/components/goals/GoalCard';
import CreateGoalModal from '@/components/goals/CreateGoalModal';
import StatsCard from '@/components/dashboard/StatsCard';
import {
  FlagIcon,
  CheckCircleIcon,
  ClockIcon,
  PauseIcon,
} from '@heroicons/react/24/outline';

export default function GoalsPage() {
  const { user } = useAuthStore();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [statusFilter, setStatusFilter] = useState<string>('all');

  const { data: goals = [], isLoading } = useQuery({
    queryKey: ['goals', user?.id],
    queryFn: () => goalsAPI.getGoals(user!.id).then(res => res.data),
    enabled: !!user?.id,
  });

  const completedGoals = goals.filter(goal => goal.status === 'COMPLETED');
  const activeGoals = goals.filter(goal => goal.status === 'ACTIVE');
  const pausedGoals = goals.filter(goal => goal.status === 'PAUSED');
  const cancelledGoals = goals.filter(goal => goal.status === 'CANCELLED');

  const stats = [
    {
      name: 'Total Goals',
      value: goals.length,
      icon: FlagIcon,
      color: 'bg-blue-500',
    },
    {
      name: 'Active',
      value: activeGoals.length,
      icon: ClockIcon,
      color: 'bg-green-500',
    },
    {
      name: 'Completed',
      value: completedGoals.length,
      icon: CheckCircleIcon,
      color: 'bg-emerald-500',
    },
    {
      name: 'Paused',
      value: pausedGoals.length,
      icon: PauseIcon,
      color: 'bg-yellow-500',
    },
  ];

  const filteredGoals = goals.filter(goal => {
    if (statusFilter === 'all') return true;
    return goal.status === statusFilter;
  });

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Goals</h1>
            <p className="mt-1 text-sm text-gray-500">
              Manage and track your personal goals.
            </p>
          </div>
          <button
            onClick={() => setIsCreateModalOpen(true)}
            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            <FlagIcon className="h-4 w-4 mr-2" />
            Create Goal
          </button>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {stats.map((stat) => (
            <StatsCard key={stat.name} {...stat} />
          ))}
        </div>

        {/* Filter */}
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex items-center space-x-4">
            <label className="text-sm font-medium text-gray-700">Filter by status:</label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
            >
              <option value="all">All Goals</option>
              <option value="ACTIVE">Active</option>
              <option value="COMPLETED">Completed</option>
              <option value="PAUSED">Paused</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>
        </div>

        {/* Goals List */}
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">
              {statusFilter === 'all' ? 'All Goals' : `${statusFilter.charAt(0) + statusFilter.slice(1).toLowerCase()} Goals`}
              <span className="ml-2 text-sm text-gray-500">({filteredGoals.length})</span>
            </h2>
          </div>
          <div className="p-6">
            {isLoading ? (
              <div className="text-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600 mx-auto"></div>
                <p className="mt-2 text-sm text-gray-500">Loading goals...</p>
              </div>
            ) : filteredGoals.length === 0 ? (
              <div className="text-center py-8">
                <FlagIcon className="mx-auto h-12 w-12 text-gray-400" />
                <h3 className="mt-2 text-sm font-medium text-gray-900">No goals found</h3>
                <p className="mt-1 text-sm text-gray-500">
                  {statusFilter === 'all' 
                    ? "Get started by creating your first goal."
                    : `No ${statusFilter.toLowerCase()} goals found.`
                  }
                </p>
                {statusFilter === 'all' && (
                  <button
                    onClick={() => setIsCreateModalOpen(true)}
                    className="mt-4 inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
                  >
                    Create Goal
                  </button>
                )}
              </div>
            ) : (
              <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
                {filteredGoals.map((goal) => (
                  <GoalCard key={goal.id} goal={goal} />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      <CreateGoalModal
        isOpen={isCreateModalOpen}
        onClose={() => setIsCreateModalOpen(false)}
      />
    </DashboardLayout>
  );
}
