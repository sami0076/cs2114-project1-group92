import type { Task } from './types'

// A task is overdue only when it is still open and its deadline has passed.
// Both values are ISO yyyy-MM-dd, so comparing them as strings is correct and
// avoids any timezone handling.
export function isOverdue(task: Task): boolean {
  const today = new Date().toISOString().slice(0, 10)
  return !task.completed && task.deadline < today
}
