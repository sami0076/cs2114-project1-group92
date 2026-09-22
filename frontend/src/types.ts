export type Priority = 'LOW' | 'MEDIUM' | 'HIGH'

/** One task, exactly as GET /tasks returns it. */
export type Task = {
  name: string
  description: string
  deadline: string
  priority: Priority
  completed: boolean
}

export type TaskInput = Omit<Task, 'completed'>

export type Stats = {
  completed: number
  overdue: number
  completionRate: number
}
