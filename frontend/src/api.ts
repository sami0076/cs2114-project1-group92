import type { Stats, Task, TaskInput } from './types'

const BASE = '/api'
const UNREACHABLE = 'Could not reach the server.'


async function readErrorMessage(response: Response): Promise<string> {
  const body = (await response.text()).trim()

  if (!body) {
    return 'Request failed (' + response.status + ').'
  }

  try {
    const parsed = JSON.parse(body)

    if (typeof parsed === 'string' && parsed) {
      return parsed
    }
    if (parsed && typeof parsed.message === 'string' && parsed.message) {
      return parsed.message
    }
    if (parsed && typeof parsed.error === 'string' && parsed.error) {
      return parsed.error
    }
    return 'Request failed (' + response.status + ').'
  }
  catch {
    return body
  }
}

/**
 * Sends one request and returns the parsed body.
 *
 * Rejects with a message suitable for showing the user in both failure cases
 * the specification lists: the backend being unreachable, and a 400 carrying a
 * validation message.
 */
async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response

  try {
    response = await fetch(BASE + path, init)
  }
  catch {
    throw new Error(UNREACHABLE)
  }


  if (response.status === 502 || response.status === 503 || response.status === 504) {
    throw new Error(UNREACHABLE)
  }

  if (!response.ok) {
    throw new Error(await readErrorMessage(response))
  }

  const body = await response.text()
  return (body ? JSON.parse(body) : undefined) as T
}

function sending(method: string, task: TaskInput): RequestInit {
  return {
    method: method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(task)
  }
}

export function getTasks(): Promise<Task[]> {
  return request<Task[]>('/tasks')
}

export function createTask(t: TaskInput): Promise<Task> {
  return request<Task>('/tasks', sending('POST', t))
}

export function updateTask(index: number, t: TaskInput): Promise<Task> {
  return request<Task>('/tasks/' + index, sending('PUT', t))
}

export function deleteTask(index: number): Promise<void> {
  return request<void>('/tasks/' + index, { method: 'DELETE' })
}

export function completeTask(index: number): Promise<Task> {
  return request<Task>('/tasks/' + index + '/complete', { method: 'PATCH' })
}


export function getStats(): Promise<Stats> {
  return request<Stats>('/stats')
}
