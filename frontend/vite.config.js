import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Configuração do Vite.
// O proxy encaminha as chamadas de /tarefas para o backend Spring Boot
// rodando em localhost:8080, evitando problemas de CORS em desenvolvimento.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/tarefas': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
