'use client';

import { useState } from 'react';
import { Goal } from '@/lib/api';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { goalsAPI } from '@/lib/api';
import toast from 'react-hot-toast';
import {
  FlagIcon,
  CheckCircleIcon,
  ClockIcon,
  PencilIcon,
  TrashIcon,
  EllipsisVerticalIcon,
} from '@heroicons/react/24/outline';

interface GoalCardProps {
  goal: Goal;
  compact?: boolean;
}

export default function GoalCard({ goal, compact = false }: GoalCardProps) {
  const [showMenu, setShowMenu] = useState(false);
  const queryClient = useQueryClient();

  const updateGoalMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<Goal> }) =>
      goalsAPI.updateGoal(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['goals'] });
      toast.success('Goal updated successfully');
    },
    onError: () => {
      toast.error('Failed to update goal');
    },
  });

  const deleteGoalMutation = useMutation({
    mutationFn: (id: number) => goalsAPI.deleteGoal(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['goals'] });
      toast.success('Goal deleted successfully');
    },
    onError: () => {
      toast.error('Failed to delete goal');
    },
  });

  const completeGoalMutation = useMutation({
    mutationFn: (id: number) => goalsAPI.completeGoal(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['goals'] });
      toast.success('Goal completed successfully');
    },
    onError: () => {
      toast.error('Failed to complete goal');
    },
  });

  const getStatusIcon = (status: Goal['status']) => {
    switch (status) {
      case 'COMPLETED':
        return <CheckCircleIcon className="h-5 w-5 text-green-500" />;
      case 'ACTIVE':
        return <ClockIcon className="h-5 w-5 text-blue-500" />;
      case 'PAUSED':
        return <ClockIcon className="h-5 w-5 text-yellow-500" />;
      default:
        return <FlagIcon className="h-5 w-5 text-gray-500" />;
    }
  };

  const getStatusColor = (status: Goal['status']) => {
    switch (status) {
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'ACTIVE':
        return 'bg-blue-100 text-blue-800';
      case 'PAUSED':
        return 'bg-yellow-100 text-yellow-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusText = (status: Goal['status']) => {
    switch (status) {
      case 'COMPLETED':
        return 'Completed';
      case 'ACTIVE':
        return 'Active';
      case 'PAUSED':
        return 'Paused';
      case 'CANCELLED':
        return 'Cancelled';
      default:
        return status;
    }
  };

  const handleStatusChange = (newStatus: Goal['status']) => {
    if (newStatus === 'COMPLETED') {
      completeGoalMutation.mutate(goal.id);
    } else {
      updateGoalMutation.mutate({ id: goal.id, data: { status: newStatus } });
    }
    setShowMenu(false);
  };

  const handleDelete = () => {
    if (confirm('Are you sure you want to delete this goal?')) {
      deleteGoalMutation.mutate(goal.id);
    }
    setShowMenu(false);
  };

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between">
        <div className="flex-1 min-w-0">
          <div className="flex items-center space-x-2">
            {getStatusIcon(goal.status)}
            <h3 className="text-sm font-medium text-gray-900 truncate">
              {goal.title}
            </h3>
          </div>
          
          {!compact && (
            <p className="mt-1 text-sm text-gray-500 line-clamp-2">
              {goal.description}
            </p>
          )}

          <div className="mt-3 flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(goal.status)}`}>
                {getStatusText(goal.status)}
              </span>
              <span className="text-xs text-gray-500">
                Due: {new Date(goal.endDate).toLocaleDateString()}
              </span>
            </div>
            
            {!compact && (
              <div className="text-xs text-gray-500">
                {goal.progress}% complete
              </div>
            )}
          </div>

          {!compact && (
            <div className="mt-3">
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-indigo-600 h-2 rounded-full transition-all duration-300"
                  style={{ width: `${goal.progress}%` }}
                />
              </div>
            </div>
          )}
        </div>

        <div className="relative ml-4">
          <button
            onClick={() => setShowMenu(!showMenu)}
            className="text-gray-400 hover:text-gray-600"
          >
            <EllipsisVerticalIcon className="h-5 w-5" />
          </button>

          {showMenu && (
            <div className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg z-10 border border-gray-200">
              <div className="py-1">
                {goal.status !== 'COMPLETED' && (
                  <button
                    onClick={() => handleStatusChange('COMPLETED')}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Mark as completed
                  </button>
                )}
                {goal.status === 'PAUSED' && (
                  <button
                    onClick={() => handleStatusChange('ACTIVE')}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Resume goal
                  </button>
                )}
                {goal.status === 'ACTIVE' && (
                  <button
                    onClick={() => handleStatusChange('PAUSED')}
                    className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    Pause goal
                  </button>
                )}
                <button
                  onClick={() => {/* TODO: Implement edit */}}
                  className="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                >
                  <PencilIcon className="inline h-4 w-4 mr-2" />
                  Edit
                </button>
                <button
                  onClick={handleDelete}
                  className="block w-full text-left px-4 py-2 text-sm text-red-700 hover:bg-red-50"
                >
                  <TrashIcon className="inline h-4 w-4 mr-2" />
                  Delete
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
