<script setup lang="ts">
  import router from '@/router';
  import { useLoginEmpStore } from '@/stores/loginEmp';
  import { ElMessage } from 'element-plus';
  import { ref } from 'vue';
  import { RouterView } from 'vue-router';

  const loginEmpStore = useLoginEmpStore();
  const loginName = ref<string|undefined>('')
  loginName.value = loginEmpStore.loginEmp.empName; 

  const logout = ()=>{
    // 1. remove login info 
    loginEmpStore.removeLoginEmp();
    // 2. go to login page using router 
    router.push('/login'); 
    ElMessage.success("You've successfully logged out.")
  }

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <span class="title"><b>Learning Center Web Management System</b></span>
        <span class="right_tools">
            <!-- <a href=""><el-icon><Edit /></el-icon> Change password&nbsp; &nbsp; | &nbsp; &nbsp; </a> -->
            <a href="" @click="logout"><el-icon><Switch /></el-icon> Log out ({{ loginName }})</a>
        </span>
      </el-header>

      <el-container>
        <el-aside width="240px">
          <el-scrollbar>
            <el-menu :default-openeds="['1', '3']" router>

              <el-menu-item index="/index"><el-icon><Promotion /></el-icon>Home</el-menu-item>
  
              <el-sub-menu index="course_stu">
                <template #title>
                  <el-icon><Menu /></el-icon>Learning Center
                </template>
                <el-menu-item-group>
                  <el-menu-item index="/course"><el-icon><HomeFilled /></el-icon>Course Manager</el-menu-item>
                  <el-menu-item index="/student"><el-icon><UserFilled /></el-icon>Student Manager</el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>

              <el-sub-menu index="system">
                <template #title>
                  <el-icon><Tools /></el-icon>System Management
                </template>
                <el-menu-item-group>
                  <el-menu-item index="/dept"><el-icon><HelpFilled /></el-icon>Department Manager</el-menu-item>
                  <el-menu-item index="/emp"><el-icon><Avatar /></el-icon>Employee Manager</el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>

              <el-sub-menu index="statistics">
                <template #title>
                  <el-icon><Histogram /></el-icon>Analytics
                </template>
                <el-menu-item-group>
                  <el-menu-item index="emp_stats"><el-icon><InfoFilled /></el-icon>Employee Statistics</el-menu-item>
                  <el-menu-item index="stu_stats"><el-icon><Share /></el-icon>Student Statistics</el-menu-item>
                  <el-menu-item index="log_stats"><el-icon><Document /></el-icon>Activity Logs</el-menu-item>
                </el-menu-item-group>
              </el-sub-menu>

            </el-menu>
          </el-scrollbar>
        </el-aside>

        <el-main>
          <RouterView></RouterView>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
    .header{
        background-image: linear-gradient(to right, #231837, #422f58, #64477a, #8a619e, #b27bc2);
    }

    .title{
        font-size: 30px;
        color: white; 
        font-family: 'Lucida Sans', 'Courier'; 
        line-height: 60px; 
    }

    .right_tools{
      float: right; 
      line-height: 60px; 
    }

    a{
        color: white; 
        text-decoration: none;
    }
</style>
