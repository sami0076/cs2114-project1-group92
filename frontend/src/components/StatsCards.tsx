import Card from 'react-bootstrap/Card'
import Col from 'react-bootstrap/Col'
import ProgressBar from 'react-bootstrap/ProgressBar'
import Row from 'react-bootstrap/Row'
import type { Stats } from '../types'

type Props = {
  stats: Stats
}

function StatsCards({ stats }: Props) {
  // completionRate arrives as 0.0 to 1.0. The backend returns 0.0 for an empty
  // list rather than dividing by zero, so there is nothing to guard here.
  const percent = Math.round(stats.completionRate * 100)

  return (
    <Row className="mb-3 g-3">
      <Col md={4}>
        <Card className="h-100 text-center">
          <Card.Body>
            <div className="h2 mb-1">{stats.completed}</div>
            <div className="text-muted">Completed</div>
          </Card.Body>
        </Card>
      </Col>

      <Col md={4}>
        <Card className="h-100 text-center">
          <Card.Body>
            <div className="h2 mb-1">{stats.overdue}</div>
            <div className="text-muted">Overdue</div>
          </Card.Body>
        </Card>
      </Col>

      <Col md={4}>
        <Card className="h-100 text-center">
          <Card.Body>
            <div className="h2 mb-1">{percent}%</div>
            <div className="text-muted mb-2">
              Completion rate ({stats.completionRate.toFixed(2)})
            </div>
            <ProgressBar variant="success" now={percent} />
          </Card.Body>
        </Card>
      </Col>
    </Row>
  )
}

export default StatsCards
