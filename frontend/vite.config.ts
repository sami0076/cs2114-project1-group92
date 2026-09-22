import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // The Java backend (TaskEasy) runs on port 8080. The dev server forwards
    // /api/... to it with the /api prefix stripped, so api.ts can use relative
    // paths like /api/tasks and we never run into CORS.
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
