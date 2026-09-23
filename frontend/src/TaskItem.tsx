import type { Task } from './types'

export function isOverdue(task: Task): boolean {
  const today = new Date().toISOString().slice(0, 10)
  return !task.completed && task.deadline < today
}

type Props = {
  task: Task
  index: number
  busy: boolean
  onComplete: (index: number) => void
  onDelete: (index: number) => void
}

function TaskItem({ task, index, busy, onComplete, onDelete }: Props) {
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

      <div className="task-actions">
        {!task.completed && (
          <button type="button" disabled={busy} onClick={() => onComplete(index)}>
            Complete
          </button>
        )}
        <button
          type="button"
          className="danger"
          disabled={busy}
          onClick={() => onDelete(index)}
        >
          Delete
        </button>
      </div>
    </li>
  )
}

export default TaskItem
