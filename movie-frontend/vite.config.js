import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000,
        strictPort: true,
        host: true,
        // Dev sırasında tarayıcıdan backend'e direkt çağrı yerine /api isteklerini proxy'leyip CORS sorunlarını azaltır.
        proxy: {
            '/api': {
                target: process.env.VITE_API_TARGET || 'http://localhost:8082',
                changeOrigin: true,
            },
        },
    }
})