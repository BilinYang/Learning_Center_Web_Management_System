<script setup lang="ts">
  import { addApi, deleteApi, getCourseApi, queryAllApi, queryPageApi, updateApi } from '@/api/course';
  import type { CourseModel, CourseModelArray, PaginationParam, SearchCourseModel } from '@/api/model/model';
  import type { FormInstance } from 'element-plus';
  import {ElMessage, ElMessageBox} from 'element-plus'
  import {ref, watch, onMounted, reactive} from 'vue'

  onMounted(()=>{
    queryPage(); 
  })


  // ----------------------------------------- Search bar ------------------------------------------

  const searchCourse = ref<SearchCourseModel>({
    courseName: '', 
    beginDate: '', 
    endDate: '', 
    date: [], 
  })

  const courseList = ref<CourseModelArray>([]); 
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
  const handleCurrentChange = (page: number) => {
    console.log(`current page: ${page}`); 
    pagination.value.currentPage = page; 
    queryPage(); 
  }
  const queryPage = async ()=>{
    const result = await queryPageApi(searchCourse.value.courseName, searchCourse.value.beginDate, 
               searchCourse.value.endDate, pagination.value.currentPage, pagination.value.pageSize);
    if (result.code){
      courseList.value = result.data.rows; 
      console.log('result', result);
      console.log("courseList", courseList)
      pagination.value.total = result.data.total; 
    }
  }

  watch(()=>searchCourse.value.date, (newVal, oldVal)=>{
    if (newVal){
      console.log("newVal", newVal); 
      searchCourse.value.beginDate = newVal[0]; 
      searchCourse.value.endDate = newVal[1]; 
    }
    else{
      searchCourse.value.beginDate = ''; 
      searchCourse.value.endDate = ''; 
    }
  }, {deep: true}); 

  const clear = () =>{
    searchCourse.value = {
      courseName: '', 
      beginDate: '', 
      endDate: '', 
      date: [], 
    }
    queryPage(); 
  }



  // ----------------------------------------- Dialog Box ------------------------------------------

  const dialogFormVisible = ref(false);
  const formTitle = ref<string>(''); 
  const labelWidth = ref<number>(90); 
  const gutterWidth = ref<number>(32); 
  const courseFormRef = ref<FormInstance>(); 

  const subjects = ref([
    { name: "Java", value: 1 }, 
    { name: "Frontend", value: 2 }, 
    { name: "Big Data", value: 3 }, 
    { name: "Python", value: 4 }, 
    { name: "Go", value: 5 }, 
    { name: "Embedded Systems", value: 6 }, 
    { name: "Other", value: 7 }, 
  ])

  const rules = {
    courseName: [
      {required: true, message: "Course name is required", trigger: "blur"}, 
      {min: 1, max: 50, message: "Length should be 1 to 50", trigger: "blur"}, 
    ], 
    room: [
      {required: true, message: "Room number is reuiqred", trigger: "blur"}, 
      {min: 1, max: 30, message: "Length should be 1 to 30", trigger: "blur"}, 
    ], 
    beginDate: [
      {required: true, message: "Course start date is required", trigger: "blur"}, 
    ], 
    endDate: [
      {required: true, message: "Course end date is required", trigger: "blur"}, 
    ], 
    subject: [
      {required: true, message: "Course subject is required", trigger: "blur"}, 
    ], 
    headTeacherId: [
      {required: true, message: "Head teacher ID is required and must be a non-negative number", trigger: "blur"}, 
      {
        validator: (rule, value, callback) => {
        if (value === "") {
          callback(new Error("Please input count"))
        } else if (!/^\d+$/.test(value)) {
          callback(new Error("Head teacher ID must be a valid, non-negative integer"))
        } else {
          const num = parseInt(value, 10)
          if (num < 0) {
            callback(new Error("Must be unsigned (≥ 0)"))
          } else {
            callback() // validation passed
          }
        }
      },
        trigger: "blur"
      }
    ], 
    headTeacherName: [
      {min: 1, max: 60, message: "Length should be 0 to 60", trigger: "blur"}, 
    ], 

  }

  const course = ref<CourseModel>({
    id: '',
    courseName: '',
    room: '',
    beginDate: '',
    endDate: '',
    subject: '', 
    headTeacherId: '',
    headTeacherName: ''
  });

  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.resetFields();
  }

  // saves course data (runs after the save button is clicked) [ used for both adding and editing courses ]
  const save = async (formEl: FormInstance | undefined) => {
    if (!formEl) return; 
    formEl.validate(async (valid)=>{
      if (valid){
        let result = null; 
        if (course.value.id) { // this is when a course is being updated (because it will already have an id)
          result = await updateApi(course.value); 
        }
        else { // this is when a new course is being added
          result = await addApi(course.value); 
        }

        if (result.code){
          ElMessage.success('Success'); 
          dialogFormVisible.value = false; 
          queryPage(); 
        }
        else {
          ElMessage.error(result.msg); 
        }
      }

    })
  }
  

  // ----------------------------------------- Add Course ------------------------------------------
  const add = ()=>{
    dialogFormVisible.value = true; 
    formTitle.value = "Add Course"; 
    course.value = {
      id: '',
      courseName: '',
      room: '',
      beginDate: '',
      endDate: '',
      subject: '', 
      headTeacherId: '',
      headTeacherName: ''
    }
    resetForm(courseFormRef.value); 
  }

  // ----------------------------------------- Edit Course ------------------------------------------
  const updateCourse = async (id: number) => {
    dialogFormVisible.value = true; 
    formTitle.value = "Edit Course"; 
    resetForm(courseFormRef.value); 
    const result = await getCourseApi(id); 
    if (result.code){
      course.value = result.data; 
    }
  }

  // ----------------------------------------- Delete Course ------------------------------------------
  const deleteCourse = async (id: number) => {
    ElMessageBox.confirm(
    'Are you sure you want to delete this course?',
    'Delete Course',
    {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      const result = await deleteApi(id); 
      if (result.code) {
        ElMessage({
          type: 'success',
          message: 'Deleted Successfully',
        })
        queryPage(); 
      }
      else{
        ElMessage.error(result.msg); 
      }
    })
    .catch(() => {
    })
  }

</script>




<template>
  <h1>Course Manager</h1><br></br>
  <!-- Search bar -->
  <el-form :inline="true" :model="searchCourse" class="demo-form-inline">
    <el-form-item label="Course Name">
      <el-input v-model="searchCourse.courseName" placeholder="Please enter course name" style="width: 260px" clearable />
    </el-form-item>
    <el-form-item label="Course End Date" style="width: 510px">
      <el-date-picker
        v-model="searchCourse.date"
        type="daterange"
        range-separator="To"
        start-placeholder=" Course end date from"
        end-placeholder="Course end date to"
        value-format="YYYY-MM-DD"
        format="YYYY-MM-DD"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="queryPage">Search</el-button>
      <el-button @click="clear">Clear</el-button>
    </el-form-item>
    <el-button type="success" @click="add" style="float:right">Add Course</el-button>
  </el-form>
  

  <!-- Course table -->
  <el-table :data="courseList" border style="width: 100%">
    <el-table-column prop="courseName" label="Course Name" width="180" align="center"/>
    <el-table-column prop="room" label="Room Number" width="100" align="center"/>
    <el-table-column prop="beginDate" label="Start Date" width="130" align="center"/>
    <el-table-column prop="endDate" label="End Date" width="130" align="center"/>
    <el-table-column prop="subject" label="Subject" width="120" align="center">
      <template #default="scope">
        <span v-if="scope.row.subject==1">Java</span>
        <span v-else-if="scope.row.subject==2">Frontend</span>
        <span v-else-if="scope.row.subject==3">Big Data</span>
        <span v-else-if="scope.row.subject==4">Python</span>
        <span v-else-if="scope.row.subject==5">Go</span>
        <span v-else-if="scope.row.subject==6">Embedded Systems</span>
        <span v-else>Other</span>
      </template>
    </el-table-column>/>
    <el-table-column prop="headTeacherName" label="Head Teacher Name" width="130" align="center"/>
    <el-table-column fixed="right" label="Actions" min-width="120" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="updateCourse(scope.row.id)">Edit</el-button>
        <el-button type="danger" size="small" @click="deleteCourse(scope.row.id)">Delete</el-button>
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
    />


  <!-- Dialog box -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="800">
    <el-form :model="course" ref="courseFormRef" :rules = "rules">
        <!-- Row 1 -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Name" :label-width="labelWidth" prop="courseName">
            <el-input v-model="course.courseName" placeholder="Enter course name of length 1 to 50"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Room No." :label-width="labelWidth" prop="room">
            <el-input v-model="course.room" placeholder="Enter room number for course"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 2 -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Start Date" :label-width="labelWidth" prop="beginDate">
            <el-input v-model="course.beginDate" type="date" placeholder="Select course start date" value-format="YYYY-MM-DD"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="End Date" :label-width="labelWidth" prop="endDate">
            <el-input v-model="course.endDate" type="date" placeholder="Select course end date" value-format="YYYY-MM-DD"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 3 -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Subject" :label-width="labelWidth" prop="subject">
            <el-select v-model="course.subject" placeholder="Select course subject" style="width: 100%;">
              <el-option v-for="subject in subjects" :key="subject.value" :label="subject.name" :value="subject.value"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Head Teacher ID" :label-width="130" prop="headTeacherId">
            <el-input v-model="course.headTeacherId" placeholder="Enter head teacher ID"/>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- Row 4 -->
      <el-row :gutter=gutterWidth>
        <el-col :span="12">
          <el-form-item label="Head Teacher Name" :label-width="150" prop="headTeacherName">
            <el-input v-model="course.headTeacherName" placeholder="Enter name of header teacher"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible=false">Cancel</el-button>
        <el-button type="primary" @click="save(courseFormRef)">Save</el-button>
      </span>
    </template>
  </el-dialog>


</template>


<style scoped>

</style>