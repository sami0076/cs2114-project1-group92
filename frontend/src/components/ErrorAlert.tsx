import Alert from 'react-bootstrap/Alert'

type Props = {
  message: string
  onDismiss: () => void
}

function ErrorAlert({ message, onDismiss }: Props) {
  if (message === '') {
    return null
  }

  return (
    <Alert variant="danger" dismissible onClose={onDismiss}>
      {message}
    </Alert>
  )
}

export default ErrorAlert
