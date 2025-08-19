import axios from 'axios'
import { useLoginEmpStore } from '@/stores/loginEmp'
import router from '@/router'
import { ElMessage } from 'element-plus'

const store = useLoginEmpStore()

const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

request.interceptors.response.use(
  (response) => { //success
    return response.data
  },
  (error) => { //failure
    if (error.response.status == 401){
      // change to login page using router 
      router.push('/login')
      ElMessage.error('You are currently not logged in. Please log in first.')
    }
    return Promise.reject(error)
  }
)

request.interceptors.request.use(function (config) {
    const loginEmp = store.getLoginEmp(); 
    // put token inside the request header
    console.log("loginEmp", loginEmp)
    if (loginEmp!=null && loginEmp.token){
      config.headers['token'] =  loginEmp.token
    }
    return config;
  }, function (error) {
    return Promise.reject(error);
  }); 

export default request