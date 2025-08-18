'use client';

import { Goal, Checkpoint } from '@/lib/api';
import { ClockIcon, CheckCircleIcon, FlagIcon } from '@heroicons/react/24/outline';

interface RecentActivityProps {
  goals: Goal[];
  checkpoints: Checkpoint[];
}

export default function RecentActivity({ goals, checkpoints }: RecentActivityProps) {
  // Create activity items from goals and checkpoints
  const activities = [
    ...goals.map(goal => ({
      id: `goal-${goal.id}`,
      type: 'goal' as const,
      title: goal.title,
      status: goal.status,
      date: goal.startDate,
      icon: FlagIcon,
    })),
    ...checkpoints.map(checkpoint => ({
      id: `checkpoint-${checkpoint.id}`,
      type: 'checkpoint' as const,
      title: checkpoint.title,
      status: checkpoint.status,
      date: checkpoint.dueDate,
      icon: CheckCircleIcon,
    })),
  ].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
   .slice(0, 10);

  if (activities.length === 0) {
    return (
      <div className="text-center py-8">
        <ClockIcon className="mx-auto h-12 w-12 text-gray-400" />
        <h3 className="mt-2 text-sm font-medium text-gray-900">No recent activity</h3>
        <p className="mt-1 text-sm text-gray-500">
          Start creating goals and checkpoints to see your activity here.
        </p>
      </div>
    );
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return 'text-green-600 bg-green-100';
      case 'ACTIVE':
        return 'text-blue-600 bg-blue-100';
      case 'PAUSED':
        return 'text-yellow-600 bg-yellow-100';
      case 'IN_PROGRESS':
        return 'text-purple-600 bg-purple-100';
      case 'PENDING':
        return 'text-gray-600 bg-gray-100';
      default:
        return 'text-gray-600 bg-gray-100';
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return 'Completed';
      case 'ACTIVE':
        return 'Active';
      case 'PAUSED':
        return 'Paused';
      case 'IN_PROGRESS':
        return 'In Progress';
      case 'PENDING':
        return 'Pending';
      default:
        return status;
    }
  };

  return (
    <div className="flow-root">
      <ul className="-mb-8">
        {activities.map((activity, activityIdx) => (
          <li key={activity.id}>
            <div className="relative pb-8">
              {activityIdx !== activities.length - 1 ? (
                <span
                  className="absolute top-4 left-4 -ml-px h-full w-0.5 bg-gray-200"
                  aria-hidden="true"
                />
              ) : null}
              <div className="relative flex space-x-3">
                <div>
                  <span className="h-8 w-8 rounded-full bg-gray-100 flex items-center justify-center ring-8 ring-white">
                    <activity.icon className="h-5 w-5 text-gray-500" />
                  </span>
                </div>
                <div className="flex min-w-0 flex-1 justify-between space-x-4 pt-1.5">
                  <div>
                    <p className="text-sm text-gray-500">
                      {activity.type === 'goal' ? 'Goal' : 'Checkpoint'}{' '}
                      <span className="font-medium text-gray-900">{activity.title}</span>
                    </p>
                    <div className="mt-1">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(activity.status)}`}>
                        {getStatusText(activity.status)}
                      </span>
                    </div>
                  </div>
                  <div className="whitespace-nowrap text-right text-sm text-gray-500">
                    {new Date(activity.date).toLocaleDateString()}
                  </div>
                </div>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
