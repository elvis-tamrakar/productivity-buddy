'use client';

import { useQuery } from '@tanstack/react-query';
import { goalsAPI, checkpointsAPI, buddyRequestsAPI } from '@/lib/api';
import { useAuthStore } from '@/store/authStore';
import DashboardLayout from '@/components/layout/DashboardLayout';
import GoalCard from '@/components/goals/GoalCard';
import ProgressChart from '@/components/charts/ProgressChart';
import StatsCard from '@/components/dashboard/StatsCard';
import RecentActivity from '@/components/dashboard/RecentActivity';
import {
  FlagIcon,
  CheckCircleIcon,
  UserGroupIcon,
  ClockIcon,
} from '@heroicons/react/24/outline';

export default function DashboardPage() {
  const { user } = useAuthStore();
  
  const { data: goals = [] } = useQuery({
    queryKey: ['goals', user?.id],
    queryFn: () => goalsAPI.getGoals(user!.id).then(res => res.data),
    enabled: !!user?.id,
  });

  const { data: checkpoints = [] } = useQuery({
    queryKey: ['checkpoints'],
    queryFn: () => checkpointsAPI.getCheckpoints().then(res => res.data),
  });

  const { data: buddyRequests = [] } = useQuery({
    queryKey: ['buddy-requests'],
    queryFn: () => buddyRequestsAPI.getBuddyRequests().then(res => res.data),
  });

  const completedGoals = goals.filter(goal => goal.status === 'COMPLETED');
  const activeGoals = goals.filter(goal => goal.status === 'ACTIVE');
  const pausedGoals = goals.filter(goal => goal.status === 'PAUSED');
  const completedCheckpoints = checkpoints.filter(cp => cp.status === 'COMPLETED');
  const pendingCheckpoints = checkpoints.filter(cp => cp.status === 'PENDING');

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
      color: 'bg-yellow-500',
    },
    {
      name: 'Completed',
      value: completedGoals.length,
      icon: CheckCircleIcon,
      color: 'bg-green-500',
    },
    {
      name: 'Buddy Requests',
      value: buddyRequests.filter(br => br.status === 'PENDING').length,
      icon: UserGroupIcon,
      color: 'bg-purple-500',
    },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
          <p className="mt-1 text-sm text-gray-500">
            Track your progress and stay motivated with your goals.
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {stats.map((stat) => (
            <StatsCard key={stat.name} {...stat} />
          ))}
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
          {/* Goals Overview */}
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">Recent Goals</h2>
            </div>
            <div className="p-6">
              {goals.length === 0 ? (
                <div className="text-center py-8">
                  <FlagIcon className="mx-auto h-12 w-12 text-gray-400" />
                  <h3 className="mt-2 text-sm font-medium text-gray-900">No goals yet</h3>
                  <p className="mt-1 text-sm text-gray-500">
                    Get started by creating your first goal.
                  </p>
                </div>
              ) : (
                <div className="space-y-4">
                  {goals.slice(0, 3).map((goal) => (
                    <GoalCard key={goal.id} goal={goal} compact />
                  ))}
                </div>
              )}
            </div>
          </div>

          {/* Progress Chart */}
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">Progress Overview</h2>
            </div>
            <div className="p-6">
              <ProgressChart goals={goals} />
            </div>
          </div>
        </div>

        {/* Checkpoints and Activity */}
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
          {/* Checkpoints */}
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">Recent Checkpoints</h2>
            </div>
            <div className="p-6">
              {checkpoints.length === 0 ? (
                <div className="text-center py-8">
                  <CheckCircleIcon className="mx-auto h-12 w-12 text-gray-400" />
                  <h3 className="mt-2 text-sm font-medium text-gray-900">No checkpoints</h3>
                  <p className="mt-1 text-sm text-gray-500">
                    Create checkpoints to break down your goals into smaller tasks.
                  </p>
                </div>
              ) : (
                <div className="space-y-3">
                  {checkpoints.slice(0, 5).map((checkpoint) => (
                    <div
                      key={checkpoint.id}
                      className="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
                    >
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          {checkpoint.title}
                        </p>
                        <p className="text-xs text-gray-500">
                          Due: {new Date(checkpoint.dueDate).toLocaleDateString()}
                        </p>
                      </div>
                      <div
                        className={`w-3 h-3 rounded-full ${
                          checkpoint.status === 'COMPLETED' ? 'bg-green-500' : 'bg-yellow-500'
                        }`}
                      />
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>

          {/* Recent Activity */}
          <div className="bg-white rounded-lg shadow">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">Recent Activity</h2>
            </div>
            <div className="p-6">
              <RecentActivity goals={goals} checkpoints={checkpoints} />
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
