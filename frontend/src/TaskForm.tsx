import { useState } from 'react'
import type { FormEvent } from 'react'
import type { Priority, TaskInput } from './types'

const PRIORITIES: Priority[] = ['LOW', 'MEDIUM', 'HIGH']

type Props = {
  onSubmit: (input: TaskInput) => Promise<boolean>
  submitLabel: string
  initial?: TaskInput
  onCancel?: () => void
}

function TaskForm({ onSubmit, submitLabel, initial, onCancel }: Props) {
  const [name, setName] = useState(initial ? initial.name : '')
  const [description, setDescription] = useState(initial ? initial.description : '')
  const [deadline, setDeadline] = useState(initial ? initial.deadline : '')
  const [priority, setPriority] = useState<Priority>(initial ? initial.priority : 'MEDIUM')
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setSubmitting(true)
    const saved = await onSubmit({ name, description, deadline, priority })
    setSubmitting(false)

    // Only the create form empties itself. When editing, App closes the form
    // on success, and on failure the values stay so they can be corrected.
    if (saved && !initial) {
      setName('')
      setDescription('')
      setDeadline('')
      setPriority('MEDIUM')
    }
  }

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <label>
        Name
        <input value={name} onChange={(e) => setName(e.target.value)} />
      </label>

      <label>
        Description
        <textarea
          rows={2}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
      </label>

      <div className="task-form-row">
        <label>
          Deadline
          <input
            type="date"
            value={deadline}
            onChange={(e) => setDeadline(e.target.value)}
          />
        </label>

        <label>
          Priority
          <select
            value={priority}
            onChange={(e) => setPriority(e.target.value as Priority)}
          >
            {PRIORITIES.map((p) => (
              <option key={p} value={p}>{p}</option>
            ))}
          </select>
        </label>
      </div>

      <div className="task-form-actions">
        <button type="submit" disabled={submitting}>
          {submitting ? 'Saving...' : submitLabel}
        </button>

        {onCancel && (
          <button type="button" onClick={onCancel} disabled={submitting}>
            Cancel
          </button>
        )}
      </div>
    </form>
  )
}

export default TaskForm
