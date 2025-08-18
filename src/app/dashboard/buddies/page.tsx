'use client';

import { useQuery } from '@tanstack/react-query';
import { buddyRequestsAPI } from '@/lib/api';
import DashboardLayout from '@/components/layout/DashboardLayout';
import StatsCard from '@/components/dashboard/StatsCard';
import {
  UserGroupIcon,
  ClockIcon,
  CheckCircleIcon,
  XCircleIcon,
} from '@heroicons/react/24/outline';

export default function BuddiesPage() {
  const { data: buddyRequests = [] } = useQuery({
    queryKey: ['buddy-requests'],
    queryFn: () => buddyRequestsAPI.getBuddyRequests().then(res => res.data),
  });

  const pendingRequests = buddyRequests.filter(br => br.status === 'PENDING');
  const acceptedRequests = buddyRequests.filter(br => br.status === 'ACCEPTED');
  const rejectedRequests = buddyRequests.filter(br => br.status === 'REJECTED');

  const stats = [
    {
      name: 'Pending Requests',
      value: pendingRequests.length,
      icon: ClockIcon,
      color: 'bg-yellow-500',
    },
    {
      name: 'Accepted',
      value: acceptedRequests.length,
      icon: CheckCircleIcon,
      color: 'bg-green-500',
    },
    {
      name: 'Rejected',
      value: rejectedRequests.length,
      icon: XCircleIcon,
      color: 'bg-red-500',
    },
    {
      name: 'Total Buddies',
      value: acceptedRequests.length,
      icon: UserGroupIcon,
      color: 'bg-blue-500',
    },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Buddies</h1>
          <p className="mt-1 text-sm text-gray-500">
            Manage your buddy requests and collaborations.
          </p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {stats.map((stat) => (
            <StatsCard key={stat.name} {...stat} />
          ))}
        </div>

        {/* Buddy Requests */}
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Buddy Requests</h2>
          </div>
          <div className="p-6">
            {buddyRequests.length === 0 ? (
              <div className="text-center py-8">
                <UserGroupIcon className="mx-auto h-12 w-12 text-gray-400" />
                <h3 className="mt-2 text-sm font-medium text-gray-900">No buddy requests</h3>
                <p className="mt-1 text-sm text-gray-500">
                  You haven't received any buddy requests yet.
                </p>
              </div>
            ) : (
              <div className="space-y-4">
                {buddyRequests.map((request) => (
                  <div
                    key={request.id}
                    className="flex items-center justify-between p-4 border border-gray-200 rounded-lg"
                  >
                    <div className="flex items-center space-x-3">
                      <div className="h-10 w-10 rounded-full bg-indigo-600 flex items-center justify-center">
                        <span className="text-sm font-medium text-white">
                          {request.requester?.username?.[0] || 'U'}
                        </span>
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          {request.requester?.username || 'Unknown User'}
                        </p>
                        <p className="text-xs text-gray-500">
                          Requested on {new Date(request.date).toLocaleDateString()}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                          request.status === 'PENDING'
                            ? 'bg-yellow-100 text-yellow-800'
                            : request.status === 'ACCEPTED'
                            ? 'bg-green-100 text-green-800'
                            : 'bg-red-100 text-red-800'
                        }`}
                      >
                        {request.status}
                      </span>
                      {request.status === 'PENDING' && (
                        <div className="flex space-x-2">
                          <button className="inline-flex items-center px-3 py-1 border border-transparent text-xs font-medium rounded-md text-white bg-green-600 hover:bg-green-700">
                            Accept
                          </button>
                          <button className="inline-flex items-center px-3 py-1 border border-gray-300 text-xs font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50">
                            Reject
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Coming Soon Section */}
        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Collaboration Features</h2>
          </div>
          <div className="p-6">
            <div className="text-center py-8">
              <UserGroupIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">Coming Soon</h3>
              <p className="mt-1 text-sm text-gray-500">
                We're working on exciting collaboration features including shared goals, 
                progress tracking, and team challenges.
              </p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
