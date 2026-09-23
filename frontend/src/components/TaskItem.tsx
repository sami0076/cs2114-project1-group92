import { isOverdue } from '../overdue'
import type { Task } from '../types'

type Props = {
  task: Task
  index: number
  busy: boolean
  onEdit: (index: number) => void
  onComplete: (index: number) => void
  onDelete: (index: number) => void
}

function TaskItem({ task, index, busy, onEdit, onComplete, onDelete }: Props) {
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
        <button type="button" disabled={busy} onClick={() => onEdit(index)}>
          Edit
        </button>

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
