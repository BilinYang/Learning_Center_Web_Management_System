//统一响应结果接口
export interface ResultModel {
  code: number,
  msg: string,
  data: any
}



//分页结果接口
export interface PageModel {
  total: number,
  rows: any[]
}

//统一响应结果接口
export interface PageResultModel {
  code: number,
  msg: string,
  data: PageModel
}

//分页参数接口
export interface PaginationParam {
  currentPage: number,
  pageSize: number,
  total: number
}



// ----------------------- 部门数据相关接口及类型 ---------------------
//部门数据接口
export interface DeptModel {
  id?: number,
  name: string,
  updateTime?: string
}

//部门数据数组
export type DeptModelArray = DeptModel[]


// --------------------- 员工数据相关接口及类型 ---------------------
//员工搜索条件接口
export interface SearchEmpModel {
  name: string, //姓名
  gender: string, //性别
  begin: string, //开始时间
  end: string, //结束时间
  date: string[] //时间范围
}

//员工工作经历数据接口
export interface EmpExprModel {
  id?: number,
  empId?: number,
  exprDate: string[], //时间范围
  begin: string,
  end: string,
  company: string,
  jobPosition: string
}

//员工数据接口
export interface EmpModel {
  id?: number,
  username: string,
  password: string,
  name: string,
  gender: string,
  phone: string,
  job: string,
  salary: string,
  image: string,
  entryDate: string,
  deptId: string,
  deptName?: string,
  exprList: EmpExprModel[]
}

//员工数据数组
export type EmpModelArray = EmpModel[]


//-------------------------------- 登录 ---------------
//登录表单信息
export interface LoginEmp {
  username?: string,
  password?: string
}

//登录成功返回信息
export interface LoginInfo {
  id?: number,
  name?: string,
  username?: string,
  token?: string
}

//--------------------- 日志 ------------------------
export interface LogModel {
  id?: number,
  operateEmpId: number,
  operateTime: string,
  className: string,
  methodName: string,
  methodParams: string,
  returnValue: string,
  costTime: number,
  operateEmpName: string
}


//日志数据数组
export type LogModelArray = LogModel[]



//-- --------------------------------- 班级管理相关接口 ---------------------------
//班级搜索条件接口
export interface SearchCourseModel {
  courseName: string, //姓名
  beginDate: string, //开始时间
  endDate: string, //结束时间
  date: string[] //时间范围
}

//班级数据接口
export interface CourseModel {
  id: string,
  courseName: string,
  room: string,
  beginDate: string,
  endDate: string,
  subject: string, 
  headTeacherId: string,
  headTeacherName?: string
}

//班级数据数组
export type CourseModelArray = CourseModel[]


//-- --------------------------------- 学生管理相关接口 ---------------------------
//学生搜索条件接口
export interface SearchStudentModel {
  courseId: string, //班级
  degree: string, //学历
  studentName: string //姓名
}

//学生数据接口
export interface StudentModel {
  id?: number,
  studentName: string,
  studentNumber: string,
  gender: string,
  phone: string,
  idCardNum: string,
  isCollegeStu: string,
  address: string,
  degree: string,
  graduationDate: string,
  courseId: string,
  violationCount?: number,
  penaltyPoints?: number
  courseName?: string
}

//学生数据数组
export type StuModelArray = StudentModel[]

