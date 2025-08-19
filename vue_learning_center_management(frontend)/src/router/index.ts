import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
        path: '/', 
        name: 'home', 
        redirect: '/index', 
        component: ()=> import ('../views/layout/index.vue'), 
        children: [
            {
              path: '/index', 
              name: 'index', 
              component: ()=> import ('../views/index/index.vue')
            }, 
            {
              path: '/dept', 
              name: 'dept', 
              component: ()=> import ('../views/dept/index.vue')
            }, 
            {
              path: '/emp', 
              name: 'emp', 
              component: ()=> import ('../views/emp/index.vue')
            }, 
            {
              path: '/course', 
              name: 'course', 
              component: ()=> import ('../views/course/index.vue')
            }, 
            {
              path: '/student', 
              name: 'student', 
              component: ()=> import ('../views/student/index.vue')
            }
          ]
      }, 
      {
        path: '/login', 
        name: 'login', 
        component: ()=> import ('../views/login/index.vue')
      }
    ]
})
 
export default router
