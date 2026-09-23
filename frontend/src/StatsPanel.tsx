import type { Stats } from './types'

type Props = {
  stats: Stats
}

function StatsPanel({ stats }: Props) {
  // completionRate arrives as 0.0 to 1.0. The backend returns 0.0 for an empty
  // list rather than dividing by zero, so there is nothing to guard here.
  const percent = Math.round(stats.completionRate * 100)

  return (
    <div className="stats">
      <span><strong>{stats.completed}</strong> completed</span>
      <span><strong>{stats.overdue}</strong> overdue</span>
      <span><strong>{percent}%</strong> done</span>
    </div>
  )
}

export default StatsPanel
