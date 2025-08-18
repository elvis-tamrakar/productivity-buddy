'use client';

import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts';
import { Goal } from '@/lib/api';

interface ProgressChartProps {
  goals: Goal[];
}

export default function ProgressChart({ goals }: ProgressChartProps) {
  const statusCounts = goals.reduce((acc, goal) => {
    acc[goal.status] = (acc[goal.status] || 0) + 1;
    return acc;
  }, {} as Record<string, number>);

  const data = [
    { name: 'Active', value: statusCounts['ACTIVE'] || 0, color: '#3B82F6' },
    { name: 'Completed', value: statusCounts['COMPLETED'] || 0, color: '#10B981' },
    { name: 'Paused', value: statusCounts['PAUSED'] || 0, color: '#F59E0B' },
    { name: 'Cancelled', value: statusCounts['CANCELLED'] || 0, color: '#EF4444' },
  ].filter(item => item.value > 0);

  if (data.length === 0) {
    return (
      <div className="text-center py-8">
        <p className="text-sm text-gray-500">No goals to display</p>
      </div>
    );
  }

  return (
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          labelLine={false}
          label={({ name, percent }) => `${name} ${((percent || 0) * 100).toFixed(0)}%`}
          outerRadius={80}
          fill="#8884d8"
          dataKey="value"
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={entry.color} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  );
}
