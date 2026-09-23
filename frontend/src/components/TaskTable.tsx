import { useState } from 'react'
import Badge from 'react-bootstrap/Badge'
import Button from 'react-bootstrap/Button'
import Card from 'react-bootstrap/Card'
import Form from 'react-bootstrap/Form'
import Table from 'react-bootstrap/Table'
import { isOverdue } from '../overdue'
import { matchTasks } from '../search'
import type { Task } from '../types'

type Props = {
  tasks: Task[]
  busy: boolean
  onEdit: (index: number) => void
  onComplete: (index: number) => void
  onDelete: (index: number) => void
}

function priorityBadge(task: Task) {
  if (task.priority === 'HIGH') {
    return <Badge bg="danger">HIGH</Badge>
  }
  if (task.priority === 'MEDIUM') {
    return <Badge bg="warning" text="dark">MEDIUM</Badge>
  }
  return <Badge bg="secondary">LOW</Badge>
}

function statusBadge(task: Task) {
  if (task.completed) {
    return <Badge bg="success">Done</Badge>
  }
  if (isOverdue(task)) {
    return <Badge bg="danger">Overdue</Badge>
  }
  return <Badge bg="secondary">Open</Badge>
}

function TaskTable({ tasks, busy, onEdit, onComplete, onDelete }: Props) {
  const [search, setSearch] = useState('')
  const rows = matchTasks(tasks, search)

  function askThenDelete(index: number) {
    if (window.confirm('Delete this task?')) {
      onDelete(index)
    }
  }

  return (
    <Card>
      <Card.Header className="d-flex justify-content-between align-items-center">
        <span>Tasks</span>
        {tasks.length > 0 && (
          <small className="text-muted">
            Showing {rows.length} of {tasks.length}
          </small>
        )}
      </Card.Header>

      <Card.Body className="p-0">
        {tasks.length > 0 && (
          <div className="p-3 border-bottom">
            <Form.Control
              type="search"
              placeholder="Search by name or description"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
        )}

        {tasks.length === 0 && (
          <p className="text-muted m-3">No tasks yet. Add one with the form.</p>
        )}

        {tasks.length > 0 && rows.length === 0 && (
          <p className="text-muted m-3">No tasks match your search.</p>
        )}

        {rows.length > 0 && (
          <Table striped hover responsive className="align-middle mb-0">
            <thead>
              <tr>
                <th>#</th>
                <th>Task</th>
                <th>Deadline</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {rows.map(({ task, index }) => (
                <tr key={task.name}>
                  <td>{index}</td>

                  <td>
                    <div className="fw-bold">{task.name}</div>
                    <small className="text-muted">{task.description}</small>
                  </td>

                  <td>{task.deadline}</td>
                  <td>{priorityBadge(task)}</td>
                  <td>{statusBadge(task)}</td>

                  <td>
                    <div className="d-flex gap-2">
                      <Button
                        size="sm"
                        variant="outline-success"
                        disabled={busy || task.completed}
                        onClick={() => onComplete(index)}
                      >
                        Complete
                      </Button>

                      <Button
                        size="sm"
                        variant="outline-primary"
                        disabled={busy}
                        onClick={() => onEdit(index)}
                      >
                        Edit
                      </Button>

                      <Button
                        size="sm"
                        variant="outline-danger"
                        disabled={busy}
                        onClick={() => askThenDelete(index)}
                      >
                        Delete
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </Card.Body>
    </Card>
  )
}

export default TaskTable
