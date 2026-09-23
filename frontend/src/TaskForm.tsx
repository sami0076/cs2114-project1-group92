import { useState } from 'react'
import type { FormEvent } from 'react'
import type { Priority, TaskInput } from './types'

const PRIORITIES: Priority[] = ['LOW', 'MEDIUM', 'HIGH']

type Props = {
  onSubmit: (input: TaskInput) => Promise<boolean>
}

function TaskForm({ onSubmit }: Props) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [deadline, setDeadline] = useState('')
  const [priority, setPriority] = useState<Priority>('MEDIUM')
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setSubmitting(true)
    const created = await onSubmit({ name, description, deadline, priority })
    setSubmitting(false)

    if (created) {
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

      <button type="submit" disabled={submitting}>
        {submitting ? 'Adding...' : 'Add task'}
      </button>
    </form>
  )
}

export default TaskForm
