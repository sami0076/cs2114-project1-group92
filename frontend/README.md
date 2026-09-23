# TaskEasy Frontend

Vite + React + TypeScript, with React Bootstrap for the UI.

## Running it

The backend must be running on **http://localhost:8080** first. The dev server
proxies `/api` to it, so there is no CORS setup on either side.

```
cd frontend
npm install     # first time only, or after package.json changes
npm run dev     # http://localhost:5173 - leave this running
```

`npm run dev` does not exit; stop it with Ctrl+C.

Without the backend the page still loads, but every request fails and the red
alert reads "Could not reach the server."

