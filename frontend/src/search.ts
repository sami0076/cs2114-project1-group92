import type { Task } from './types'

export type TaskRow = {
  task: Task
  index: number
}

export function matchTasks(tasks: Task[], query: string): TaskRow[] {
  const rows = tasks.map((task, index) => ({ task, index }))
  const needle = query.trim().toLowerCase()

  if (needle === '') {
    return rows
  }

  return rows.filter(({ task }) =>
    task.name.toLowerCase().includes(needle) ||
    task.description.toLowerCase().includes(needle)
  )
}
