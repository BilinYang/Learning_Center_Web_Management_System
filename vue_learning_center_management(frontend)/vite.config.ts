import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: { // Configure reverse proxy
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Target address
        secure: false, // Disable HTTPS verification
        changeOrigin: true, // Modify request origin
        rewrite: (path) => path.replace(/^\/api/, ''), // Rewrite path 
      }
    }
  }
})


