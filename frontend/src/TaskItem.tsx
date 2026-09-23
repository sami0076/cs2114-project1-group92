import type { Task } from './types'

export function isOverdue(task: Task): boolean {
  const today = new Date().toISOString().slice(0, 10)
  return !task.completed && task.deadline < today
}

type Props = {
  task: Task
}

function TaskItem({ task }: Props) {
  return (
    <li className={task.completed ? 'task completed' : 'task'}>
      <div className="task-head">
        <span className="task-name">{task.name}</span>
        <span className={'priority priority-' + task.priority}>{task.priority}</span>
      </div>

      <p className="task-description">{task.description}</p>

      <p className="task-meta">
        Due {task.deadline}
        {isOverdue(task) && <span className="overdue"> - Overdue</span>}
        {task.completed && <span> - Completed</span>}
      </p>
    </li>
  )
}

export default TaskItem
