import { useEffect, useState } from 'react'
import './App.css'
import { getTasks } from './api'
import type { Task } from './types'

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  // Load the tasks once, when the page opens.
  useEffect(() => {
    getTasks()
      .then(setTasks)
      .catch((e: Error) => setError(e.message))
      .finally(() => setLoading(false))
  }, [])

  return (
    <main className="app">
      <h1>TaskEasy</h1>

      {error !== '' && <p className="error">{error}</p>}

      {loading && <p>Loading...</p>}
      {!loading && error === '' && <p>{tasks.length} tasks loaded.</p>}
    </main>
  )
}

export default App
