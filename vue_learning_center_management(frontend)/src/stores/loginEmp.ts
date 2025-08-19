import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { type LoginInfo } from '@/api/model/model'

export const useLoginEmpStore = defineStore('loginEmpInfo', () => {
  // define variable to store all user login information
  const loginEmp = ref<LoginInfo>({}); 

  const setLoginEmp = (logininfo: LoginInfo)=>{
    loginEmp.value = logininfo; 
  }

  const getLoginEmp = ()=>{
    return loginEmp.value; 
  }

  const removeLoginEmp = ()=>{
    loginEmp.value = {};
  }
  return { loginEmp, setLoginEmp, getLoginEmp, removeLoginEmp}
}, {persist: true})
