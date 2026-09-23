import { useCallback, useEffect, useState } from 'react'
import './App.css'
import { createTask, getTasks } from './api'
import TaskForm from './TaskForm'
import TaskItem from './TaskItem'
import type { Task, TaskInput } from './types'

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

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

  return (
    <main className="app">
      <h1>TaskEasy</h1>

      <TaskForm onSubmit={handleCreate} />

      {error !== '' && <p className="error">{error}</p>}

      {loading && <p>Loading...</p>}

      {!loading && tasks.length > 0 && (
        <ul className="task-list">
          {tasks.map((task) => (
            <TaskItem key={task.name} task={task} />
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
