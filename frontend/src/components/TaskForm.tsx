import { useState } from 'react'
import type { FormEvent } from 'react'
import Button from 'react-bootstrap/Button'
import Card from 'react-bootstrap/Card'
import Col from 'react-bootstrap/Col'
import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import type { Priority, TaskInput } from '../types'

const PRIORITIES: Priority[] = ['LOW', 'MEDIUM', 'HIGH']

type Props = {
  onSubmit: (input: TaskInput) => Promise<boolean>
  title: string
  submitLabel: string
  initial?: TaskInput
  onCancel?: () => void
}

function TaskForm({ onSubmit, title, submitLabel, initial, onCancel }: Props) {
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
    <Card>
      <Card.Header>{title}</Card.Header>

      <Card.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="taskName">
            <Form.Label>Name</Form.Label>
            <Form.Control
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <Form.Text className="text-muted">
              {name.length}/50 characters
            </Form.Text>
          </Form.Group>

          <Form.Group className="mb-3" controlId="taskDescription">
            <Form.Label>Description</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
            <Form.Text className="text-muted">
              {description.length}/200 characters
            </Form.Text>
          </Form.Group>

          <Row className="mb-3">
            <Col>
              <Form.Group controlId="taskDeadline">
                <Form.Label>Deadline</Form.Label>
                <Form.Control
                  type="date"
                  value={deadline}
                  onChange={(e) => setDeadline(e.target.value)}
                />
              </Form.Group>
            </Col>

            <Col>
              <Form.Group controlId="taskPriority">
                <Form.Label>Priority</Form.Label>
                <Form.Select
                  value={priority}
                  onChange={(e) => setPriority(e.target.value as Priority)}
                >
                  {PRIORITIES.map((p) => (
                    <option key={p} value={p}>{p}</option>
                  ))}
                </Form.Select>
              </Form.Group>
            </Col>
          </Row>

          <div className="d-flex gap-2">
            <Button type="submit" variant="primary" disabled={submitting}>
              {submitting ? 'Saving...' : submitLabel}
            </Button>

            {onCancel && (
              <Button
                type="button"
                variant="secondary"
                onClick={onCancel}
                disabled={submitting}
              >
                Cancel
              </Button>
            )}
          </div>
        </Form>
      </Card.Body>
    </Card>
  )
}

export default TaskForm
