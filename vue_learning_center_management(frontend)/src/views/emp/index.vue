<script setup lang="ts">
  import type { SearchEmpModel, EmpModelArray, PageModel, PaginationParam, EmpModel, DeptModel, DeptModelArray, EmpExprModel } from '@/api/model/model';
  import {ref, watch, onMounted} from 'vue'
  import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi } from '@/api/emp';
  import { queryAllApi as queryAllDeptsApi } from '@/api/dept';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import type { FormInstance, FormRules, UploadProps } from 'element-plus'; 


  onMounted(()=>{
    queryPage(); 
    queryAllDepts(); 
  })

  // Encapsulate data in employee search box 
  const searchEmp = ref<SearchEmpModel>({
    name: '', 
    gender: '', 
    begin: '', 
    end: '', 
    date: [], 
  });

  // watch functin used to detect changes in date and assign the new dates to the variables searchEmp.begin and searchEmp.end 
  watch(()=> searchEmp.value.date, (newVal, oldVal)=>{
     if (newVal){
      // console.log("newVal", newVal)
      searchEmp.value.begin = newVal[0]; 
      searchEmp.value.end = newVal[1]; 
     }
     else{
      searchEmp.value.begin = ''; 
      searchEmp.value.end = ''; 
     }
  }, {deep: true})


  // used to show data in employee table 
  const empList = ref<EmpModelArray>([])
  const pagination = ref<PaginationParam>({
    currentPage: 1,
    pageSize: 5,
    total: 0
  })

  // runs when the number of rows of data displayed per page is changed by the user
  const handleSizeChange = (pageSize: number) => {
    console.log(`${pageSize} items per page`); 
    pagination.value.pageSize = pageSize; 
    queryPage(); 
  }
  // runs the current page the user is on changes
  const handleCurrentChange = (page: number) => {
    console.log(`current page: ${page}`);
    pagination.value.currentPage = page; 
    queryPage();  
  }

  // search all data and display results using pagination 
  const queryPage = async ()=>{
    const result = await queryPageApi(searchEmp.value.begin, searchEmp.value.end, searchEmp.value.gender, 
    searchEmp.value.name, pagination.value.currentPage, pagination.value.pageSize); 

    if (result.code){
      console.log("result", result); 
      empList.value = result.data.rows;  
      pagination.value.total = result.data.total; 
      console.log(empList); 
    }
  }

   // when the 'clear' button in the search bar is pressed 
  const clear = ()=>{
    searchEmp.value = {
    name: '', 
    gender: '', 
    begin: '', 
    end: '', 
    date: [], }; 
    queryPage();  
  }

  // const multipleSelection = ref<EmpModel[]>([])
  // runs when multiple rows are selected
  let selectedIds = ref<(number|undefined)[]>([]); 
  const handleSelectionChange = (val: EmpModel[]) => {
    // multipleSelection.value = val; 
    selectedIds.value = val.map(element=>{
      return element.id; 
    })
  }


  // ------------------------------------- Add employee --------------------------------------
  const dialogFormVisible = ref<boolean>(false); 
  const formTitle = ref<string>(''); 
  // encapsulate the data in the dialog form for adding/updating employees
  const emp = ref<EmpModel>({
    username: '',
    password: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    image: '',
    entryDate: '',
    deptId: '',
    exprList: []
  })

  const labelWidth = ref<number>(80); 
  const gutterWidth = ref<number>(15); 

  const genders = ref([{"gender": "Male", "value": "1"}, {"gender": "Female", "value": "2"}])
  const jobPositions = ref([{ name: 'Course Teacher', value: 1 },{ name: 'Lecturer', value: 2 },{ name: 'Student Affairs Supervisor', value: 3 },
                { name: 'Teaching Research Supervisor', value: 4 },{ name: 'Counselor', value: 5 },{ name: 'Other', value: 6 }])

  // get all departments (which we need because we will show a list of departments to the user when they add/update an employee)
  const deptList = ref<DeptModelArray>([]); 
  const queryAllDepts = (async ()=>{
    const result = await queryAllDeptsApi(); 
    deptList.value = result.data;
  })


  // ------------------------------------- Upload profile picture --------------------------------------
  // runs after file is successfully uploaded
  const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
  ) => {
    emp.value.image = response.data; 
    
  }

    // runs before uploading file 
  const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
    if (rawFile.type !== 'image/jpeg') {
      ElMessage.error('Profile picture must be JPG format!')
      return false // means that the file will not be uploadded 
    } else if (rawFile.size / 1024 / 1024 > 2) {
      ElMessage.error('Profile picture size can not exceed 2MB!')
      return false
    }
    return true
  }


  // when the buttons is pressed, this function runs to add a new work experience to exprList
  const addWorkExpr = (()=>{
    // add a new work experience to exprList
    emp.value.exprList.push({
        exprDate: [],
        begin: '',
        end: '',
        company: '',
        jobPosition: ''
    });
  })

    // ------------------------------------- Watch  --------------------------------------

  // watch emp object, if it changes, iterate over emp.exprList to assign values from emp.exprDate to emp.begin and emp.end
  watch(emp, (newVal, oldVal)=>{
    if (emp.value.exprList){
      emp.value.exprList.forEach((expr)=>{
        if (expr.exprDate && expr.exprDate.length > 0){ // if the date already has a non-null value assigned to it 
          expr.begin = expr.exprDate[0]; 
          expr.end = expr.exprDate[1];  
        }
      })
    }
  }, {deep: true})

  // runs when the button to delete a work experience is pressed 
  const deleteWorkExpr = ((expr: EmpExprModel)=>{
    // get the index of the work experience that we want to delete
    const start = emp.value.exprList.indexOf(expr);
    console.log("start: ", start)
    if (start != -1){
      // delete the work experience 
      emp.value.exprList.splice(start, 1); 
    }
  })

  // add employee
  const add = ()=>{
    dialogFormVisible.value = true; 
    formTitle.value = "Add Employee"; 
    emp.value = {
        username: '',
        password: '',
        name: '',
        gender: '',
        phone: '',
        job: '',
        salary: '',
        image: '',
        entryDate: '',
        deptId: '',
        exprList: []
    }
    // reset form 
    resetForm(empFormRef.value); 
  }

    // ------------------------------------- Save dialog box --------------------------------------

   // saves employee data (runs after the save button is clicked) [ used for both adding and editing employees ]
   const save = async (formEl: FormInstance | undefined)=>{
    // see if we can successfully validate the form first 
    if (!formEl) return;
    await formEl.validate(async (valid, fields) =>{ 
      if (valid){ // form validation successful 
        let result = null; 
        // sees whether the current 'save' is from adding or editing an employee 
        // (because when editing an employee, they will already have their id)
        if (emp.value.id){ // this is from editing an employee 
          result = await updateApi(emp.value); 
        }
        else{
          result = await addApi(emp.value); 
        }
        if (result.code){ // success
          ElMessage.success('Success'); 
          dialogFormVisible.value = false; 
          queryPage(); 
        }
        else { // failure
          ElMessage.error(result.msg); 
        }
      }
    })
   }



   const empFormRef = ref<FormInstance>(); 

   // rules to validate emp dialog box (used to add/update employees)
   const rules = ref<FormRules<EmpModel>>({
    username: [
      { required: true, message: 'Please enter username', trigger: 'blur' },
      { min: 1, max: 50, message: 'Username length should be 1 to 50', trigger: 'blur' },
    ],
    name: [
      { required: true, message: 'Please enter name', trigger: 'blur' },
      { min: 1, max: 50, message: 'Name length should be 1 to 50', trigger: 'blur' },
    ],
    gender: [{ required: true, message: 'Please select gender', trigger: 'blur' }], 
    phone: [
        { required: true, message: 'Please enter phone number', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/g, message: 'Please enter a valid phone number', trigger: 'blur' }
    ],
    salary: [
        { pattern: /^[1-9]\d*$/g, message: 'Please enter a valid salary number', trigger: 'blur' }
    ]
    
  })

  // reset form 
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

   
    // ------------------------------------- Update employee --------------------------------------

    const updateEmp = async (id: number)=>{
      // shows dialog box 
      dialogFormVisible.value = true; 
      // change form title 
      formTitle.value = "Edit Employee"; 
      // clear all validation warnings and fields 
      resetForm(empFormRef.value); 
      // call API to send asynchronous request to get data of the employee that the user wishes to edit 
      const result = await queryInfoApi(id); 
      if (result.code){
        // fill up the form with the data of the employee that the user wishes to edit 
        emp.value = result.data; 

        // process 'begin' and 'end' attributes in EmpExpr objects in emp.exprList and turn them into exprDate objects 
        const exprList = emp.value.exprList; 
        exprList.forEach((expr)=>{
          expr.exprDate = [expr.begin, expr.end]; 
        })
      }
      
    }


    // ------------------------------------- Delete employee --------------------------------------
    const deleteEmp = async (id: number) => {
      // Message box to ask the user if they're sure they want to delete the employee 
      ElMessageBox.confirm(
        'Are you sure you want to delete this employee?', 'Delete Employee',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
      ).then(async () => {
          // Send asynchronous request to delete employee 
          const result = await deleteApi(id.toString()); // note that `${id}` converts id (which is a number) to a string  
          console.log("result: ", result)
          if (result.code){
            ElMessage.success('Deleted successfully');
            queryPage(); 
          }
          else{
            ElMessage.error(result.msg); 
          }
        })
        .catch(() => {
      })
    }


    // Batch delete employees  
    const deleteBatch = ()=>{
      ElMessageBox.confirm(
        'Are you sure you want to delete the selected employee(s)?', 'Batch Delete Employees',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
      ).then(async () => {
          // Send asynchronous request to delete employee 
          const result = await deleteApi(selectedIds.value.toString()); // note that `${id}` converts id (which is a number) to a string  
          console.log("result: ", result)
          if (result.code){
            ElMessage.success('Deleted successfully');
            queryPage(); 
          }
          else{
            ElMessage.error(result.msg); 
          }
        }).catch(() => {
      })
    }


</script>

<template>
  <h1>Employee Management</h1><br></br>

  <!-- Search Bar -->
   <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
    <el-form-item label="Name">
      <el-input v-model="searchEmp.name" placeholder="Enter a name" clearable />
    </el-form-item>
    <el-form-item label="Gender">
      <el-select v-model="searchEmp.gender" placeholder="Select a gender" style="width: 160px" clearable>
        <el-option label="Male" value="1" />
        <el-option label="Female" value="2" />
      </el-select>
    </el-form-item>
    <el-form-item label="Entry Date">
      <el-date-picker
        v-model="searchEmp.date"
        type="daterange"
        range-separator="To"
        start-placeholder="Entry Date From"
        end-placeholder="Entry Date To"
        value-format="YYYY-MM-DD"
        format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="queryPage">Search</el-button>
      <el-button @click="clear">Clear</el-button>
    </el-form-item>
    <br></br>

    <!-- Add or delete employee(s) -->
    <el-button type="success" @click="add">Add Employee</el-button>
    <el-button type="danger" @click="deleteBatch">Batch Delete Employees</el-button>
  </el-form>


  <!-- Data table  -->
   <br></br>
  <el-table :data="empList" border style="width: 100%" fit @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column prop="name" label="Name" align="center" width="100px" />
    <el-table-column label="Gender" align="center" width="75px">
      <template #default="scope">
        {{ scope.row.gender==1 ? 'Male' : 'Female'}}
      </template>
    </el-table-column>>
     <el-table-column label="Image" align="center">
      <template #default="scope">
        <img :src="scope.row.image" width="50px"></img>
      </template>
     </el-table-column>/>
    <el-table-column prop="deptName" label="Affiliated Department" align="center" width="130px"/>
    <el-table-column label="Job Position" align="center" width="130px">
      <template #default="scope">
        <span v-if="scope.row.jobPosition==1">Course Teacher</span>
        <span v-else-if="scope.row.job==2">Lecturer</span>
        <span v-else-if="scope.row.job==3">Student Affairs Supervisor</span>
        <span v-else-if="scope.row.job==4">Teaching Research Supervisor</span>
        <span v-else-if="scope.row.job==5">Counselor</span>
        <span v-else>Other</span>
      </template>
    </el-table-column>/>
    <el-table-column prop="entryDate" label="Entry Date" align="center" width="110px"/>
    <el-table-column prop="updateTime" label="Last Updated" align="center" width="140px"/>
    <el-table-column label="Actions" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="updateEmp(scope.row.id)">Edit</el-button>
      </template>
    </el-table-column>
  </el-table><br></br>


  <!-- Pagination -->
  <el-pagination
      v-model:current-page="pagination.currentPage"
      v-model:page-size="pagination.pageSize"
      :page-sizes="[5, 10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    /><br></br><br></br>

  <!-- Dialog Box to Add/Update Employee  -->
   <el-dialog v-model="dialogFormVisible" :title="formTitle">
    <el-form :model="emp" :rules="rules" ref="empFormRef">
      <!-- First Row -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Username" :label-width="labelWidth" prop="username">
            <el-input v-model="emp.username" placeholder="Enter a username of length 1 to 50"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Name" :label-width="labelWidth" prop="name">
            <el-input v-model="emp.name" placeholder="Enter full name of length 1 to 50"/>
          </el-form-item>
        </el-col>
      </el-row>
      
      <!-- Second Row -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Gender" :label-width="labelWidth" prop="gender">
            <el-select v-model="emp.gender" placeholder="Select gender" style="width: 100%;">
              <!-- <el-option label="Male" value="1" />
              <el-option label="Female" value="2" /> -->
              <el-option v-for="gender in genders" :key="gender.value" 
              :label="gender.gender" :value="gender.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" prop="phone">
          <el-form-item label="Phone" :label-width="labelWidth" prop="phone">
            <el-input v-model="emp.phone" placeholder="Enter a valid phone number"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Third Row -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Salary" :label-width="labelWidth" prop="salary">
            <el-input v-model="emp.salary" placeholder="Enter monthly salary"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Entry Date" :label-width="labelWidth">
            <el-date-picker v-model="emp.entryDate" type="date" placeholder="Select entry date" value-format="YYYY-MM-DD" style="width: 100%;"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Fourth Row -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12"> 
          <el-form-item label="Department" :label-width="labelWidth">
            <el-select v-model="emp.deptId" placeholder="Select department" style="width: 100%;">
              <el-option v-for="(dept, index) in deptList" :key="index" :label="dept.name" :value="dept.id"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Position" :label-width="labelWidth">
            <el-select v-model="emp.job" placeholder="Select job position" style="width: 100%;">
              <el-option v-for="jobPos in jobPositions" :key="jobPos.value" :label="jobPos.name" :value="jobPos.value"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Fifth Row -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Picture" :label-width="labelWidth" style="display: flex; align-items: center;">
            <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess" 
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="emp.image" :src="emp.image" class="avatar" style="max-width: 120px; max-height: 120px; object-fit: cover;"/>
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Sixth Row -->
      <el-row :gutter=gutterWidth style="display: flex; align-items: center;">
        <el-col :span="24">
          <el-form-item label="Work Experience" :label-width=125 >
            <el-button type="success" size="small" @click="addWorkExpr">+ Add Work Experience</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Seventh Row -->
      <el-row :gutter=gutterWidth v-for="(expr, index) in emp.exprList" :key="index">
        <el-col :span="9">
          <el-form-item label="Period" size="small" :label-width="labelWidth">
            <el-date-picker v-model="expr.exprDate" type="daterange" range-separator="to" start-placeholder="Start" end-placeholder="End" value-format="YYYY-MM-DD"/>
          </el-form-item>
        </el-col>
        
        <el-col :span="6">
          <el-form-item label="Company" size="small">
            <el-input placeholder="Company name" v-model="expr.company"/>
          </el-form-item>
        </el-col>

        <el-col :span="6">
          <el-form-item label="Position" size="small">
            <el-input placeholder="Job title" v-model="expr.jobPosition"/>
          </el-form-item>
        </el-col>

        <el-col :span="3">
          <el-form-item size="small">
            <el-button type="danger" @click="deleteWorkExpr(expr)">Delete</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible=false">Cancel</el-button>
        <el-button type="primary" @click="save(empFormRef)">Save</el-button>
      </span>
    </template>
</el-dialog>


</template>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }

  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 78px;
    height: 78px;
    text-align: center;
    border: 1px dashed; 
  }
</style>