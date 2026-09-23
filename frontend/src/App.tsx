import { useEffect, useState } from 'react'
import './App.css'
import { getTasks } from './api'
import TaskItem from './TaskItem'
import type { Task } from './types'

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getTasks()
      .then(setTasks)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false))
  }, [])

  const showTasks = !loading && error === ''

  return (
    <main className="app">
      <h1>TaskEasy</h1>

      {error !== '' && <p className="error">{error}</p>}

      {loading && <p>Loading...</p>}

      {showTasks && tasks.length === 0 && <p className="empty">No tasks yet.</p>}

      {showTasks && tasks.length > 0 && (
        <ul className="task-list">
          {tasks.map((task) => (
            <TaskItem key={task.name} task={task} />
          ))}
        </ul>
      )}
    </main>
  )
}

export default App
