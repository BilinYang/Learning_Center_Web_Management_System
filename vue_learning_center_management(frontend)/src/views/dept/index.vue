<script setup lang="ts">
  import {ref, onMounted} from 'vue'
  import type {DeptModel, DeptModelArray} from '@/api/model/model'
  import {queryAllApi} from '@/api/dept'
  import {addApi, getDeptApi, updateApi, deleteApi} from '@/api/dept'
  import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
  import { useLoginEmpStore } from '@/stores/loginEmp'

  const deptList = ref<DeptModelArray>([])
  const queryAll = async ()=>{
    // const result = await axios.get('https://m1.apifoxmock.com/m1/6873501-6588519-default/depts'); 
    const result = await queryAllApi(); 
    console.log(result); 
    deptList.value = result.data; 
  }

  onMounted(()=>{
    queryAll(); 
  }) 


  // add department
  let dialogFormVisible = ref<boolean>(false)
  const deptForm = ref<DeptModel>({name:''})
  let formTitle = ref<string>('')
  // runs when the button to add a department is clicked
  const add = ()=>{
    resetForm(deptFormRef.value); 
    formTitle.value = 'Add Department'; 
    dialogFormVisible.value=true; 
    deptForm.value = {name:''}; 
  }

  // saves the data and submits the request (after 'confirm' is clicked on the form to add a department) 
  const save = async (formEl: FormInstance | undefined)=>{
    // validate data before submitting 
    if (!formEl) return
    await formEl.validate(async (valid) => {
      if (valid) { // valid = true means that the form was successfully submitted
        let result = null; 
        if (deptForm.value.id){ // if there is a value for id, then we are editing an existing department
          result = await updateApi(deptForm.value); 
        }
        else{ // if there is not a value for id, then we are adding a new department 
          result = await addApi(deptForm.value);
        }

        if (result.code){
          dialogFormVisible.value = false; 
          queryAll(); 
          ElMessage.success('Success'); 
        }
        else{
          ElMessage.error(result.msg);
        }
      }
    })
  }

  // edit department information 
  const update = async (id: number)=>{
    resetForm(deptFormRef.value);  
    formTitle.value = 'Edit Department Information'; 
    dialogFormVisible.value = true; 
    deptForm.value = {name:''}; 

    const result = await getDeptApi(id); 
    if (result.code){
      deptForm.value = result.data; 
    }
  }

  // delete department by ID
  const deleteById = (id: number) =>{
    // Message box to ask the user if they're sure they want to delete the department 
    ElMessageBox.confirm(
      'Are you sure you want to delete this department?', 'Delete Department',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
    ).then(async () => {
        // Send asynchronous request to delete department data
        const result = await deleteApi(id); 
        if (result.code){
          ElMessage.success('Deleted successfully');
          queryAll(); 
        }
        else{
          ElMessage.error(result.msg); 
        }
      })
      .catch(() => {
    })
  }

  // define deptForm validation rules 
  const deptFormRef = ref<FormInstance>() // define FormInstance object 
  const rules = ref<FormRules<DeptModel>>({
    name: [
      // verify if boxes if empty (if not, display message)
      { required: true, message: 'Please input department name', trigger: 'blur' }, 
      // verify if input is of the correct length (if not, display message)
      { min: 1, max: 50, message: 'Length should be between 1 to 50', trigger: 'blur' },
    ]
  })

  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }
 
</script>

<template>
  <h1>Department Manager</h1><br></br>

  <el-button type="success" style="float:right" @click="add">Add Department</el-button><br><br>

  <!-- department table -->
  <el-table :data="deptList" border style="width: 100%">
    <el-table-column type="index" label="Index" width="140" />
    <el-table-column prop="name" label="Department Name" width="260" />
    <el-table-column prop="updateTime" label="Last Updated" width="260"/>
    <el-table-column prop="edit" label="Actions">
      <template #default="scope">
        <el-button size="small" type="primary" @click="update(scope.row.id)">Edit</el-button>
        <el-button size="small" type="danger" @click="deleteById(scope.row.id)">Delete</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- add department form through dialog box -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="deptForm" :rules="rules" ref="deptFormRef">
      <el-form-item label="Department name" prop="name">
        <el-input v-model="deptForm.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save(deptFormRef)">Confirm</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>
