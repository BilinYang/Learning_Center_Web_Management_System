<script setup lang="ts">
  import { ref } from 'vue'
  import type { LoginEmp } from '@/api/model/model'
  import { loginApi } from '@/api/login'
  import { ElMessage } from 'element-plus'
  import router from '@/router'
  import { useLoginEmpStore } from '@/stores/loginEmp'

  const store = useLoginEmpStore()
  let loginForm = ref<LoginEmp>({username:'', password:''})

  // login 
  const login = async ()=>{
    // send async request 
    const result = await loginApi(loginForm.value); 
    if (result.code){
      ElMessage.success("Login successful!"); // login sucessful
      // need to change address to front page by using router 
      router.push('/index'); 

      store.setLoginEmp(result.data); // store login info (including token) in pinia
    }
    else{
      ElMessage.error(result.msg); 
    }
  }

  // clear all elements in the login form
  const clear = ()=>{
    loginForm.value = {username:'', password:''};  
  }


</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">Learning Center Web Management System</p>
        <el-form-item label="Username" prop="username">
          <el-input v-model="loginForm.username" placeholder="Please enter your username"></el-input>
        </el-form-item>

        <el-form-item label="Password" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="Please enter your password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="button" type="primary" @click="login">Login</el-button>
          <el-button class="button" type="info" @click="clear">Clear All</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  padding: 10%;
  height: 410px;
  background-image: url('@/assets/login_background.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.login-form {
  max-width: 400px;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  background-color: white;
}

.title {
  font-size: 30px;
  font-family: '楷体';
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}
</style>