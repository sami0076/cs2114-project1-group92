import { useCallback, useEffect, useState } from 'react'
import './App.css'
import { completeTask, createTask, deleteTask, getTasks, updateTask } from './api'
import TaskForm from './TaskForm'
import TaskItem from './TaskItem'
import type { Task, TaskInput } from './types'

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [busy, setBusy] = useState(false)
  const [editingIndex, setEditingIndex] = useState<number | null>(null)

  // Tasks are identified by their position in the list, and those positions
  // shift on the backend, so always reload rather than editing tasks locally.
  const refresh = useCallback(() => {
    return getTasks()
      .then(setTasks)
      .catch((e: Error) => setError(e.message))
  }, [])

  useEffect(() => {
    refresh().finally(() => setLoading(false))
  }, [refresh])

  // Deleting a task shifts every later task down one position, so no second
  // request may go out on the old positions until the list has reloaded. For
  // the same reason any open edit is closed: its index may no longer be right.
  async function run(action: () => Promise<unknown>) {
    setError('')
    setBusy(true)
    setEditingIndex(null)

    try {
      await action()
      await refresh()
    }
    catch (e) {
      setError((e as Error).message)
    }
    finally {
      setBusy(false)
    }
  }

  async function handleCreate(input: TaskInput): Promise<boolean> {
    setError('')

    try {
      await createTask(input)
      await refresh()
      return true
    }
    catch (e) {
      setError((e as Error).message)
      return false
    }
  }

  async function handleUpdate(input: TaskInput): Promise<boolean> {
    if (editingIndex === null) {
      return false
    }
    setError('')

    try {
      await updateTask(editingIndex, input)
      await refresh()
      setEditingIndex(null)
      return true
    }
    catch (e) {
      setError((e as Error).message)
      return false
    }
  }

  const editing = editingIndex === null ? undefined : tasks[editingIndex]

  return (
    <main className="app">
      <h1>TaskEasy</h1>

      {editing ? (
        <TaskForm
          key={'edit-' + editingIndex}
          initial={{
            name: editing.name,
            description: editing.description,
            deadline: editing.deadline,
            priority: editing.priority
          }}
          submitLabel="Save changes"
          onSubmit={handleUpdate}
          onCancel={() => {
            setEditingIndex(null)
            setError('')
          }}
        />
      ) : (
        <TaskForm key="new" submitLabel="Add task" onSubmit={handleCreate} />
      )}

      {error !== '' && <p className="error">{error}</p>}

      {loading && <p>Loading...</p>}

      {!loading && tasks.length > 0 && (
        <ul className="task-list">
          {tasks.map((task, index) => (
            <TaskItem
              key={task.name}
              task={task}
              index={index}
              busy={busy}
              onEdit={(i) => {
                setEditingIndex(i)
                setError('')
              }}
              onComplete={(i) => run(() => completeTask(i))}
              onDelete={(i) => run(() => deleteTask(i))}
            />
          ))}
        </ul>
      )}

      {!loading && tasks.length === 0 && error === '' && (
        <p className="empty">No tasks yet.</p>
      )}
    </main>
  )
}

export default App
