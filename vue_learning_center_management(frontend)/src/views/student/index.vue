<script setup lang="ts">
  import {ref, onMounted} from 'vue'; 
  import type {CourseModelArray, PaginationParam, SearchStudentModel, StudentModel, StuModelArray} from '@/api/model/model'; 
  import { addApi, applyPenaltyApi, deleteApi, queryInfoApi, queryPageApi, updateApi } from '@/api/student';
  import { ElMessage, type FormRules, type FormInstance, ElMessageBox } from 'element-plus';
  import { queryAllApi } from '@/api/course';

  onMounted(()=>{
    queryPage(); 
    queryAllCourses(); 
  })


  // ----------------------------------------- Pagination ------------------------------------------
  const pagination = ref<PaginationParam>({
    currentPage: 1,
    pageSize: 5,
    total: 0
  })

  const handleSizeChange = (pageSize: number) => {
    console.log(`${pageSize} items per page`); 
    pagination.value.pageSize = pageSize; 
    queryPage(); 
  }
  const handleCurrentChange = (currentPage: number) => {
    console.log(`current page: ${currentPage}`); 
    pagination.value.currentPage = currentPage; 
    queryPage(); 
  }

  const studentForm = ref<SearchStudentModel>({
    studentName: '',
    degree: '',
    courseId: '',
  })

  // ----------------------------------------- Search bar ------------------------------------------
  const stuList = ref<StuModelArray>([]); 

  const degreeOptions = ref([
    {label: 'Middle School', value: 1}, 
    {label: 'High School', value: 2}, 
    {label: 'Junior College', value: 3}, 
    {label: "Bachelor's", value: 4}, 
    {label: "Master's", value: 5}, 
    {label: 'Doctorate', value: 6}, 
  ])

  const queryPage = async ()=>{
    const result = await queryPageApi(studentForm.value.studentName, studentForm.value.degree, 
      studentForm.value.courseId, pagination.value.currentPage, pagination.value.pageSize); 
    console.log(result); 
    
    if (result.code){
      stuList.value = result.data.rows;
      pagination.value.total = result.data.total; 
    }
  }

  const clear = ()=>{
    studentForm.value ={
      studentName: '',
      degree: '',
      courseId: '', 
    }
    queryPage(); 
  }


  // ----------------------------------------- Dialog Box for Adding/Editing Students ------------------------------------------

  const genders = ref([
    {label: 'Male', value: 1}, 
    {label: 'Female', value: 2}
  ])

  const isCollegeStuOptions = ref([
    {label: 'Yes', value: 1}, 
    {label: 'No', value: 0}
  ])

  const courseList = ref<CourseModelArray>([]); 
  const queryAllCourses = async ()=>{
    const result = await queryAllApi(); 
    if (result.code){
      courseList.value = result.data; 
    }
  }


  const dialogFormVisible = ref<boolean>(false); 
  const formTitle = ref<string>(''); 
  const labelWidth = ref<number>(90); 
  const gutterWidth = ref<number>(32); 
  const stuFormRef = ref<FormInstance>(); 


  const rules = ref<FormRules<StudentModel>>({
    studentName: [
      { required: true, message: 'Please enter name', trigger: 'blur' },
      { min: 1, max: 50, message: 'Name length should be 1 to 60', trigger: 'blur' },
    ],
    studentNumber: [
        { required: true, message: 'Please enter student number', trigger: 'blur' },
        { min: 10, max: 10, message: 'Student number should be of length 10', trigger: 'blur' },
    ],
    gender: [{ required: true, message: 'Please select gender', trigger: 'blur' }], 
    phone: [
        { required: true, message: 'Please enter phone number', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/g, message: 'Please enter a valid phone number', trigger: 'blur' }
    ],
    idCardNum: [
        { min: 18, max: 18, message: 'ID card number should be of length 10', trigger: 'blur' },
    ],
    // isCollegeStu: [
    //     { message: 'Please select whether or not the student is a college student', trigger: 'blur' },
    // ],
    degree: [
        { required: true, message: 'Please select highest level of education', trigger: 'blur' },
    ],
    isCollegeStu: [
        { required: true, message: 'Please select if the student is in college', trigger: 'blur' },
    ],
    courseId: [
        { required: true, message: 'Please select the course the student is in', trigger: 'blur' },
    ],
    violationCount: [
      {
        validator: (rule, value, callback) => {
          // Skip validation if empty
          if (!value && value !== 0) { // allows "", null, undefined, but not 0
            callback();
            return;
          }
          
          // Validate non-empty values
          if (!/^\d+$/.test(value)) {
            callback(new Error("Must be a non-negative integer"));
          } else if (parseInt(value, 10) < 0) {
            callback(new Error("Must be ≥ 0"));
          } else {
            callback();
          }
        },
        trigger: "blur"
      }
    ],
    penaltyPoints: [
      {
        validator: (rule, value, callback) => {
          // Skip validation if empty
          if (!value && value !== 0) { // allows "", null, undefined, but not 0
            callback();
            return;
          }
          
          // Validate non-empty values
          if (!/^\d+$/.test(value)) {
            callback(new Error("Must be a non-negative integer"));
          } else if (parseInt(value, 10) < 0) {
            callback(new Error("Must be ≥ 0"));
          } else {
            callback();
          }
        },
        trigger: "blur"
      }
    ],
  })
  

  const student = ref<StudentModel>({
    id: undefined,
    studentName: '',
    studentNumber: '',
    gender: '',
    phone: '',
    idCardNum: '',
    isCollegeStu: '',
    address: '',
    degree: '',
    graduationDate: '',
    courseId: '',
    violationCount: undefined,
    penaltyPoints: undefined,
    courseName: ''
  }); 

    const resetForm = (formEl: FormInstance | undefined) => {
      if (!formEl) return
      formEl.resetFields()
    }


  // ----------------------------------------- Add Student ------------------------------------------
  const add = ()=>{
    dialogFormVisible.value = true; 
    formTitle.value = "Add Student"; 
    student.value = {
      id: undefined,
      studentName: '',
      studentNumber: '',
      gender: '',
      phone: '',
      idCardNum: '',
      isCollegeStu: '',
      address: '',
      degree: '',
      graduationDate: '',
      courseId: '',
      violationCount: undefined,
      penaltyPoints: undefined,
      courseName: ''
    }
    resetForm(stuFormRef.value); 
  }

  // ----------------------------------------- Edit Student ------------------------------------------
  const update = async (id: number)=>{
    dialogFormVisible.value = true; 
    formTitle.value = "Edit Student"; 
    const result = await queryInfoApi(id); 
    console.log("id", id); 
    console.log(result); 
    if (result.code){
      student.value = result.data; 
      queryPage(); 
    }
    resetForm(stuFormRef.value); 
  }

  // ----------------------------------------- Save Student (after adding/editing) ------------------------------------------
  const save = async (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    await formEl.validate(async (valid, fields) =>{ 
      if (valid){ 
        let result = null; 
        console.log("student.value", student.value); 
        if (student.value.id){ 
          result = await updateApi(student.value); 
        }
        else{
          result = await addApi(student.value); 
        }
        console.log("result", result)
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


  // ----------------------------------------- Batch Delete Students ------------------------------------------
  let selectedIds = ref<(number|undefined)[]>([]); 
  const handleSelectionChange = (val: StudentModel[]) => {
    selectedIds.value = val.map(element=>{
      return element.id; 
    })
  }

  const deleteBatch = ()=>{
    ElMessageBox.confirm(
      'Are you sure you want to delete the selected student(s)?', 'Batch Delete Students',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
    ).then(async () => {
        const result = await deleteApi(selectedIds.value.toString()); 
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


  // ----------------------------------------- Dialog Box for Deducting Penalty from a students ------------------------------------------
  const penaltyDialogBoxVisible = ref<boolean>(false); 
  const penaltyDialogBoxTitle = ref<string>(''); 
  const penaltyFormRef = ref<FormInstance>(); 

  const penaltyRules = ref<FormRules<StudentModel>>({
    penaltyPoints: [
      {
        validator: (rule, value, callback) => {
          if (value === '' || value === null || value === undefined) {
            callback(new Error('This field is required'));
            return;
          }
          
          // Convert to number and validate
          const num = Number(value);
          if (isNaN(num)) {
            callback(new Error('The number of penalty points must be a valid number'));
          } else if (num < 0) {
            callback(new Error('The number of penalty points must be non-negative'));
          } else if (!Number.isInteger(num)) {
            callback(new Error('The number of penalty points must be an integer'));
          } else {
            callback(); // Validation passed
          }
        },
        trigger: 'blur'
      }
    ]
  });

  const resetPenaltyForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }

  const addPenalty = async (id: number, studentName: string) => {
    penaltyDialogBoxVisible.value = true; 
    penaltyDialogBoxTitle.value = 'Add Penalty Points to ' + studentName; 
    resetPenaltyForm(penaltyFormRef.value); 
    const result = await queryInfoApi(id);
    if (result.code){
      student.value = result.data; 
    }
  }

  const savePenaltyPoints = async (formEl: FormInstance | undefined) => {
    if (!formEl) return; 
    await formEl.validate(async (valid, fields) =>{ 
      if (valid){ 
        const result = await applyPenaltyApi(student.value.id as number, student.value.penaltyPoints as number); 
        console.log("result", result)
        if (result.code){ // success
          ElMessage.success('Success'); 
          penaltyDialogBoxVisible.value = false; 
          queryPage(); 
        }
        else { // failure
          ElMessage.error(result.msg); 
        }
      }
    })
  }

</script>


<template>
  <h1>Student Manager</h1><br></br>
  <!-- Search bar -->
   <el-form :inline="true" :model="studentForm" class="demo-form-inline">
    <el-form-item label="Name">
      <el-input v-model="studentForm.studentName" placeholder="Enter student name" clearable />
    </el-form-item>
    <el-form-item label="Highest Education Level">
      <el-select v-model="studentForm.degree" placeholder="Select highest education level" style="width: 150px" clearable>
        <el-option
          v-for="degree in degreeOptions"
          :key="degree.value"
          :label="degree.label"
          :value="degree.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="Course ID">
      <el-input v-model="studentForm.courseId" placeholder="Enter course ID" clearable />
    </el-form-item><br></br>
    
    <el-form-item>
      <el-button type="primary" @click="queryPage">Search</el-button>
      <el-button @click="clear">Clear</el-button>
      
    </el-form-item>
   <div style="float: right;">
    <el-button type="danger" @click="deleteBatch" style="margin-right: 10px;">Batch Delete Students</el-button>
    <el-button type="success" @click="add">Add Student</el-button>
  </div>
  </el-form>

  <!-- Student Data Table -->
  <el-table :data="stuList" border style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column prop="studentName" label="Student Name" width="100" />
    <el-table-column prop="studentNumber" label="Student Number" width="115" />
    <el-table-column prop="gender" label="Gender" width="85">
      <template #default="scope">
        {{ scope.row.gender ==1 ? "Male" : "Female" }}
      </template>
    </el-table-column>/>
    <el-table-column prop="phone" label="Phone" width="120" />
    <el-table-column prop="idCardNum" label="ID Card No." width="105" />
    <el-table-column prop="isCollegeStu" label="Is College Student" width="80">
      <template #default="scope">
        {{ scope.row.isCollegeStu ==1 ? "Yes" : "No" }}
      </template>
    </el-table-column>/>
    <el-table-column prop="address" label="Address" width="100" />
    <el-table-column prop="degree" label="Highest Education Level" width="100">
      <template #default="scope">
        <span v-if="scope.row.degree==1">Middle School</span>
        <span v-else-if="scope.row.degree==2">High School</span>
        <span v-else-if="scope.row.degree==3">Junior College</span>
        <span v-else-if="scope.row.degree==4">Bachelor's</span>
        <span v-else-if="scope.row.degree==5">Master's</span>
        <span v-else-if="scope.row.degree==6">Doctorate</span>
        <span v-else>Other</span>
      </template>
    </el-table-column>/>
    <el-table-column prop="graduationDate" label="Graduation Date" width="85" />
    <el-table-column prop="courseName" label="Course Name" width="90" />
    <el-table-column prop="violationCount" label="Violation Count" width="85" />
    <el-table-column prop="penaltyPoints" label="Penalty Points" width="80" />
    <el-table-column label="Actions">
      <template #default="scope">
        <el-button size="small" type="primary" @click="addPenalty(scope.row.id, scope.row.studentName)">Add Penalty Points</el-button>
        <el-button size="small" type="primary" @click="update(scope.row.id)">Edit</el-button>
      </template>
    </el-table-column>
  </el-table><br></br>


  <!-- Pagination -->
   <br></br>
  <el-pagination
      v-model:current-page="pagination.currentPage"
      v-model:page-size="pagination.pageSize"
      :page-sizes="[3, 5, 10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />


  <!-- Dialog Box for adding/editing student -->
   <el-dialog v-model="dialogFormVisible" :title="formTitle" width="850">
      <el-form :model="student" ref="stuFormRef" :rules = rules>
        <!-- First Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="Name" :label-width="labelWidth" prop="studentName">
              <el-input v-model="student.studentName" placeholder="Enter a name of length 1 to 60"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Student No." :label-width="100" prop="studentNumber">
              <el-input v-model="student.studentNumber" placeholder="Enter a student number of length 10"/>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- Second Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="Gender" :label-width="labelWidth" prop="gender">
              <el-select v-model="student.gender" placeholder="Select gender" clearable>
                <el-option v-for="gender in genders" 
                  :key="gender.value" :value="gender.value" :label="gender.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Phone" :label-width="labelWidth" prop="phone">
              <el-input v-model="student.phone" placeholder="Enter student phone number"/>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Third Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="ID Card No." :label-width="labelWidth" prop="idCardNum">
              <el-input v-model="student.idCardNum" placeholder="Enter student ID Card number"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Is College Student" :label-width="160" prop="isCollegeStu">
              <el-select v-model="student.isCollegeStu" placeholder="Is the student a college student?" clearable>
                <el-option v-for="option in isCollegeStuOptions" 
                  :key="option.value" :value="option.value" :label="option.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Fourth Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="Address" :label-width="labelWidth" prop="address">
              <el-input v-model="student.address" placeholder="Enter student address"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Highest Education Level" :label-width="180" prop="degree">
              <el-select v-model="student.degree" placeholder="Select highest education level" clearable>
                <el-option v-for="option in degreeOptions" 
                  :key="option.value" :value="option.value" :label="option.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Fifth Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="Graduation Date" :label-width="130" prop="graduationDate">
              <el-input v-model="student.graduationDate" type="date" placeholder="Select student graduation date" value-format="YYYY-MM-DD"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Course" :label-width="labelWidth" prop="courseId">
              <el-select v-model="student.courseId" placeholder="Select the course the student is taking" clearable>
                <el-option v-for="(course, index) in courseList" :key="index" :label="course.courseName" :value="course.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Sixth Row -->
        <el-row :gutter=gutterWidth>
          <el-col :span="12">
            <el-form-item label="Violation Count" :label-width="120" prop="violationCount">
              <el-input v-model="student.violationCount" placeholder="Enter student violation count"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Penalty Points" :label-width="115" prop="penaltyPoints">
              <el-input v-model="student.penaltyPoints" placeholder="Enter the total number of penalty points"/>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible=false">Cancel</el-button>
          <el-button type="primary" @click="save(stuFormRef)">Save</el-button>
        </span>
      </template>
  </el-dialog>


  <!-- Dialog box for adding penalty points to selected student -->
  <el-dialog v-model="penaltyDialogBoxVisible" :title="penaltyDialogBoxTitle" width="500">
    <el-form :model="student" ref="penaltyFormRef" :rules = penaltyRules>
      <el-form-item label="Number of penalty points to give" prop="penaltyPoints">
        <el-input v-model="student.penaltyPoints" autocomplete="off"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="penaltyDialogBoxVisible = false">Cancel</el-button>
        <el-button type="primary" @click="savePenaltyPoints(penaltyFormRef)">Confirm</el-button>
      </div>
    </template>
  </el-dialog>

</template>

<style scoped>

</style>