import Badge from 'react-bootstrap/Badge'
import Button from 'react-bootstrap/Button'
import Card from 'react-bootstrap/Card'
import Table from 'react-bootstrap/Table'
import { isOverdue } from '../overdue'
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
  function askThenDelete(index: number) {
    if (window.confirm('Delete this task?')) {
      onDelete(index)
    }
  }

  return (
    <Card>
      <Card.Header>Tasks</Card.Header>

      <Card.Body className="p-0">
        {tasks.length === 0 ? (
          <p className="text-muted m-3">No tasks yet. Add one with the form.</p>
        ) : (
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
              {tasks.map((task, index) => (
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
